package com.example.todoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val HighPriorityColor = Color(color = 0xFFff0000)
val MediumPriorityColor = Color(color = 0xFFFFFF00)
val LowPriorityColor = Color(color = 0xFF00FF00)
val NonePriorityColor = Color(color = 0xFF9C9C9C)

val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0xFF9C9C9C)
val DarkGray = Color(0xFF141414)
val Teal200 = Color(0xFF03DAC5)

val Colors.taskItemBackgroundColor : Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else DarkGray

val Colors.taskItemContentColor : Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.Black else LightGray

val Colors.topAppBarContentColor : Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Color.White else LightGray

val Colors.topAppBarBackgroundColor : Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Purple40 else Color.Black

val Colors.fabBarBackgroundColor : Color
    @Composable
    get() = if (!isSystemInDarkTheme()) Teal200 else PurpleGrey40