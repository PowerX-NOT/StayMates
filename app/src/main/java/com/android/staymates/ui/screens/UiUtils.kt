package com.android.staymates.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ── Shared UI helpers ─────────────────────────────────────────────────────────

/** Returns a deterministic gradient color pair based on the name string. */
fun avatarColors(name: String): Pair<Color, Color> {
    val colors = listOf(
        Pair(Color(0xFF5C6BC0), Color(0xFF7C4DFF)),
        Pair(Color(0xFFFF7043), Color(0xFFFFB300)),
        Pair(Color(0xFF26A69A), Color(0xFF00BCD4)),
        Pair(Color(0xFFEC407A), Color(0xFFAB47BC)),
        Pair(Color(0xFF42A5F5), Color(0xFF26C6DA)),
    )
    val index = name.hashCode().mod(colors.size).let { if (it < 0) it + colors.size else it }
    return colors[index]
}

@Composable
fun styledTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline
)

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { androidx.compose.material3.Text(label) },
        leadingIcon = leadingIcon,
        enabled = enabled,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(16.dp),
        colors = styledTextFieldColors()
    )
}
