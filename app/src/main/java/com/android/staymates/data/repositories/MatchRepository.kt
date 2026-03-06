package com.android.staymates.data.repositories

import com.android.staymates.data.SupabaseClient
import com.android.staymates.data.models.Match
import com.android.staymates.data.models.MatchWithProfile
import com.android.staymates.data.models.Profile
import io.github.jan.supabase.postgrest.from

class MatchRepository(private val userId: String) {
    private val client = SupabaseClient.instance

    suspend fun getMatches(): List<MatchWithProfile> {
        val matches = client.from("matches")
            .select()
            .decodeList<Match>()
            .filter { it.userId == userId || it.matchedUserId == userId }

        val matchesWithProfiles = mutableListOf<MatchWithProfile>()

        for (match in matches) {
            val profileId = if (match.userId == userId) match.matchedUserId else match.userId
            val profile = client.from("profiles")
                .select()
                .decodeList<Profile>()
                .firstOrNull { it.id == profileId }

            if (profile != null) {
                matchesWithProfiles.add(MatchWithProfile(match, profile))
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
