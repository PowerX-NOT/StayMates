package com.android.staymates.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("matched_user_id")
    val matchedUserId: String,
    @SerialName("match_score")
    val matchScore: Int,
    val status: String = "pending",
    @SerialName("created_at")
    val createdAt: String = ""
)

data class MatchWithProfile(
    val match: Match,
    val profile: Profile
)
