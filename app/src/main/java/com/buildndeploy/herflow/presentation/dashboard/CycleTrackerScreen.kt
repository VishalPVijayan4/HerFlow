package com.buildndeploy.herflow.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate

@Composable
internal fun CycleTrackerScreen(onNewCycle: () -> Unit) {
    var showCycleDialog by remember { mutableStateOf(false) }
    val cycles = remember { mutableStateListOf<String>() }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HeaderWithAction(
            "Cycle Tracker",
            "Log and manage your menstrual cycles",
            "+ New Cycle"
        ) {
            onNewCycle()
            showCycleDialog = true
        }
        if (cycles.isEmpty()) {
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
        } else {
            CardContainer {
                Text("Tracked Cycles", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
                cycles.forEach {
                    Text("• $it", style = MaterialTheme.typography.bodyLarge, color = TextMuted, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
    if (showCycleDialog) {
        AddCycleDialog(
            onDismiss = { showCycleDialog = false },
            onAdd = { start, end ->
                cycles.add("$start - ${end.ifBlank { "Ongoing" }}")
                showCycleDialog = false
            }
        )
    }
}

@Composable
private fun AddCycleDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Log New Cycle", style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("✕") }
                }
                Text("Enter the start and end dates of your period", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(10.dp))
                Text("Start Date", fontWeight = FontWeight.Bold, color = TextPrimary)
                FieldPill(startDate?.format(DisplayDateFormatter) ?: "dd/mm/yyyy", Icons.Outlined.CalendarMonth) {
                    showStartPicker = true
                }
                Spacer(Modifier.height(8.dp))
                Text("End Date (optional)", fontWeight = FontWeight.Bold, color = TextPrimary)
                FieldPill(endDate?.format(DisplayDateFormatter) ?: "dd/mm/yyyy", Icons.Outlined.CalendarMonth) {
                    showEndPicker = true
                }
                Text("Leave empty if your period is ongoing", style = MaterialTheme.typography.bodySmall, color = TextMuted, modifier = Modifier.padding(top = 6.dp))
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            onAdd(
                                startDate?.format(DisplayDateFormatter).orEmpty(),
                                endDate?.format(DisplayDateFormatter).orEmpty()
                            )
                        },
                        modifier = Modifier.weight(1f),
                        enabled = startDate != null,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkAction)
                    ) {
                        Text("Add Cycle")
                    }
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F4F6), contentColor = TextPrimary)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }

    if (showStartPicker) {
        AppCalendarDialog(
            initialDate = startDate ?: LocalDate.now(),
            onDismiss = { showStartPicker = false },
            onDateSelected = {
                startDate = it
                showStartPicker = false
            }
        )
    }
    if (showEndPicker) {
        AppCalendarDialog(
            initialDate = endDate ?: (startDate ?: LocalDate.now()),
            onDismiss = { showEndPicker = false },
            onDateSelected = {
                endDate = it
                showEndPicker = false
            }
        )
    }
}
