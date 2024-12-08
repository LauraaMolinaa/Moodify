package com.example.moodify.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.text.style.TextOverflow
import com.example.moodify.BottomNavItem
import com.example.moodify.ColorRepository
import com.example.moodify.DiaryRepository
import com.example.moodify.MoodboardRepository
import com.example.moodify.MoodifyDatabase


fun mapColorNameToComposeColor(name: String): Color {
    return when (name.lowercase()) {
        "magenta" -> Color.Magenta
        "yellow" -> Color.Yellow
        "green" -> Color.Green
        "blue" -> Color.Blue
        "white" -> Color.White
        "red" -> Color.Red
        else -> Color.Transparent
    }
}

@Composable
fun MoodBoardScreen(
    db: MoodifyDatabase,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    moodboardRepository: MoodboardRepository,
    colorRepository: ColorRepository,
    diaryRepository: DiaryRepository,
    navController: NavController
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val today = LocalDate.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7 // Adjust for 0-indexed grid
    val moods = remember { mutableStateMapOf<LocalDate, Color>() }
    val diaryEntries by remember { mutableStateOf(diaryRepository.getAllDiaries()) }

    LaunchedEffect(currentMonth) {
        val moodboards = moodboardRepository.getAllMoodboards()
        val colors = colorRepository.getAllColors().associateBy { it.id } // Map colorId to Color object

        moods.clear()
        moodboards.forEach { moodboard ->
            val date = LocalDate.parse(moodboard.date, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val color = colors[moodboard.colorId]?.let { mapColorNameToComposeColor(it.name) } ?: Color.Transparent
            moods[date] = color
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBarWithMonthSelector(
            selectedMonth = currentMonth.month,
            selectedYear = currentMonth.year,
            onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
            onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Grid
        CalendarGrid(
            daysInMonth = daysInMonth,
            firstDayOfWeek = firstDayOfWeek,
            selectedDay = selectedDay,
            onDaySelected = { date -> selectedDay = date },
            onDayDoubleClicked = { date ->
                val formattedDate = date.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                val diaryEntry = diaryEntries.find { it.date == formattedDate }
                val route = if (diaryEntry != null) {
                    "${BottomNavItem.Diary.route}?date=$formattedDate&entry=${diaryEntry.description}"
                } else {
                    "${BottomNavItem.Diary.route}?date=$formattedDate"
                }
                navController.navigate(route)
            },
            today = today,
            currentMonth = currentMonth,
            moods = moods,
            moodboardRepository = moodboardRepository,
            colorRepository = colorRepository,
            diaryRepository = diaryRepository,
            onMoodUpdated = { date, color ->
                moods[date] = color // Update the UI after database change
                db.recalculateAndSaveStatistics()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        MoodColorPickerWithLabels { color ->
            selectedDay?.let { day ->
                val colorId = colorRepository.getAllColors()
                    .find { mapColorNameToComposeColor(it.name) == color }?.id
                if (colorId != null) {
                    val date = day.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    val diaryId = diaryRepository.getAllDiaries().find { it.date == date }?.id
                    moodboardRepository.insertOrUpdateMoodboard(date, colorId, diaryId)
                    moods[day] = color
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Searchable List of Diary Entries
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Diary Entries") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            val filteredEntries = diaryEntries.filter { entry ->
                entry.description.contains(searchQuery, ignoreCase = true) ||
                        entry.date.contains(searchQuery, ignoreCase = true)
            }
            items(filteredEntries) { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val route = "${BottomNavItem.Diary.route}?date=${entry.date}&entry=${entry.description}"
                            navController.navigate(route)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.date,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = entry.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(2f)
                    )
                }
                Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            }
        }

    }
}





@Composable
fun TopBarWithMonthSelector(
    selectedMonth: Month,
    selectedYear: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous Month")
        }
        Text(
            text = "${selectedMonth.name.capitalize()} $selectedYear",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}

@Composable
fun CalendarGrid(
    daysInMonth: Int,
    firstDayOfWeek: Int,
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate) -> Unit,
    onDayDoubleClicked: (LocalDate) -> Unit,
    today: LocalDate,
    currentMonth: YearMonth,
    moods: Map<LocalDate, Color>,
    moodboardRepository: MoodboardRepository,
    colorRepository: ColorRepository,
    diaryRepository: DiaryRepository,
    onMoodUpdated: (LocalDate, Color) -> Unit,
) {
    val clickTimestamps = remember { mutableStateMapOf<LocalDate, Long>() }
    val doubleClickThreshold = 300
    val diaryDates = remember {
        diaryRepository.getAllDiaries()
            .mapNotNull {
                try {
                    parseDate(it.date)
                } catch (e: Exception) {
                    null
                }
            }.toSet()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Day labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            items(firstDayOfWeek) {
                Spacer(modifier = Modifier.aspectRatio(1f))
            }

            // Calendar days
            items(daysInMonth) { index ->
                val day = index + 1
                val date = currentMonth.atDay(day)
                val hasDiaryEntry = diaryDates.contains(date)
                val moodColor = moods[date]?.copy(alpha = 0.4f) ?: Color.Transparent

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .background(
                            color = moodColor,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            val currentTime = System.currentTimeMillis()
                            val lastClickTime = clickTimestamps[date] ?: 0L

                            if (currentTime - lastClickTime <= doubleClickThreshold) {
                                onDayDoubleClicked(date)
                            } else {
                                onDaySelected(date)
                            }
                            clickTimestamps[date] = currentTime
                        }
                        .border(
                            width = if (date == selectedDay) 2.dp else 1.dp,
                            color = if (date == selectedDay) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = day.toString(),
                            fontWeight = if (date == today) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (hasDiaryEntry) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Diary Entry Present",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}





@Composable
fun MoodColorPickerWithLabels(onColorSelected: (Color) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val moodColors = listOf(
                Color.Magenta to "Perfect",
                Color.Yellow to "Happy",
                Color.Green to "Okay",
                Color.Blue to "Sad",
                Color.Red to "Depressed"
            )
            moodColors.forEach { (color, label) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onColorSelected(color) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color, shape = CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

fun parseDate(dateString: String): LocalDate {
    return try {
        LocalDate.parse(dateString.substringBefore("T"))
    } catch (e: Exception) {
        try {
            // Parse MM/dd/yyyy format
            LocalDate.parse(
                dateString,
                java.time.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")
            )
        } catch (e: Exception) {
            try {
                // Parse dd-MM-yyyy format
                LocalDate.parse(
                    dateString,
                    java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
                )
            } catch (e: Exception) {
                throw IllegalArgumentException("Unsupported date format: $dateString")
            }
        }
    }
}