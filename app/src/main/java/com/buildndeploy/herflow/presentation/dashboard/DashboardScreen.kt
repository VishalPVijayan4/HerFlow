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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
                onSave = { onIntent(DashboardIntent.Refresh) },
                onSectionChange = { selectedSection = it }
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
    onSectionChange: (AppSection) -> Unit
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
                AppSection.Home -> HomeScreen(onSectionChange)
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

@Composable
private fun HomeScreen(onSectionChange: (AppSection) -> Unit) {
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
                        onClick = { onSectionChange(AppSection.CycleTracker) },
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
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
        item { FeatureTile(Icons.Outlined.MonitorHeart, "Track Daily", "Log symptoms, moods, and fertility signs", "Open Daily Log", BrandPink) { onSectionChange(AppSection.DailyLog) } }
        item { FeatureTile(Icons.Outlined.CalendarMonth, "View Calendar", "Visualize your cycle and patterns", "Open Calendar", BrandPurple) { onSectionChange(AppSection.Calendar) } }
        item { Spacer(Modifier.height(12.dp)) }
    }
}
