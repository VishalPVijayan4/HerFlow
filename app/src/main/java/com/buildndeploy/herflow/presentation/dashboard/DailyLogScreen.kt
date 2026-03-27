package com.buildndeploy.herflow.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class SymptomsLog(val items: Set<String>, val notes: String)
private data class MoodLog(val moods: Set<String>, val notes: String)
private data class MucusLog(val type: String)
private data class BbtLog(val temperature: String)

@Composable
internal fun DailyLogScreen(onSave: () -> Unit) {
    val tabs = DailyLogTab.entries
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    val symptomsByDate = remember { mutableStateMapOf<LocalDate, SymptomsLog>() }
    val moodsByDate = remember { mutableStateMapOf<LocalDate, MoodLog>() }
    val mucusByDate = remember { mutableStateMapOf<LocalDate, MucusLog>() }
    val bbtByDate = remember { mutableStateMapOf<LocalDate, BbtLog>() }

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2200)
            toastMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Daily Log", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Track your symptoms, mood, and fertility signs", color = TextMuted, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(14.dp))

            CardContainer {
                Text("Select Date", fontWeight = FontWeight.Bold, color = TextPrimary)
                FieldPill(selectedDate.format(DisplayDateFormatter), Icons.Outlined.CalendarMonth) { showDatePicker = true }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8E8EC), RoundedCornerShape(18.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, tab ->
                    val selected = pagerState.currentPage == index
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (selected) Color.White else Color.Transparent, RoundedCornerShape(14.dp))
                            .clickable { scope.launch { pagerState.animateScrollToPage(index) } }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            tab.label,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (selected) TextPrimary else TextMuted
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                when (tabs[page]) {
                    DailyLogTab.Symptoms -> SymptomsPage(
                        currentLog = symptomsByDate[selectedDate],
                        onSaveLog = { log ->
                            symptomsByDate[selectedDate] = log
                            toastMessage = "Symptoms saved for ${selectedDate.format(DisplayDateFormatter)}"
                            onSave()
                        }
                    )

                    DailyLogTab.Mood -> MoodPage(
                        currentLog = moodsByDate[selectedDate],
                        onSaveLog = { log ->
                            moodsByDate[selectedDate] = log
                            toastMessage = "Mood saved for ${selectedDate.format(DisplayDateFormatter)}"
                            onSave()
                        }
                    )

                    DailyLogTab.Mucus -> MucusPage(
                        currentLog = mucusByDate[selectedDate],
                        onSaveLog = { log ->
                            mucusByDate[selectedDate] = log
                            toastMessage = "Cervical mucus saved for ${selectedDate.format(DisplayDateFormatter)}"
                            onSave()
                        }
                    )

                    DailyLogTab.BBT -> BbtPage(
                        currentLog = bbtByDate[selectedDate],
                        onSaveLog = { log ->
                            bbtByDate[selectedDate] = log
                            toastMessage = "BBT saved for ${selectedDate.format(DisplayDateFormatter)}"
                            onSave()
                        }
                    )
                }
            }
        }

        toastMessage?.let { message ->
            ToggleToast(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                message = message
            )
        }
    }

    if (showDatePicker) {
        AppCalendarDialog(
            initialDate = selectedDate,
            onDismiss = { showDatePicker = false },
            onDateSelected = {
                selectedDate = it
                showDatePicker = false
            }
        )
    }
}

@Composable
private fun SymptomsPage(currentLog: SymptomsLog?, onSaveLog: (SymptomsLog) -> Unit) {
    val selectedItems = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(currentLog) {
        selectedItems.clear()
        selectedItems.addAll(currentLog?.items ?: emptySet())
        notes = currentLog?.notes.orEmpty()
    }

    val sections = listOf(
        "Physical Symptoms" to listOf("Cramps", "Headache", "Bloating", "Breast Tenderness", "Backache", "Nausea", "Fatigue"),
        "Skin" to listOf("Acne", "Oily Skin", "Dry Skin"),
        "Digestion" to listOf("Constipation", "Diarrhea", "Appetite Changes")
    )

    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            sections.forEach { (title, items) ->
                CardContainer {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(Modifier.height(10.dp))
                    items.forEach { label ->
                        val selected = selectedItems.contains(label)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    if (selected) selectedItems.remove(label) else selectedItems.add(label)
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(if (selected) BrandPink.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(4.dp))
                                    .border(1.dp, if (selected) BrandPink else Color(0xFFD1D5DC), RoundedCornerShape(4.dp))
                            )
                            Text(label, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                        }
                    }
                }
            }
        }
        item {
            CardContainer {
                Text("Notes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(value = notes, onValueChange = { notes = it }, placeholder = { Text("Any additional thoughts or observations") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            }
        }
        item {
            Button(onClick = { onSaveLog(SymptomsLog(selectedItems.toSet(), notes)) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = DarkAction), shape = RoundedCornerShape(10.dp)) {
                Text("Save Symptoms", color = Color.White)
            }
        }
    }
}

@Composable
private fun MoodPage(currentLog: MoodLog?, onSaveLog: (MoodLog) -> Unit) {
    val moods = listOf("Happy" to "😊", "Calm" to "😌", "Anxious" to "😰", "Irritable" to "😤", "Sad" to "😢", "Sensitive" to "🥺", "Confident" to "😎")
    val selectedMoods = remember { mutableStateListOf<String>() }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(currentLog) {
        selectedMoods.clear()
        selectedMoods.addAll(currentLog?.moods ?: emptySet())
        notes = currentLog?.notes.orEmpty()
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            CardContainer {
                Text("How are you feeling?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
                Text("Select all that apply", style = MaterialTheme.typography.bodyLarge, color = TextMuted, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(2), userScrollEnabled = false, modifier = Modifier.height(430.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(moods) { (label, emoji) ->
                        val selected = selectedMoods.contains(label)
                        Card(
                            modifier = Modifier.fillMaxWidth().height(100.dp).clickable { if (selected) selectedMoods.remove(label) else selectedMoods.add(label) },
                            colors = CardDefaults.cardColors(containerColor = if (selected) BrandPink.copy(alpha = 0.12f) else Color.White),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Text(emoji, style = MaterialTheme.typography.headlineSmall)
                                Text(label, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            }
                        }
                    }
                }
            }
        }
        item {
            CardContainer {
                Text("Notes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(value = notes, onValueChange = { notes = it }, placeholder = { Text("Any additional thoughts or observations") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            }
        }
        item {
            Button(onClick = { onSaveLog(MoodLog(selectedMoods.toSet(), notes)) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = DarkAction)) {
                Text("Save Mood", color = Color.White)
            }
        }
    }
}

@Composable
private fun MucusPage(currentLog: MucusLog?, onSaveLog: (MucusLog) -> Unit) {
    var selectedType by remember { mutableStateOf("Select mucus type") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Dry", "Sticky", "Creamy", "Egg-white (stretchy)", "Watery")

    LaunchedEffect(currentLog) {
        selectedType = currentLog?.type ?: "Select mucus type"
    }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CardContainer {
            Text("Cervical Mucus Type", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text("Egg-white (stretchy) mucus indicates peak fertility", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF2F2F5), RoundedCornerShape(10.dp)).clickable { expanded = !expanded }.padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selectedType, style = MaterialTheme.typography.bodyLarge, color = TextMuted)
                Spacer(Modifier.weight(1f))
                Text("⌄", color = TextMuted)
            }
            if (expanded) {
                Spacer(Modifier.height(6.dp))
                options.forEach { option ->
                    Text(option, modifier = Modifier.fillMaxWidth().clickable { selectedType = option; expanded = false }.padding(vertical = 6.dp), color = TextPrimary)
                }
            }
        }
        Button(onClick = { onSaveLog(MucusLog(selectedType)) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = DarkAction.copy(alpha = 0.55f)), enabled = selectedType != "Select mucus type") {
            Text("Save Cervical Mucus", color = Color.White)
        }
    }
}

@Composable
private fun BbtPage(currentLog: BbtLog?, onSaveLog: (BbtLog) -> Unit) {
    var temperature by remember { mutableStateOf("36.5") }

    LaunchedEffect(currentLog) {
        temperature = currentLog?.temperature ?: "36.5"
    }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CardContainer {
            Text("Basal Body Temperature", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text("Take your temperature first thing in the morning before getting up", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = temperature, onValueChange = { temperature = it }, label = { Text("Temperature (°C)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F1FF)), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFB7D0FF))) {
                Text(
                    "Tip: A dip followed by a rise confirms ovulation. Normal BBT ranges from 36.1-36.4°C before ovulation and 36.4-37.0°C after.",
                    color = Color(0xFF0E44CC),
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(14.dp))
                OutlinedAction("View Partner Guide", Icons.Outlined.FavoriteBorder)
            }
        }
        Button(onClick = { onSaveLog(BbtLog(temperature)) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = DarkAction.copy(alpha = 0.60f))) {
            Text("Save BBT", color = Color.White)
        }
    }
}
