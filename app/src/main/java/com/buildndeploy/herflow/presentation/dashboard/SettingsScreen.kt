package com.buildndeploy.herflow.presentation.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
internal fun AnalyticsScreen() {
    val symptoms = listOf(
        "bloating" to 5f,
        "cramps" to 4f,
        "fatigue" to 4f,
        "backache" to 3f,
        "headache" to 2f,
        "breast tenderness" to 2f
    )
    val moodDistribution = listOf("Happy" to 5f, "Calm" to 4f, "Sad" to 2f, "Anxious" to 2f)
    val bbtValues = listOf(36.2f, 36.3f, 36.1f, 36.2f, 36.3f, 36.2f, 36.3f, 36.4f, 36.5f, 36.7f, 36.8f, 36.7f, 36.8f, 36.9f, 36.7f, 36.8f, 36.7f, 36.8f, 36.7f, 36.8f, 36.7f, 36.6f, 36.7f, 36.6f)
    val energyValues = listOf(1f, 1f, 2f, 3f, 3f, 1f, 1f)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Analytics", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Track your patterns and trends", color = TextMuted, style = MaterialTheme.typography.bodyLarge)

        ChartCard("Cycle Length Trend", "Last 1 cycles") {
            SingleBar(value = 5f, label = "Cycle 1", color = BrandPink)
        }
        ChartCard("Period Duration", "How many days each period lasted") {
            SingleBar(value = 5f, label = "Period 1", color = BrandPurple)
        }
        ChartCard("Most Common Symptoms", "Tracked across all logged days") {
            HorizontalBars(symptoms)
        }
        ChartCard("Mood Distribution", "Your emotional patterns") {
            MultiBars(moodDistribution)
        }
        ChartCard("Basal Body Temperature Curve", "Temperature pattern over time") {
            SimpleLineChart(values = bbtValues, minY = 35.5f, maxY = 37.5f, lineColor = Color(0xFFEF4444))
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE7F0FB), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    "Look for a temperature dip followed by a sustained rise — this confirms ovulation. Normal pre-ovulation: 36.1-36.4°C, Post-ovulation: 36.4-37.0°C",
                    color = Color(0xFF1D4ED8),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        ChartCard("Energy Levels (Last 2 Weeks)", "Track your energy patterns") {
            SimpleLineChart(values = energyValues, minY = 0f, maxY = 4f, lineColor = Color(0xFF10B981))
        }
    }
}

@Composable
internal fun PartnerModeScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(BrandPink, BrandPurple)), RoundedCornerShape(12.dp))
                .padding(vertical = 28.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.FavoriteBorder, null, tint = Color.White, modifier = Modifier.size(44.dp))
                Text("Partner Support Guide", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Understanding her cycle to be more supportive", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            }
        }

        CardContainer {
            Text("Current Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(14.dp))
            Text("Current Phase", color = TextMuted)
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(Color(0xFFF2F3F7), RoundedCornerShape(8.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text("unknown", fontWeight = FontWeight.Bold, color = TextMuted)
            }
        }

        CardContainer {
            Text("How to Be Supportive", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Text("Understanding her cycle helps you be a more supportive partner.", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        }

        CardContainer {
            Text("General Tips for Partners", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            listOf(
                "Communicate openly" to "Ask how she's feeling and what she needs",
                "Be patient and understanding" to "Hormonal changes can affect mood and energy",
                "Help with practical things" to "Stock up on supplies, prepare meals, handle chores",
                "Respect boundaries" to "Everyone's experience is different - listen to her needs"
            ).forEach { (title, subtitle) ->
                Row(modifier = Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                    Text("♡", color = BrandPink, modifier = Modifier.padding(top = 2.dp))
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(subtitle, style = MaterialTheme.typography.bodyLarge, color = TextMuted)
                    }
                }
            }
        }

        CardContainer {
            OutlinedAction("Download Summary", Icons.Outlined.Insights)
            Spacer(Modifier.height(10.dp))
            OutlinedAction("Understanding the Menstrual Cycle", Icons.Outlined.Insights)
        }

        Text(
            "This guide is for educational purposes to help partners be more supportive.\nAlways respect privacy and communicate openly.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextMuted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChartCard(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    CardContainer {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Text(subtitle, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(14.dp))
        Column { content() }
    }
}

@Composable
private fun SingleBar(value: Float, label: String, color: Color) {
    val normalized = (value / 8f).coerceIn(0f, 1f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(172.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                .padding(horizontal = 28.dp, vertical = 10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.30f)
                    .height((108f * normalized).dp.coerceAtLeast(16.dp))
                    .background(color, RoundedCornerShape(8.dp))
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(label, modifier = Modifier.align(Alignment.CenterHorizontally), color = TextMuted)
    }
}

@Composable
private fun HorizontalBars(items: List<Pair<String, Float>>) {
    val max = items.maxOfOrNull { it.second } ?: 1f
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { (label, value) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, modifier = Modifier.width(112.dp), color = TextMuted)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(24.dp)
                        .background(Color(0xFFEFF3FA), RoundedCornerShape(8.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((value / max).coerceIn(0f, 1f))
                            .height(24.dp)
                            .background(Color(0xFF3D7BE0), RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiBars(items: List<Pair<String, Float>>) {
    val max = items.maxOfOrNull { it.second } ?: 1f
    val colors = listOf(BrandPink, BrandPurple, Color(0xFF3B82F6), Color(0xFF10B981), Color(0xFFF59E0B), Color(0xFFEF4444), Color(0xFF8B5CF6))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        items.forEachIndexed { index, (label, value) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((104f * (value / max)).dp.coerceAtLeast(10.dp))
                        .background(colors[index % colors.size], RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(6.dp))
                Text(label, color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
        }

        CardContainer {
            OutlinedAction("Download Summary", Icons.Outlined.Insights)
            Spacer(Modifier.height(10.dp))
            OutlinedAction("Understanding the Menstrual Cycle", Icons.Outlined.Insights)
        }

        Text(
            "This guide is for educational purposes to help partners be more supportive.\nAlways respect privacy and communicate openly.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextMuted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChartCard(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    CardContainer {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Text(subtitle, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(12.dp))
        Column { content() }
    }
}

@Composable
private fun SingleBar(value: Float, label: String, color: Color) {
    val normalized = (value / 8f).coerceIn(0f, 1f)
    Column(modifier = Modifier.fillMaxWidth().height(220.dp), verticalArrangement = Arrangement.Bottom) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((160f * normalized).dp.coerceAtLeast(8.dp))
                .background(color, RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.height(8.dp))
        Text(label, modifier = Modifier.align(Alignment.CenterHorizontally), color = TextMuted)
    }
}

@Composable
private fun HorizontalBars(items: List<Pair<String, Float>>) {
    val max = items.maxOfOrNull { it.second } ?: 1f
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        items.forEach { (label, value) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, modifier = Modifier.width(112.dp), color = TextMuted)
                Box(modifier = Modifier.weight(1f).height(30.dp).background(Color(0xFFE8EEF8), RoundedCornerShape(8.dp))) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((value / max).coerceIn(0f, 1f))
                            .height(30.dp)
                            .background(Color(0xFF3D7BE0), RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun MultiBars(items: List<Pair<String, Float>>) {
    val max = items.maxOfOrNull { it.second } ?: 1f
    val colors = listOf(BrandPink, BrandPurple, Color(0xFF3B82F6), Color(0xFF10B981), Color(0xFFF59E0B), Color(0xFFEF4444), Color(0xFF8B5CF6))
    Row(modifier = Modifier.fillMaxWidth().height(220.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
        items.forEachIndexed { index, (label, value) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((140f * (value / max)).dp.coerceAtLeast(12.dp))
                        .background(colors[index % colors.size], RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(6.dp))
                Text(label, color = TextMuted, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun SimpleLineChart(values: List<Float>, minY: Float, maxY: Float, lineColor: Color) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        if (values.size < 2) return@Canvas
        val stepX = size.width / (values.size - 1)
        fun mapY(v: Float): Float = size.height - (((v - minY) / (maxY - minY)).coerceIn(0f, 1f) * size.height)

        val path = Path().apply {
            moveTo(0f, mapY(values.first()))
            values.drop(1).forEachIndexed { index, v ->
                lineTo((index + 1) * stepX, mapY(v))
            }
        }
        drawPath(path = path, color = lineColor, style = Stroke(width = 4f))
        values.forEachIndexed { index, v ->
            drawCircle(lineColor, radius = 5f, center = Offset(index * stepX, mapY(v)))
        }
    }
}

@Composable
private fun SimpleLineChart(values: List<Float>, minY: Float, maxY: Float, lineColor: Color) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(188.dp)
                .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (values.size < 2) return@Canvas
                val stepX = size.width / (values.size - 1)
                fun mapY(v: Float): Float = size.height - (((v - minY) / (maxY - minY)).coerceIn(0f, 1f) * size.height)

                val horizontalLines = 4
                repeat(horizontalLines + 1) { index ->
                    val y = (size.height / horizontalLines) * index
                    drawLine(
                        color = Color(0xFFD9E1F1),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.2f
                    )
                }

                val path = Path().apply {
                    moveTo(0f, mapY(values.first()))
                    values.drop(1).forEachIndexed { index, v ->
                        lineTo((index + 1) * stepX, mapY(v))
                    }
                }
                drawPath(path = path, color = lineColor, style = Stroke(width = 3.4f))
                values.forEachIndexed { index, v ->
                    drawCircle(color = lineColor, radius = 4.6f, center = Offset(index * stepX, mapY(v)))
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(String.format("%.1f", minY), color = TextMuted, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.weight(1f))
            Text(String.format("%.1f", maxY), color = TextMuted, style = MaterialTheme.typography.bodySmall)
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
                    Text("Partner Mode", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
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
                    Text("Data Management", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                    Text("Export, import, or clear your data", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(14.dp))
                    OutlinedAction("Export Data (JSON)", Icons.Outlined.Insights)
                    Spacer(Modifier.height(10.dp))
                    OutlinedAction("Import Data", Icons.Outlined.Insights)
                    Spacer(Modifier.height(10.dp))
                    OutlinedAction("Clear All Data", Icons.Outlined.Close, Color.Red)
                }
                CardContainer {
                    Text("About HerFlow", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
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
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
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
    var reminderTime by remember { mutableStateOf("09:00") }
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
                        .clickable {
                            reminderTime = if (reminderTime == "09:00") "08:00" else "09:00"
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(reminderTime, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Icon(Icons.Outlined.Insights, contentDescription = null, tint = Color(0xFF6D7482), modifier = Modifier.padding(start = 10.dp).size(14.dp))
                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkAction)
                ) {
                    Text("Save", color = Color.White)
                }
            }
        }
    }
}
