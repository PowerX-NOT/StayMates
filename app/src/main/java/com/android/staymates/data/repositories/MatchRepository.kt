package com.android.staymates.data.repositories

import com.android.staymates.data.SupabaseClient
import com.android.staymates.data.models.Match
import com.android.staymates.data.models.MatchWithProfile
import com.android.staymates.data.models.Profile
import io.github.jan.supabase.postgrest.from

class MatchRepository(private val userId: String) {
    private val client = SupabaseClient.instance

    suspend fun getMatches(): List<MatchWithProfile> {
        val matchesAsUser = try {
            client.from("matches")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<Match>()
        } catch (_: Exception) {
            emptyList()
        }

        val matchesAsMatchedUser = try {
            client.from("matches")
                .select {
                    filter {
                        eq("matched_user_id", userId)
                    }
                }
                .decodeList<Match>()
        } catch (_: Exception) {
            emptyList()
        }

        val allMatches = matchesAsUser + matchesAsMatchedUser

        val matchesWithProfiles = mutableListOf<MatchWithProfile>()

        for (match in allMatches) {
            val profileId = if (match.userId == userId) match.matchedUserId else match.userId

            try {
                val profile = client.from("profiles")
                    .select {
                        filter {
                            eq("id", profileId)
                        }
                    }
                    .decodeSingleOrNull<Profile>()

                if (profile != null) {
                    matchesWithProfiles.add(MatchWithProfile(match, profile))
                }
            } catch (_: Exception) {
            }
        }

        return matchesWithProfiles.sortedByDescending { it.match.matchScore }
    }

    suspend fun updateMatchStatus(matchId: String, status: String) {
        client.from("matches")
            .update(mapOf("status" to status)) {
                filter {
                    eq("id", matchId)
                }
            }
    }
}
