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
        val conversationsAsParticipant1 = try {
            client.from("conversations")
                .select {
                    filter {
                        eq("participant_1_id", userId)
                    }
                }
                .decodeList<Conversation>()
        } catch (_: Exception) {
            emptyList()
        }

        val conversationsAsParticipant2 = try {
            client.from("conversations")
                .select {
                    filter {
                        eq("participant_2_id", userId)
                    }
                }
                .decodeList<Conversation>()
        } catch (_: Exception) {
            emptyList()
        }

        val allConversations = conversationsAsParticipant1 + conversationsAsParticipant2

        val conversationsWithDetails = mutableListOf<ConversationWithDetails>()

        for (conversation in allConversations) {
            val otherParticipantId = if (conversation.participant1Id == userId) {
                conversation.participant2Id
            } else {
                conversation.participant1Id
            }

            try {
                val otherParticipant = client.from("profiles")
                    .select {
                        filter {
                            eq("id", otherParticipantId)
                        }
                    }
                    .decodeSingleOrNull<Profile>()

                if (otherParticipant == null) continue

                val messages = try {
                    client.from("messages")
                        .select {
                            filter {
                                eq("conversation_id", conversation.id)
                            }
                        }
                        .decodeList<Message>()
                        .sortedByDescending { it.createdAt }
                } catch (_: Exception) {
                    emptyList()
                }

                val lastMessage = messages.firstOrNull()
                val unreadCount = messages.count { !it.isRead && it.senderId != userId }

                conversationsWithDetails.add(
                    ConversationWithDetails(
                        conversation = conversation,
                        otherParticipant = otherParticipant,
                        lastMessage = lastMessage,
                        unreadCount = unreadCount
                    )
                )
            } catch (_: Exception) {
            }
        }

        return conversationsWithDetails.sortedByDescending { it.conversation.updatedAt }
    }

    suspend fun getMessagesForConversation(conversationId: String): List<Message> {
        return try {
            client.from("messages")
                .select {
                    filter {
                        eq("conversation_id", conversationId)
                    }
                }
                .decodeList<Message>()
                .sortedBy { it.createdAt }
        } catch (_: Exception) {
            emptyList()
        }
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
        try {
            val messages = client.from("messages")
                .select {
                    filter {
                        eq("conversation_id", conversationId)
                        neq("sender_id", userId)
                        eq("is_read", false)
                    }
                }
                .decodeList<Message>()

            messages.forEach { message ->
                try {
                    client.from("messages")
                        .update(mapOf("is_read" to true)) {
                            filter {
                                eq("id", message.id)
                            }
                        }
                } catch (_: Exception) {
                }
            }
        } catch (_: Exception) {
        }
    }

    suspend fun getConversation(conversationId: String): Conversation? {
        return try {
            client.from("conversations")
                .select {
                    filter {
                        eq("id", conversationId)
                    }
                }
                .decodeSingleOrNull<Conversation>()
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getProfile(profileId: String): Profile? {
        return try {
            client.from("profiles")
                .select {
                    filter {
                        eq("id", profileId)
                    }
                }
                .decodeSingleOrNull<Profile>()
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Returns the existing conversation ID between [userId] and [otherUserId],
     * or creates a new one and returns its ID.
     */
    suspend fun createOrGetConversation(otherUserId: String): String {
        // Check if conversation already exists (userId as participant1)
        val existingAsP1 = try {
            client.from("conversations")
                .select {
                    filter {
                        eq("participant_1_id", userId)
                        eq("participant_2_id", otherUserId)
                    }
                }
                .decodeList<Conversation>()
                .firstOrNull()
        } catch (_: Exception) { null }

        if (existingAsP1 != null) return existingAsP1.id

        // Check if conversation already exists (userId as participant2)
        val existingAsP2 = try {
            client.from("conversations")
                .select {
                    filter {
                        eq("participant_1_id", otherUserId)
                        eq("participant_2_id", userId)
                    }
                }
                .decodeList<Conversation>()
                .firstOrNull()
        } catch (_: Exception) { null }

        if (existingAsP2 != null) return existingAsP2.id

        // Create a new conversation
        val created = client.from("conversations")
            .insert(
                buildJsonObject {
                    put("participant_1_id", userId)
                    put("participant_2_id", otherUserId)
                }
            ) {
                select()
            }
            .decodeSingle<Conversation>()

        return created.id
    }
}
