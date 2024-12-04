package com.example.moodify.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodify.ui.theme.MoodifyTheme
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoodBoardScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    val today = LocalDate.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7 // Adjust for 0-indexed grid
    val moods = remember { mutableStateMapOf<LocalDate, Color>() }

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

        CalendarGrid(
            daysInMonth = daysInMonth,
            firstDayOfWeek = firstDayOfWeek,
            selectedDay = selectedDay,
            onDaySelected = { day ->
                selectedDay = day
            },
            today = today,
            currentMonth = currentMonth,
            moods = moods
        )

        Spacer(modifier = Modifier.height(16.dp))

        MoodColorPickerWithLabels { color ->
            selectedDay?.let { day ->
                moods[day] = color
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarGrid(
    daysInMonth: Int,
    firstDayOfWeek: Int,
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate) -> Unit,
    today: LocalDate,
    currentMonth: YearMonth,
    moods: Map<LocalDate, Color>
) {
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
        items(daysInMonth) { index ->
            val day = index + 1
            val date = currentMonth.atDay(day)
            val moodColor = moods[date] ?: Color.Transparent

            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .background(
                        color = moodColor, // Only show mood color
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onDaySelected(date) }
                    .border(
                        width = if (date == selectedDay) 2.dp else 1.dp,
                        color = if (date == selectedDay) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    fontWeight = if (date == today) FontWeight.Bold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
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
        Text(
            text = "Pick a Mood",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
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



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun MoodPreview() {
    MoodifyTheme {
        MoodBoardScreen(
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}


