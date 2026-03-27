package com.buildndeploy.herflow.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.buildndeploy.herflow.domain.model.CycleEntry
import com.buildndeploy.herflow.domain.model.FeatureCard
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardRoute(
    state: DashboardState = DashboardState(
        isLoading = false,
        entries = previewEntries,
        featureCards = previewCards
    ),
    onIntent: (DashboardIntent) -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        state.error?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PeriodPal") },
                actions = {
                    IconButton(onClick = {
                        onIntent(DashboardIntent.Refresh)
                        onRefresh()
                    }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            DashboardContent(
                modifier = Modifier.padding(padding),
                state = state
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DashboardContent(
    modifier: Modifier = Modifier,
    state: DashboardState
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(Modifier.height(8.dp)) }
        item {
            HeroCard(
                cycleLength = state.cycleLengthDays,
                nextPeriod = state.nextPeriodDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
            )
        }
        item {
            SectionHeader("Feature Overview")
        }
        items(state.featureCards) { card ->
            FeatureInfoCard(card = card)
        }
        item {
            SectionHeader("Recent Log")
        }
        items(state.entries) { entry ->
            LogCard(entry = entry)
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun HeroCard(cycleLength: Int, nextPeriod: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Favorite, contentDescription = null)
                Text(
                    text = "Your next period is expected on",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = nextPeriod,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Based on your $cycleLength-day cycle",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun FeatureInfoCard(card: FeatureCard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(card.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(card.subtitle, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Text(card.detail, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun LogCard(entry: CycleEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "${entry.startDate} - ${entry.endDate}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Flow ${entry.flowLevel}/5 • Pain ${entry.painLevel}/5 • Mood: ${entry.mood}",
                style = MaterialTheme.typography.bodySmall
            )
            entry.notes?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

private val previewCards = listOf(
    FeatureCard("Smart Predictions", "Cycle + ovulation windows", "Forecasts adapt as you track."),
    FeatureCard("Symptom Journal", "Mood, pain, flow", "Log daily changes in one place."),
    FeatureCard("Insights Hub", "Patterns that matter", "Get suggestions tailored to your trend.")
)

@RequiresApi(Build.VERSION_CODES.O)
private val previewEntries = listOf(
    CycleEntry(1, java.time.LocalDate.now().minusDays(28), java.time.LocalDate.now().minusDays(23), 4, 3, "Low energy", "Sleep early for better recovery")
)
