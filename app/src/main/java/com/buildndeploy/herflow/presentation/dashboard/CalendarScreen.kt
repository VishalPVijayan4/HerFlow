package com.buildndeploy.herflow.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
internal fun CalendarScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showCalendar by remember { mutableStateOf(false) }

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
            Text("Selected: ${selectedDate.format(DisplayDateFormatter)}", color = TextPrimary, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            FieldPill(selectedDate.format(DisplayDateFormatter), Icons.Outlined.CalendarMonth) { showCalendar = true }
        }
    }

    if (showCalendar) {
        AppCalendarDialog(
            initialDate = selectedDate,
            onDismiss = { showCalendar = false },
            onDateSelected = {
                selectedDate = it
                showCalendar = false
            }
        )
    }
}

@Composable
internal fun AppCalendarDialog(
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }
    val today = LocalDate.now()
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(currentMonth.format(monthFormatter), fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(Modifier.weight(1f))
                    SmallChip("‹")
                    SmallChip("›")
                }
                Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                        Text(it, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(8.dp))
                val firstDayOffset = (currentMonth.atDay(1).dayOfWeek.value % 7)
                val daysInMonth = currentMonth.lengthOfMonth()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(240.dp),
                    userScrollEnabled = false,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(firstDayOffset) { Box(modifier = Modifier.size(32.dp)) }
                    items((1..daysInMonth).toList()) { day ->
                        val date = currentMonth.atDay(day)
                        val isSelected = date == selectedDate
                        val isToday = date == today
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = if (isSelected) BrandPink.copy(alpha = 0.2f) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected || isToday) BrandPink else BorderColor,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    selectedDate = date
                                    onDateSelected(date)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(day.toString(), color = TextPrimary, fontSize = 11.sp)
                        }
                    }
                    item(span = { GridItemSpan(7) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { currentMonth = YearMonth.from(today); selectedDate = today; onDateSelected(today) },
                                modifier = Modifier.fillMaxWidth(0.5f),
                                colors = ButtonDefaults.buttonColors(containerColor = DarkAction)
                            ) {
                                Text("Today", color = Color.White, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
