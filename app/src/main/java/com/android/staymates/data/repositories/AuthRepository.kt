package com.android.staymates.data.repositories

import com.android.staymates.data.SupabaseClient
import com.android.staymates.data.models.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository {
    private val client = SupabaseClient.instance

    suspend fun signUp(email: String, password: String, profile: Profile): Boolean {
        try {
            val authResult = client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val userId = authResult?.id ?: throw Exception("Failed to create user account")

            client.from("profiles")
                .insert(
                    buildJsonObject {
                        put("id", userId)
                        put("name", profile.name)
                        put("age", profile.age)
                        put("occupation", profile.occupation)
                        put("preferences", profile.preferences)
                        put("bio", profile.bio)
                        put("is_verified", false)
                    }
                )

            return true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun signIn(email: String, password: String): Boolean {
        try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            ensureProfileExists()
            return true
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun signOut() {
        try {
            client.auth.signOut()
        } catch (e: Exception) {
            throw e
        }
    }

    fun getCurrentUserId(): String? {
        return try {
            client.auth.currentUserOrNull()?.id
        } catch (e: Exception) {
            null
        }
    }

    fun isUserLoggedIn(): Boolean {
        return getCurrentUserId() != null
    }

    suspend fun getProfile(userId: String): Profile? {
        return try {
            client.from("profiles")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeList<Profile>()
                .firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun ensureProfileExists(): Profile? {
        val user = try {
            client.auth.currentUserOrNull()
        } catch (e: Exception) {
            null
        } ?: return null

        val userId = user.id
        val existing = getProfile(userId)
        if (existing != null) return existing

        val fallbackName = user.email?.substringBefore("@")?.takeIf { it.isNotBlank() } ?: "User"

        return try {
            client.from("profiles")
                .insert(
                    buildJsonObject {
                        put("id", userId)
                        put("name", fallbackName)
                        put("age", 0)
                        put("occupation", "")
                        put("preferences", "")
                        put("bio", "")
                        put("is_verified", false)
                    }
                )
            getProfile(userId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCurrentUserProfile(): Profile? {
        val userId = getCurrentUserId() ?: return null
        return getProfile(userId)
    }

    suspend fun updateProfile(
        userId: String,
        name: String,
        age: Int,
        occupation: String,
        preferences: String,
        bio: String,
    ) {
        client.from("profiles")
            .update(
                buildJsonObject {
                    put("name", name)
                    put("age", age)
                    put("occupation", occupation)
                    put("preferences", preferences)
                    put("bio", bio)
                }
            ) {
                filter {
                    eq("id", userId)
                }
            }
    }
}
