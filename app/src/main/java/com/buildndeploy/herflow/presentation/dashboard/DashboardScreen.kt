package com.buildndeploy.herflow.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private val AppBackground = Color(0xFFF2EEF8)
private val CardBackground = Color(0xFFFCFCFD)
private val BorderColor = Color(0xFFE4DCEB)
private val BrandPink = Color(0xFFF6249B)
private val BrandPurple = Color(0xFF9D3DF2)
private val TextPrimary = Color(0xFF0E1525)
private val TextMuted = Color(0xFF4D5A72)
private val DarkAction = Color(0xFF05072D)

private enum class AppSection(val title: String, val icon: ImageVector) {
    Home("Home", Icons.Outlined.Home),
    CycleTracker("Cycle Tracker", Icons.Outlined.FavoriteBorder),
    DailyLog("Daily Log", Icons.Outlined.MonitorHeart),
    Calendar("Calendar", Icons.Outlined.CalendarMonth),
    Analytics("Analytics", Icons.Outlined.Analytics),
    PartnerMode("Partner Mode", Icons.Outlined.People),
    Settings("Settings", Icons.Outlined.Settings)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardRoute(
    state: DashboardState = DashboardState(
        isLoading = false,
        entries = emptyList(),
        featureCards = emptyList()
    ),
    onIntent: (DashboardIntent) -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    var selectedSection by remember { mutableStateOf(AppSection.Home) }
    var showDrawer by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            AppTopBar(
                showClose = showDrawer,
                onMenuClick = { showDrawer = !showDrawer }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(top = padding.calculateTopPadding())
        ) {
            ScreenContent(
                selectedSection = selectedSection,
                onNewCycle = onRefresh,
                onSave = { onIntent(DashboardIntent.Refresh) }
            )

            if (showDrawer) {
                Row(Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color.Black.copy(alpha = 0.20f))
                            .clickable { showDrawer = false }
                    )
                    DrawerPanel(
                        selectedSection = selectedSection,
                        onSelected = {
                            selectedSection = it
                            showDrawer = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppTopBar(showClose: Boolean, onMenuClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(72.dp)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(brush = Brush.linearGradient(listOf(BrandPink, BrandPurple)), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, tint = Color.White)
            }
            Text(
                text = "HerFlow",
                color = BrandPurple,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = if (showClose) Icons.Outlined.Close else Icons.Outlined.Menu,
                    contentDescription = "Menu",
                    tint = BrandPink
                )
            }
        }
    }
}

@Composable
private fun DrawerPanel(
    selectedSection: AppSection,
    onSelected: (AppSection) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.52f)
            .widthIn(min = 220.dp, max = 300.dp)
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(2.dp))
        AppSection.entries.forEach { section ->
            val selected = section == selectedSection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (selected) Brush.linearGradient(listOf(BrandPink, BrandPurple)) else Brush.linearGradient(
                            listOf(Color.Transparent, Color.Transparent)
                        ),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelected(section) }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = section.icon,
                    contentDescription = section.title,
                    tint = if (selected) Color.White else Color(0xFF364152)
                )
                Text(
                    text = section.title,
                    modifier = Modifier.padding(start = 12.dp),
                    color = if (selected) Color.White else Color(0xFF364152),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ScreenContent(
    selectedSection: AppSection,
    onNewCycle: () -> Unit,
    onSave: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val horizontalPadding = if (maxWidth > 700.dp) 36.dp else 12.dp
        val contentWidth = if (maxWidth > 900.dp) 860.dp else Dp.Unspecified

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = 14.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            val childModifier = if (contentWidth == Dp.Unspecified) Modifier.fillMaxWidth() else Modifier.width(contentWidth)
            Column(modifier = childModifier) {
                when (selectedSection) {
                    AppSection.Home -> HomeScreen()
                    AppSection.CycleTracker -> CycleTrackerScreen(onNewCycle)
                    AppSection.DailyLog -> DailyLogScreen(onSave)
                    AppSection.Calendar -> CalendarScreen()
                    AppSection.Analytics -> AnalyticsScreen()
                    AppSection.PartnerMode -> PartnerModeScreen()
                    AppSection.Settings -> SettingsScreen()
                }
            }
        }
    }
}

@Composable
private fun HomeScreen() {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item { Spacer(Modifier.height(4.dp)) }
        item {
            CardContainer {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, null, tint = BrandPink, modifier = Modifier.size(64.dp))
                    Text(
                        text = "Welcome to HerFlow!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB628B3)
                    )
                    Text(
                        text = "Start tracking your cycle to get personalized insights,\npredictions, and understand your body better.",
                        color = TextMuted,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .background(
                                brush = Brush.horizontalGradient(listOf(BrandPink, BrandPurple)),
                                shape = RoundedCornerShape(999.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.FavoriteBorder, null, tint = Color.White)
                            Text(
                                "Log Your First Period",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
        item { FeatureTile(Icons.Outlined.MonitorHeart, "Track Daily", "Log symptoms, moods, and fertility signs", "Click to start", BrandPink) }
        item { FeatureTile(Icons.Outlined.CalendarMonth, "View Calendar", "Visualize your cycle and patterns", "Coming soon", BrandPurple) }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun CycleTrackerScreen(onNewCycle: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HeaderWithAction("Cycle Tracker", "Log and manage your menstrual cycles", "New Cycle", onNewCycle)
        CardContainer {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.CalendarMonth, null, tint = Color(0xFFC9CDD6), modifier = Modifier.size(52.dp))
                Text("No cycles tracked yet", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    "Start by logging your first period to get personalized insights",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
private fun DailyLogScreen(onSave: () -> Unit) {
    val sections = listOf(
        "Physical Symptoms" to listOf("Cramps", "Headache", "Bloating", "Breast Tenderness", "Backache", "Nausea", "Fatigue"),
        "Skin" to listOf("Acne", "Oily Skin", "Dry Skin"),
        "Digestion" to listOf("Constipation", "Diarrhea", "Appetite Changes")
    )

    Column {
        Text("Daily Log", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Track your symptoms, mood, and fertility signs", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(14.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardContainer {
                Text("Select Date", fontWeight = FontWeight.Medium)
                FieldPill("27/03/2026", Icons.Outlined.CalendarMonth)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8E8EC), RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Symptoms", "Mood", "Mucus", "BBT").forEachIndexed { idx, label ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (idx == 0) Color.White else Color.Transparent, RoundedCornerShape(14.dp))
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            sections.forEach { (title, items) ->
                CardContainer {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))
                    items.forEach { label ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .border(1.dp, Color(0xFFD1D5DC), RoundedCornerShape(4.dp))
                            )
                            Text(label, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            CardContainer {
                Text("Energy & Sleep", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
                Text("Energy Level", style = MaterialTheme.typography.bodyLarge)
                FieldPill("Normal", Icons.Outlined.Insights)
                Spacer(Modifier.height(8.dp))
                Text("Sleep Quality", style = MaterialTheme.typography.bodyLarge)
                FieldPill("Okay", Icons.Outlined.Insights)
            }

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkAction),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Save Symptoms", color = Color.White, modifier = Modifier.padding(vertical = 4.dp))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CalendarScreen() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Calendar", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Visual overview of your cycle", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        CardContainer {
            val labels = listOf(
                "Period" to Color(0xFFFF3B45),
                "Fertile Window" to Color(0xFF1CC658),
                "Ovulation" to Color(0xFF9A4DFF),
                "PMS Window" to Color(0xFFE3A700),
                "Data Logged" to Color(0xFF3B82F6)
            )
            labels.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 12.dp)) {
                    row.forEach { (label, color) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(16.dp).background(color, RoundedCornerShape(4.dp)))
                            Text(label, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
        CardContainer {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("March 2026", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(Modifier.weight(1f))
                SmallChip("‹")
                SmallChip("Today")
                SmallChip("›")
            }
            Spacer(Modifier.height(12.dp))
            val headers = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                headers.forEach { Text(it, color = TextMuted, fontWeight = FontWeight.Medium) }
            }
            Spacer(Modifier.height(12.dp))
            val days = (1..31).map { it.toString() } + listOf("1", "2", "3", "4")
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(300.dp),
                userScrollEnabled = false,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(days) { day ->
                    val muted = day.toIntOrNull()?.let { it < 5 && days.indexOf(day) > 30 } == true
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .border(
                                1.dp,
                                if (day == "27") BrandPink else BorderColor,
                                RoundedCornerShape(12.dp)
                            )
                            .alpha(if (muted) 0.4f else 1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day, color = if (muted) Color(0xFFB7BCC6) else Color(0xFF2D3B55))
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsScreen() {
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
private fun PartnerModeScreen() {
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
private fun SettingsScreen() {
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
private fun HeaderWithAction(title: String, subtitle: String, action: String, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(subtitle, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPink)
        ) {
            Icon(Icons.Rounded.Add, null)
            Text(action, modifier = Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
private fun CardContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
            content()
        }
    }
}

@Composable
private fun FeatureTile(icon: ImageVector, title: String, description: String, badge: String, badgeColor: Color) {
    CardContainer {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = BrandPink)
            Spacer(Modifier.weight(1f))
            Text(badge, color = badgeColor)
        }
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp), color = TextPrimary)
        Text(description, style = MaterialTheme.typography.bodyLarge, color = TextMuted)
    }
}

@Composable
private fun FieldPill(text: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .background(Color(0xFFF2F2F5), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.weight(1f))
        Icon(icon, null, tint = Color(0xFF6D7482), modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun SmallChip(label: String) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontWeight = FontWeight.Medium)
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
                Text(switchLabel, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium, color = TextPrimary)
                Text(
                    switchSubtitle,
                    color = TextMuted,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            ToggleSwitch(enabled = enabled, onToggle = onToggle)
        }
    }
}

@Composable
private fun OutlinedAction(label: String, icon: ImageVector, tint: Color = Color(0xFF111927)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(16.dp))
        Text(label, modifier = Modifier.padding(start = 12.dp), color = tint, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PillReminderBlock(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    CardContainer {
        Text("Pill Reminder", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Text("Daily contraceptive reminder", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Enable Daily Reminder", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium, color = TextPrimary)
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

@Composable
private fun ToggleSwitch(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .width(34.dp)
            .height(22.dp)
            .background(if (enabled) DarkAction else Color(0xFFD0D4DC), CircleShape)
            .clickable { onToggle(!enabled) }
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .align(if (enabled) Alignment.CenterEnd else Alignment.CenterStart)
                .size(18.dp)
                .background(Color.White, CircleShape)
        )
    }
}

@Composable
private fun ToggleToast(modifier: Modifier = Modifier, message: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F9)),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E3E7))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(18.dp).background(Color(0xFF111217), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = message,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}
