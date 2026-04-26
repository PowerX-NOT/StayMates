package com.android.staymates.data.repositories

import com.android.staymates.data.SupabaseClient
import com.android.staymates.data.models.Profile
import io.github.jan.supabase.postgrest.from

class MatchRepository(private val userId: String) {
    private val client = SupabaseClient.instance

    data class ProfileMatch(
        val profile: Profile,
        val matchScore: Int,
    )

    suspend fun getMatches(limit: Int = 50): List<ProfileMatch> {
        val currentUserProfile = client.from("profiles")
            .select {
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingleOrNull<Profile>()

        if (currentUserProfile == null) return emptyList()

        val allProfiles = client.from("profiles")
            .select()
            .decodeList<Profile>()

        return allProfiles
            .asSequence()
            .filter { it.id != userId }
            .map { other ->
                ProfileMatch(
                    profile = other,
                    matchScore = calculateMatchScore(currentUserProfile, other),
                )
            }
            .sortedByDescending { it.matchScore }
            .take(limit)
            .toList()
    }

    suspend fun getProfileById(profileId: String): Profile? {
        return try {
            client.from("profiles")
                .select {
                    filter { eq("id", profileId) }
                }
                .decodeSingleOrNull<Profile>()
        } catch (_: Exception) { null }
    }

    private fun calculateMatchScore(a: Profile, b: Profile): Int {
        val ageScore = ageCompatibility(a.age, b.age)
        val keywordScore = keywordSimilarity(
            "${a.preferences} ${a.bio} ${a.occupation}",
            "${b.preferences} ${b.bio} ${b.occupation}",
        )
        val verifiedBonus = if (b.isVerified) 5 else 0

        return (ageScore * 0.35 + keywordScore * 0.65).toInt().coerceIn(0, 100) + verifiedBonus
    }

    private fun ageCompatibility(ageA: Int, ageB: Int): Int {
        if (ageA <= 0 || ageB <= 0) return 50
        val diff = kotlin.math.abs(ageA - ageB)
        return (100 - diff * 7).coerceIn(0, 100)
    }

    private fun keywordSimilarity(textA: String, textB: String): Int {
        val tokensA = tokenize(textA)
        val tokensB = tokenize(textB)

        if (tokensA.isEmpty() || tokensB.isEmpty()) return 40

        val intersection = tokensA.intersect(tokensB).size
        val union = tokensA.union(tokensB).size

        if (union == 0) return 40
        return ((intersection.toDouble() / union.toDouble()) * 100.0).toInt().coerceIn(0, 100)
    }

    private fun tokenize(text: String): Set<String> {
        return text
            .lowercase()
            .split(Regex("[^a-z0-9]+"))
            .asSequence()
            .map { it.trim() }
            .filter { it.length >= 3 }
            .filterNot { it in STOP_WORDS }
            .toSet()
    }

    private companion object {
        val STOP_WORDS = setOf(
            "and",
            "the",
            "for",
            "with",
            "from",
            "this",
            "that",
            "have",
            "has",
            "are",
            "you",
            "your",
            "not",
        )
    }
}
