package com.maxxxwk.sportseventsapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
@Suppress("FunctionNaming")
fun SportsEventsAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = lightColors(
            primary = Gray,
            onPrimary = Color.White,
            secondary = DarkGray,
            onSecondary = Color.White
        ),
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}