package com.android.staymates.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.android.staymates.data.models.Profile
import com.android.staymates.data.repositories.AuthRepository

data class AuthState(
    val isLoggedIn: Boolean = false,
    val userId: String? = null,
    val userProfile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun rememberAuthState(): AuthState {
    val repository = remember { AuthRepository() }
    val (isLoggedIn, setIsLoggedIn) = remember { mutableStateOf(repository.isUserLoggedIn()) }
    val (userId, setUserId) = remember { mutableStateOf(repository.getCurrentUserId()) }
    val (userProfile, setUserProfile) = remember { mutableStateOf<Profile?>(null) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    return AuthState(
        isLoggedIn = isLoggedIn,
        userId = userId,
        userProfile = userProfile,
        isLoading = isLoading,
        error = error
    )
}
