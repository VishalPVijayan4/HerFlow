package com.buildndeploy.herflow.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MonitorHeart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import java.time.LocalDate

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
    var selectedLogDate by remember { mutableStateOf(LocalDate.now()) }
    val cycles = remember { mutableStateListOf(CycleRecord(LocalDate.of(2026, 2, 25), LocalDate.of(2026, 3, 1))) }
    val symptomsByDate = remember { mutableStateMapOf<LocalDate, SymptomsLogState>() }
    val moodsByDate = remember { mutableStateMapOf<LocalDate, MoodLogState>() }
    val mucusByDate = remember { mutableStateMapOf<LocalDate, MucusLogState>() }
    val bbtByDate = remember { mutableStateMapOf<LocalDate, BbtLogState>() }

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
                onSave = { onIntent(DashboardIntent.Refresh) },
                onSectionChange = { selectedSection = it },
                cycles = cycles,
                symptomsByDate = symptomsByDate,
                moodsByDate = moodsByDate,
                mucusByDate = mucusByDate,
                bbtByDate = bbtByDate,
                selectedLogDate = selectedLogDate,
                onSelectedLogDateChange = { selectedLogDate = it },
                onNavigateToSection = { selectedSection = it }
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
    onSave: () -> Unit,
    onSectionChange: (AppSection) -> Unit,
    cycles: androidx.compose.runtime.snapshots.SnapshotStateList<CycleRecord>,
    symptomsByDate: androidx.compose.runtime.snapshots.SnapshotStateMap<LocalDate, SymptomsLogState>,
    moodsByDate: androidx.compose.runtime.snapshots.SnapshotStateMap<LocalDate, MoodLogState>,
    mucusByDate: androidx.compose.runtime.snapshots.SnapshotStateMap<LocalDate, MucusLogState>,
    bbtByDate: androidx.compose.runtime.snapshots.SnapshotStateMap<LocalDate, BbtLogState>,
    selectedLogDate: LocalDate,
    onSelectedLogDateChange: (LocalDate) -> Unit,
    onNavigateToSection: (AppSection) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 860.dp)
        ) {
            when (selectedSection) {
                AppSection.Home -> HomeScreen(
                    onSectionChange = onSectionChange,
                    hasSymptoms = symptomsByDate[LocalDate.now()] != null,
                    hasMood = moodsByDate[LocalDate.now()] != null,
                    hasMucus = mucusByDate[LocalDate.now()] != null,
                    hasBbt = bbtByDate[LocalDate.now()] != null,
                    cyclesCount = cycles.size,
                    symptomsCount = symptomsByDate.size,
                    mucusCount = mucusByDate.size,
                    bbtCount = bbtByDate.size
                )
                AppSection.CycleTracker -> CycleTrackerScreen(onNewCycle, cycles)
                AppSection.DailyLog -> DailyLogScreen(onSave, selectedLogDate, symptomsByDate, moodsByDate, mucusByDate, bbtByDate)
                AppSection.Calendar -> CalendarScreen(
                    hasSymptoms = { symptomsByDate[it] != null },
                    hasMood = { moodsByDate[it] != null },
                    hasMucus = { mucusByDate[it] != null },
                    hasBbt = { bbtByDate[it] != null },
                    onAddEntry = { date ->
                        onSelectedLogDateChange(date)
                        onNavigateToSection(AppSection.DailyLog)
                    }
                )
                AppSection.Analytics -> AnalyticsScreen()
                AppSection.PartnerMode -> PartnerModeScreen()
                AppSection.Settings -> SettingsScreen()
            }
        }
    }
}

@Composable
private fun HomeScreen(
    onSectionChange: (AppSection) -> Unit,
    hasSymptoms: Boolean,
    hasMood: Boolean,
    hasMucus: Boolean,
    hasBbt: Boolean,
    cyclesCount: Int,
    symptomsCount: Int,
    mucusCount: Int,
    bbtCount: Int
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                HomeQuickActionTile("Log Today", BrandPink, Icons.Outlined.Edit, onClick = { onSectionChange(AppSection.DailyLog) }, modifier = Modifier.weight(1f))
                HomeQuickActionTile("Track Cycle", BrandPurple, Icons.Outlined.FavoriteBorder, onClick = { onSectionChange(AppSection.CycleTracker) }, modifier = Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                HomeQuickActionTile("View Stats", Color(0xFF2563EB), Icons.Outlined.ShowChart, onClick = { onSectionChange(AppSection.Analytics) }, modifier = Modifier.weight(1f))
                HomeQuickActionTile("Calendar", Color(0xFF4F46E5), Icons.Outlined.CalendarMonth, onClick = { onSectionChange(AppSection.Calendar) }, modifier = Modifier.weight(1f))
            }
        }
        item {
            CardContainer {
                Text("Today's Logs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = Color(0xFF16A34A))
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(
                        "Symptoms" to hasSymptoms,
                        "Mood" to hasMood,
                        "Mucus" to hasMucus,
                        "BBT" to hasBbt
                    ).forEach { (section, saved) ->
                        val label = if (saved) "$section ✓" else "$section"
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE9F9EF), RoundedCornerShape(999.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(label, color = Color(0xFF15803D), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Text("  Log your cycle data to get personalized insights.", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        item {
            HomePhaseSummaryCard(phaseLabel = "Unknown", cycleDay = 31)
        }
        item {
            CardContainer {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.WaterDrop, null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                    Text("Fertility Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 6.dp))
                }
                Spacer(Modifier.height(12.dp))
                Text("Low", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text("Fertility Score", color = TextMuted)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                    Text("Ovulation", color = TextMuted)
                    Spacer(Modifier.weight(1f))
                    Text("Feb 16", fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth()) {
                    Text("Fertile Window", color = TextMuted)
                    Spacer(Modifier.weight(1f))
                    Text("Feb 12 - Feb 18", fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFE7F0FB), RoundedCornerShape(10.dp)).padding(12.dp)) {
                    Text("These predictions are estimates based on your cycle history. Not a substitute for medical advice.", color = Color(0xFF1D4ED8), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        item {
            CardContainer {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.TrendingUp, null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                    Text("Today's Insight", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 6.dp))
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.background(Color(0xFFF2F3F7), RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("unknown", color = TextMuted, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Text("  Log your cycle data to get personalized insights.", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                HomeStatCard(cyclesCount.toString(), "Cycles Tracked", Icons.Outlined.CalendarMonth, BrandPink, modifier = Modifier.weight(1f))
                HomeStatCard(symptomsCount.toString(), "Symptom Logs", Icons.Outlined.FavoriteBorder, BrandPurple, modifier = Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                HomeStatCard(mucusCount.toString(), "Mucus Logs", Icons.Outlined.WaterDrop, Color(0xFF3B82F6), modifier = Modifier.weight(1f))
                HomeStatCard(bbtCount.toString(), "BBT Logs", Icons.Outlined.TrendingUp, Color(0xFF10B981), modifier = Modifier.weight(1f))
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

@Composable
private fun HomePhaseSummaryCard(phaseLabel: String, cycleDay: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFFD946EF), BrandPurple)),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.AutoAwesome,
                        null,
                        tint = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        "Current Phase",
                        color = Color.White.copy(alpha = 0.90f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                Text(
                    text = phaseLabel,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Cycle Day",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    cycleDay.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HomeQuickActionTile(title: String, color: Color, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Brush.linearGradient(listOf(color, color.copy(alpha = 0.75f))), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun HomeStatCard(value: String, label: String, icon: ImageVector, iconTint: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CardContainer {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Outlined.TrendingUp, null, tint = Color(0xFF22C55E), modifier = Modifier.size(16.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        }
        item {
            HomePhaseSummaryCard(phaseLabel = "Unknown", cycleDay = 31)
        }
        item {
            CardContainer {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.WaterDrop, null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                    Text("Fertility Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 6.dp))
                }
                Spacer(Modifier.height(12.dp))
                Text("Low", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text("Fertility Score", color = TextMuted)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth()) {
                    Text("Ovulation", color = TextMuted)
                    Spacer(Modifier.weight(1f))
                    Text("Feb 16", fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth()) {
                    Text("Fertile Window", color = TextMuted)
                    Spacer(Modifier.weight(1f))
                    Text("Feb 12 - Feb 18", fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFE7F0FB), RoundedCornerShape(10.dp)).padding(12.dp)) {
                    Text("These predictions are estimates based on your cycle history. Not a substitute for medical advice.", color = Color(0xFF1D4ED8), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        item {
            CardContainer {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.TrendingUp, null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                    Text("Today's Insight", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 6.dp))
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.background(Color(0xFFF2F3F7), RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("unknown", color = TextMuted, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Text("  Log your cycle data to get personalized insights.", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                HomeStatCard(cyclesCount.toString(), "Cycles Tracked", Icons.Outlined.CalendarMonth, BrandPink, modifier = Modifier.weight(1f))
                HomeStatCard(symptomsCount.toString(), "Symptom Logs", Icons.Outlined.FavoriteBorder, BrandPurple, modifier = Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                HomeStatCard(mucusCount.toString(), "Mucus Logs", Icons.Outlined.WaterDrop, Color(0xFF3B82F6), modifier = Modifier.weight(1f))
                HomeStatCard(bbtCount.toString(), "BBT Logs", Icons.Outlined.TrendingUp, Color(0xFF10B981), modifier = Modifier.weight(1f))
            }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

@Composable
private fun HomePhaseSummaryCard(phaseLabel: String, cycleDay: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFFD946EF), BrandPurple)),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.AutoAwesome,
                        null,
                        tint = Color.White.copy(alpha = 0.92f),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        "Current Phase",
                        color = Color.White.copy(alpha = 0.90f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                Text(
                    text = phaseLabel,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Cycle Day",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    cycleDay.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HomeQuickActionTile(title: String, color: Color, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Brush.linearGradient(listOf(color, color.copy(alpha = 0.75f))), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun HomeStatCard(value: String, label: String, icon: ImageVector, iconTint: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CardContainer {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Outlined.TrendingUp, null, tint = Color(0xFF22C55E), modifier = Modifier.size(16.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        }
    }
    }
}

@Composable
private fun StatCard(value: String, label: String, icon: ImageVector, iconTint: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CardContainer {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Outlined.TrendingUp, null, tint = Color(0xFF22C55E), modifier = Modifier.size(16.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        }
    }
    }
}

@Composable
private fun QuickActionTile(title: String, color: Color, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(Brush.linearGradient(listOf(color, color.copy(alpha = 0.75f))), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
            Text(title, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, icon: ImageVector, iconTint: Color, modifier: Modifier = Modifier) {
    CardContainer {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(22.dp))
                Spacer(Modifier.weight(1f))
                Icon(Icons.Outlined.TrendingUp, null, tint = Color(0xFF22C55E), modifier = Modifier.size(16.dp))
            }
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, color = TextMuted, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

