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
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            client.from("profiles")
                .insert(
                    buildJsonObject {
                        put("id", profile.id)
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

    suspend fun getCurrentUserProfile(): Profile? {
        val userId = getCurrentUserId() ?: return null
        return try {
            client.from("profiles")
                .select()
                .decodeList<Profile>()
                .firstOrNull { it.id == userId }
        } catch (e: Exception) {
            null
        }
    }
}
