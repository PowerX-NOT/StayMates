package com.android.staymates.data.repositories

import com.android.staymates.data.SupabaseClient
import com.android.staymates.data.models.Conversation
import com.android.staymates.data.models.ConversationWithDetails
import com.android.staymates.data.models.Message
import com.android.staymates.data.models.Profile
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ChatRepository(private val userId: String) {
    private val client = SupabaseClient.instance

    suspend fun getConversations(): List<ConversationWithDetails> {
        val conversations = client.from("conversations")
            .select()
            .decodeList<Conversation>()
            .filter { it.participant1Id == userId || it.participant2Id == userId }

        val conversationsWithDetails = mutableListOf<ConversationWithDetails>()

        for (conversation in conversations) {
            val otherParticipantId = if (conversation.participant1Id == userId) {
                conversation.participant2Id
            } else {
                conversation.participant1Id
            }

            val otherParticipant = client.from("profiles")
                .select()
                .decodeList<Profile>()
                .firstOrNull { it.id == otherParticipantId }

            val messages = client.from("messages")
                .select()
                .decodeList<Message>()
                .filter { it.conversationId == conversation.id }
                .sortedByDescending { it.createdAt }

            val lastMessage = messages.firstOrNull()
            val unreadCount = messages.count { !it.isRead && it.senderId != userId }

            if (otherParticipant != null) {
                conversationsWithDetails.add(
                    ConversationWithDetails(
                        conversation = conversation,
                        otherParticipant = otherParticipant,
                        lastMessage = lastMessage,
                        unreadCount = unreadCount
                    )
                )
            }
        }

        return conversationsWithDetails.sortedByDescending { it.conversation.updatedAt }
    }

    suspend fun getMessagesForConversation(conversationId: String): List<Message> {
        return client.from("messages")
            .select()
            .decodeList<Message>()
            .filter { it.conversationId == conversationId }
            .sortedBy { it.createdAt }
    }

    suspend fun sendMessage(conversationId: String, senderId: String, content: String) {
        client.from("messages")
            .insert(
                buildJsonObject {
                    put("conversation_id", conversationId)
                    put("sender_id", senderId)
                    put("content", content)
                    put("is_read", false)
                }
            )

        client.from("conversations")
            .update(
                mapOf("updated_at" to java.time.Instant.now().toString())
            ) {
                filter {
                    eq("id", conversationId)
                }
            }
    }

    suspend fun markMessagesAsRead(conversationId: String, userId: String) {
        val messages = client.from("messages")
            .select()
            .decodeList<Message>()
            .filter { it.conversationId == conversationId && it.senderId != userId && !it.isRead }

        messages.forEach { message ->
            client.from("messages")
                .update(mapOf("is_read" to true)) {
                    filter {
                        eq("id", message.id)
                    }
                }
        }
    }

    suspend fun getConversation(conversationId: String): Conversation? {
        return client.from("conversations")
            .select()
            .decodeList<Conversation>()
            .firstOrNull { it.id == conversationId }
    }

    suspend fun getProfile(profileId: String): Profile? {
        return client.from("profiles")
            .select()
            .decodeList<Profile>()
            .firstOrNull { it.id == profileId }
    }
}
