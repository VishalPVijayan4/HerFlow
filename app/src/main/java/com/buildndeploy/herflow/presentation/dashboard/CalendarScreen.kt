package com.buildndeploy.herflow.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun CalendarScreen(
    hasSymptoms: (LocalDate) -> Boolean = { false },
    hasMood: (LocalDate) -> Boolean = { false },
    hasMucus: (LocalDate) -> Boolean = { false },
    hasBbt: (LocalDate) -> Boolean = { false },
    onAddEntry: (LocalDate) -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    val today = LocalDate.now()
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val firstDayOffset = (currentMonth.atDay(1).dayOfWeek.value % 7)
    val daysInMonth = currentMonth.lengthOfMonth()
    val totalCells = ((firstDayOffset + daysInMonth + 6) / 7) * 7
    val firstVisibleDate = currentMonth.atDay(1).minusDays(firstDayOffset.toLong())

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                Text(currentMonth.format(monthFormatter), fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.weight(1f))
                OutlinedButton(
                    onClick = { currentMonth = currentMonth.minusMonths(1) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) { Text("‹") }
                Spacer(Modifier.size(8.dp))
                OutlinedButton(
                    onClick = {
                        currentMonth = YearMonth.from(today)
                        selectedDate = today
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) { Text("Today", fontWeight = FontWeight.SemiBold) }
                Spacer(Modifier.size(8.dp))
                OutlinedButton(
                    onClick = { currentMonth = currentMonth.plusMonths(1) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) { Text("›") }
            }
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                    Text(it, color = TextMuted, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(292.dp),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(totalCells) { index ->
                    val date = firstVisibleDate.plusDays(index.toLong())
                    val isCurrentMonth = date.month == currentMonth.month && date.year == currentMonth.year
                    val isSelected = date == selectedDate
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White, RoundedCornerShape(10.dp))
                            .border(1.4.dp, if (isSelected) BrandPink else BorderColor, RoundedCornerShape(10.dp))
                            .clickable {
                                selectedDate = date
                                currentMonth = YearMonth.from(date)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (isCurrentMonth) TextPrimary else Color(0xFFCBD1DA),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        CardContainer {
            Text("Predictions for ${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                Text("Ovulation", color = TextMuted)
                Spacer(Modifier.weight(1f))
                Text(currentMonth.atDay(16).format(DateTimeFormatter.ofPattern("MMM d")), fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth()) {
                Text("Fertile Window", color = TextMuted)
                Spacer(Modifier.weight(1f))
                Text("${currentMonth.atDay(12).format(DateTimeFormatter.ofPattern("MMM d"))} - ${currentMonth.atDay(18).format(DateTimeFormatter.ofPattern("MMM d"))}", fontWeight = FontWeight.SemiBold)
            }
        }

        CardContainer {
            Text(selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM d")), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text("Logs for this day", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf(
                    "Symptoms" to hasSymptoms(selectedDate),
                    "Mood" to hasMood(selectedDate),
                    "Mucus" to hasMucus(selectedDate),
                    "BBT" to hasBbt(selectedDate)
                ).forEach { (label, done) ->
                    Box(
                        modifier = Modifier
                            .background(if (done) Color(0xFFE8F9ED) else Color(0xFFF2F3F7), RoundedCornerShape(999.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(if (done) "$label ✓" else label, color = if (done) Color(0xFF15803D) else TextMuted, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { onAddEntry(selectedDate) },
                colors = ButtonDefaults.buttonColors(containerColor = DarkAction),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add entry", color = Color.White)
            }
        }
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
