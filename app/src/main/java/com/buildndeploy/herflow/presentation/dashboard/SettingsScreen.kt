package com.buildndeploy.herflow.presentation.dashboard

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
internal fun AnalyticsScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Analytics", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Track your patterns and trends", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        CardContainer {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.Analytics, null, tint = Color(0xFFC9CDD6), modifier = Modifier.size(46.dp))
                Text("No data yet", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text("Start tracking your cycle and daily logs to see analytics", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
internal fun PartnerModeScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Spacer(Modifier.height(32.dp))
        CardContainer {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Outlined.FavoriteBorder, null, tint = Color(0xFFFFA8D9), modifier = Modifier.size(56.dp))
                Text("No cycle data available", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text("Start tracking to enable partner view", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
internal fun SettingsScreen() {
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var pillReminderEnabled by remember { mutableStateOf(false) }
    var wellnessTipsEnabled by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2200)
            toastMessage = null
        }
    }

    Box {
        Column {
            Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Manage your preferences and reminders", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsBlock(
                    title = "Notifications",
                    subtitle = "Enable or disable app notifications",
                    switchLabel = "Push Notifications",
                    switchSubtitle = "Receive cycle predictions and reminders",
                    enabled = pushNotificationsEnabled,
                    onToggle = { enabled ->
                        pushNotificationsEnabled = enabled
                        toastMessage = if (enabled) "Push notifications enabled" else "Push notifications disabled"
                    }
                )
                PillReminderBlock(
                    enabled = pillReminderEnabled,
                    onToggle = { enabled ->
                        pillReminderEnabled = enabled
                        toastMessage = if (enabled) "Pill reminder enabled" else "Pill reminder disabled"
                    }
                )
                SettingsBlock(
                    title = "Self-Care Reminders",
                    subtitle = "Phase-aware wellness tips",
                    switchLabel = "Hydration & Wellness Tips",
                    switchSubtitle = "Get personalized tips based on your cycle phase",
                    enabled = wellnessTipsEnabled,
                    onToggle = { enabled ->
                        wellnessTipsEnabled = enabled
                        toastMessage = if (enabled) "Self-care reminders enabled" else "Self-care reminders disabled"
                    }
                )
                CardContainer {
                    Text("Partner Mode", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("Share cycle insights with your partner", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "A supportive guide for partners to understand your cycle and be more helpful during each phase.",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Spacer(Modifier.height(14.dp))
                    OutlinedAction("View Partner Guide", Icons.Outlined.FavoriteBorder)
                }
                CardContainer {
                    Text("Data Management", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("Export, import, or clear your data", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(14.dp))
                    OutlinedAction("Export Data (JSON)", Icons.Outlined.Insights)
                    Spacer(Modifier.height(10.dp))
                    OutlinedAction("Import Data", Icons.Outlined.Insights)
                    Spacer(Modifier.height(10.dp))
                    OutlinedAction("Clear All Data", Icons.Outlined.Close, Color.Red)
                }
                CardContainer {
                    Text("About HerFlow", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold,color = TextPrimary)
                    Text(
                        "HerFlow helps you track and understand your menstrual cycle with comprehensive logging features and intelligent predictions.",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(12.dp))
                    Divider()
                    Text("Version 1.0.0", color = Color(0xFF687588), modifier = Modifier.padding(top = 10.dp))
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        toastMessage?.let { message ->
            ToggleToast(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                message = message
            )
        }
    }
}

@Composable
private fun SettingsBlock(
    title: String,
    subtitle: String,
    switchLabel: String,
    switchSubtitle: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    CardContainer {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(subtitle, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(switchLabel, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(switchSubtitle, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
            }
            ToggleSwitch(enabled = enabled, onToggle = onToggle)
        }
    }
}

@Composable
private fun PillReminderBlock(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    CardContainer {
        Text("Pill Reminder", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Daily contraceptive reminder", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Enable Daily Reminder", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Get notified at the same time every day", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
            }
            ToggleSwitch(enabled = enabled, onToggle = onToggle)
        }
        if (enabled) {
            Spacer(Modifier.height(14.dp))
            Divider(color = BorderColor)
            Spacer(Modifier.height(14.dp))
            Text("Reminder Time", style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF2F2F5), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("09:00", style = MaterialTheme.typography.titleMedium)
                    Icon(Icons.Outlined.Insights, contentDescription = null, tint = Color(0xFF6D7482), modifier = Modifier.padding(start = 10.dp).size(14.dp))
                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkAction)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
