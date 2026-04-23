package com.android.staymates.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.staymates.data.models.Profile
import com.android.staymates.data.repositories.AuthRepository
import com.android.staymates.ui.theme.GradientEnd
import com.android.staymates.ui.theme.GradientMid
import com.android.staymates.ui.theme.GradientStart
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val repository = remember { AuthRepository() }
    val coroutineScope = rememberCoroutineScope()

    // Step 1 — Personal Info
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }

    // Step 2 — Preferences & Bio
    var preferences by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    // Step 3 — Credentials
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var step by remember { mutableIntStateOf(0) }       // 0, 1, 2
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val totalSteps = 3
    val progress = (step + 1).toFloat() / totalSteps

    val stepTitles = listOf("Personal Info", "About You", "Your Account")
    val stepSubtitles = listOf(
        "Tell us the basics about yourself",
        "Share your lifestyle preferences",
        "Create your login credentials"
    )

    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(GradientStart, GradientMid, GradientEnd)
    )

    fun validateStep(): String? = when (step) {
        0 -> when {
            fullName.isBlank() -> "Please enter your full name"
            age.isBlank() -> "Please enter your age"
            age.toIntOrNull() == null || (age.toIntOrNull() ?: 0) < 16 -> "Please enter a valid age (16+)"
            occupation.isBlank() -> "Please enter your occupation"
            else -> null
        }
        1 -> null // optional step
        2 -> when {
            email.isBlank() -> "Please enter your email"
            password.isBlank() -> "Please enter a password"
            password.length < 6 -> "Password must be at least 6 characters"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
        else -> null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { if (step > 0) step-- else onBack() },
                        enabled = !isLoading
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            // ── Step indicator ─────────────────────────────────────────────────
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(totalSteps) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(CircleShape)
                            .background(
                                if (index <= step)
                                    Brush.horizontalGradient(listOf(GradientStart, GradientMid))
                                else
                                    Brush.horizontalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Step ${step + 1} of $totalSteps",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(18.dp))

            // ── Step title ─────────────────────────────────────────────────────
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                },
                label = "step_title"
            ) { targetStep ->
                Column {
                    Text(
                        text = stepTitles[targetStep],
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stepSubtitles[targetStep],
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Step content ───────────────────────────────────────────────────
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                },
                label = "step_content"
            ) { targetStep ->
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    when (targetStep) {
                        0 -> {
                            StyledTextField(
                                value = fullName,
                                onValueChange = { fullName = it; error = null },
                                label = "Full Name",
                                leadingIcon = { Icon(Icons.Filled.Person, null, tint = MaterialTheme.colorScheme.primary) },
                                enabled = !isLoading
                            )
                            StyledTextField(
                                value = age,
                                onValueChange = { age = it.filter { c -> c.isDigit() }.take(3); error = null },
                                label = "Age",
                                leadingIcon = { Icon(Icons.Filled.Badge, null, tint = MaterialTheme.colorScheme.primary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                enabled = !isLoading
                            )
                            StyledTextField(
                                value = occupation,
                                onValueChange = { occupation = it; error = null },
                                label = "Occupation",
                                leadingIcon = { Icon(Icons.Filled.Work, null, tint = MaterialTheme.colorScheme.primary) },
                                enabled = !isLoading
                            )
                        }

                        1 -> {
                            OutlinedTextField(
                                value = preferences,
                                onValueChange = { preferences = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Roommate Preferences") },
                                placeholder = { Text("e.g., Non-smoker, quiet hours, pet-friendly…") },
                                enabled = !isLoading,
                                minLines = 4,
                                shape = RoundedCornerShape(16.dp),
                                colors = styledTextFieldColors()
                            )
                            OutlinedTextField(
                                value = bio,
                                onValueChange = { bio = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("About Me") },
                                placeholder = { Text("Tell future roommates about yourself…") },
                                enabled = !isLoading,
                                minLines = 4,
                                shape = RoundedCornerShape(16.dp),
                                colors = styledTextFieldColors()
                            )
                        }

                        2 -> {
                            StyledTextField(
                                value = email,
                                onValueChange = { email = it; error = null },
                                label = "Email Address",
                                leadingIcon = { Icon(Icons.Filled.Email, null, tint = MaterialTheme.colorScheme.primary) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                enabled = !isLoading
                            )
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it; error = null },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Password") },
                                leadingIcon = { Icon(Icons.Filled.Lock, null, tint = MaterialTheme.colorScheme.primary) },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                enabled = !isLoading,
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = styledTextFieldColors()
                            )
                            OutlinedTextField(
                                value = confirmPassword,
                                onValueChange = { confirmPassword = it; error = null },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Confirm Password") },
                                leadingIcon = { Icon(Icons.Filled.Lock, null, tint = MaterialTheme.colorScheme.primary) },
                                trailingIcon = {
                                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                        Icon(
                                            if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                enabled = !isLoading,
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = styledTextFieldColors()
                            )
                        }
                    }
                }
            }

            // ── Error banner ───────────────────────────────────────────────────
            if (error != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(14.dp)
                ) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Next / Create Account button ───────────────────────────────────
            Button(
                onClick = {
                    val err = validateStep()
                    if (err != null) { error = err; return@Button }
                    error = null

                    if (step < totalSteps - 1) {
                        step++
                    } else {
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                val profile = Profile(
                                    id = "",
                                    name = fullName.trim(),
                                    age = age.toIntOrNull() ?: 0,
                                    occupation = occupation.trim(),
                                    preferences = preferences.trim(),
                                    bio = bio.trim(),
                                    isVerified = false
                                )
                                repository.signUp(email.trim(), password, profile)
                                onRegisterSuccess()
                            } catch (e: Exception) {
                                error = e.message ?: "Registration failed"
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = if (step < totalSteps - 1) "Continue →" else "Create Account",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }

            if (step == 1) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.TextButton(onClick = { error = null; step++ }) {
                        Text(
                            text = "Skip for now",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


