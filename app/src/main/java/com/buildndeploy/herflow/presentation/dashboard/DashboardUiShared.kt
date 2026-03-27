package com.buildndeploy.herflow.presentation.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.format.DateTimeFormatter

internal val AppBackground = Color(0xFFF2EEF8)
internal val CardBackground = Color(0xFFFCFCFD)
internal val BorderColor = Color(0xFFE4DCEB)
internal val BrandPink = Color(0xFFF6249B)
internal val BrandPurple = Color(0xFF9D3DF2)
internal val TextPrimary = Color(0xFF0E1525)
internal val TextMuted = Color(0xFF334155)
internal val DarkAction = Color(0xFF05072D)
internal val DisplayDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

internal enum class AppSection(val title: String, val icon: ImageVector) {
    Home("Home", Icons.Outlined.Home),
    CycleTracker("Cycle Tracker", Icons.Outlined.FavoriteBorder),
    DailyLog("Daily Log", Icons.Outlined.MonitorHeart),
    Calendar("Calendar", Icons.Outlined.CalendarMonth),
    Analytics("Analytics", Icons.Outlined.Analytics),
    PartnerMode("Partner Mode", Icons.Outlined.People),
    Settings("Settings", Icons.Outlined.Settings)
}

internal enum class DailyLogTab(val label: String, val icon: ImageVector) {
    Symptoms("Symptoms", Icons.Outlined.MonitorHeart),
    Mood("Mood", Icons.Outlined.SentimentSatisfied),
    Mucus("Mucus", Icons.Outlined.WaterDrop),
    BBT("BBT", Icons.Outlined.Insights)
}
