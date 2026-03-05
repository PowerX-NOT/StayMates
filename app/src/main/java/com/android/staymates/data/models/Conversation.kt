package com.android.staymates.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: String,
    @SerialName("participant_1_id")
    val participant1Id: String,
    @SerialName("participant_2_id")
    val participant2Id: String,
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = ""
)

data class ConversationWithDetails(
    val conversation: Conversation,
    val otherParticipant: Profile,
    val lastMessage: Message?,
    val unreadCount: Int
)
