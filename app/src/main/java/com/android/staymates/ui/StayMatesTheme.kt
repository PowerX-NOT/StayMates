package com.android.staymates.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.android.staymates.ui.theme.DarkBackground
import com.android.staymates.ui.theme.DarkError
import com.android.staymates.ui.theme.DarkErrorContainer
import com.android.staymates.ui.theme.DarkOnBackground
import com.android.staymates.ui.theme.DarkOnError
import com.android.staymates.ui.theme.DarkOnErrorContainer
import com.android.staymates.ui.theme.DarkOnPrimary
import com.android.staymates.ui.theme.DarkOnPrimaryContainer
import com.android.staymates.ui.theme.DarkOnSecondary
import com.android.staymates.ui.theme.DarkOnSecondaryContainer
import com.android.staymates.ui.theme.DarkOnSurface
import com.android.staymates.ui.theme.DarkOnSurfaceVariant
import com.android.staymates.ui.theme.DarkOnTertiary
import com.android.staymates.ui.theme.DarkOnTertiaryContainer
import com.android.staymates.ui.theme.DarkOutline
import com.android.staymates.ui.theme.DarkPrimary
import com.android.staymates.ui.theme.DarkPrimaryContainer
import com.android.staymates.ui.theme.DarkSecondary
import com.android.staymates.ui.theme.DarkSecondaryContainer
import com.android.staymates.ui.theme.DarkSurfaceColor
import com.android.staymates.ui.theme.DarkSurfaceContainerHighColor
import com.android.staymates.ui.theme.DarkSurfaceContainerLowColor
import com.android.staymates.ui.theme.DarkSurfaceVariantColor
import com.android.staymates.ui.theme.DarkTertiary
import com.android.staymates.ui.theme.DarkTertiaryContainer
import com.android.staymates.ui.theme.LightBackground
import com.android.staymates.ui.theme.LightError
import com.android.staymates.ui.theme.LightErrorContainer
import com.android.staymates.ui.theme.LightOnBackground
import com.android.staymates.ui.theme.LightOnError
import com.android.staymates.ui.theme.LightOnErrorContainer
import com.android.staymates.ui.theme.LightOnPrimary
import com.android.staymates.ui.theme.LightOnPrimaryContainer
import com.android.staymates.ui.theme.LightOnSecondary
import com.android.staymates.ui.theme.LightOnSecondaryContainer
import com.android.staymates.ui.theme.LightOnSurface
import com.android.staymates.ui.theme.LightOnSurfaceVariant
import com.android.staymates.ui.theme.LightOnTertiary
import com.android.staymates.ui.theme.LightOnTertiaryContainer
import com.android.staymates.ui.theme.LightOutline
import com.android.staymates.ui.theme.LightPrimary
import com.android.staymates.ui.theme.LightPrimaryContainer
import com.android.staymates.ui.theme.LightSecondary
import com.android.staymates.ui.theme.LightSecondaryContainer
import com.android.staymates.ui.theme.LightSurface
import com.android.staymates.ui.theme.LightSurfaceContainerHigh
import com.android.staymates.ui.theme.LightSurfaceContainerLow
import com.android.staymates.ui.theme.LightSurfaceVariant
import com.android.staymates.ui.theme.LightTertiary
import com.android.staymates.ui.theme.LightTertiaryContainer
import com.android.staymates.ui.theme.StayMatesTypography

private val LightColors = lightColorScheme(
    primary              = LightPrimary,
    onPrimary            = LightOnPrimary,
    primaryContainer     = LightPrimaryContainer,
    onPrimaryContainer   = LightOnPrimaryContainer,
    secondary            = LightSecondary,
    onSecondary          = LightOnSecondary,
    secondaryContainer   = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary             = LightTertiary,
    onTertiary           = LightOnTertiary,
    tertiaryContainer    = LightTertiaryContainer,
    onTertiaryContainer  = LightOnTertiaryContainer,
    background           = LightBackground,
    onBackground         = LightOnBackground,
    surface              = LightSurface,
    onSurface            = LightOnSurface,
    surfaceVariant       = LightSurfaceVariant,
    onSurfaceVariant     = LightOnSurfaceVariant,
    outline              = LightOutline,
    surfaceContainerLow  = LightSurfaceContainerLow,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    error                = LightError,
    onError              = LightOnError,
    errorContainer       = LightErrorContainer,
    onErrorContainer     = LightOnErrorContainer,
)

private val DarkColors = darkColorScheme(
    primary              = DarkPrimary,
    onPrimary            = DarkOnPrimary,
    primaryContainer     = DarkPrimaryContainer,
    onPrimaryContainer   = DarkOnPrimaryContainer,
    secondary            = DarkSecondary,
    onSecondary          = DarkOnSecondary,
    secondaryContainer   = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary             = DarkTertiary,
    onTertiary           = DarkOnTertiary,
    tertiaryContainer    = DarkTertiaryContainer,
    onTertiaryContainer  = DarkOnTertiaryContainer,
    background           = DarkBackground,
    onBackground         = DarkOnBackground,
    surface              = DarkSurfaceColor,
    onSurface            = DarkOnSurface,
    surfaceVariant       = DarkSurfaceVariantColor,
    onSurfaceVariant     = DarkOnSurfaceVariant,
    outline              = DarkOutline,
    surfaceContainerLow  = DarkSurfaceContainerLowColor,
    surfaceContainerHigh = DarkSurfaceContainerHighColor,
    error                = DarkError,
    onError              = DarkOnError,
    errorContainer       = DarkErrorContainer,
    onErrorContainer     = DarkOnErrorContainer,
)

private val StayMatesShapes = Shapes(
    extraSmall = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    small      = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    medium     = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    large      = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    extraLarge = androidx.compose.foundation.shape.RoundedCornerShape(32.dp),
)

@Composable
fun StayMatesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = StayMatesTypography,
        shapes      = StayMatesShapes,
        content     = content,
    )
}
