package com.example.moodify

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    var selectedMonth by remember { mutableStateOf(LocalDate.now().month) }
    var selectedDay by remember { mutableStateOf<LocalDate?>(null) }
    var moodColor by remember { mutableStateOf(Color.Transparent) }
    val daysInMonth = YearMonth.now().lengthOfMonth()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBarWithMonthSelector(
            selectedMonth = selectedMonth,
            onPreviousMonth = { selectedMonth = selectedMonth.minus(1) },
            onNextMonth = { selectedMonth = selectedMonth.plus(1) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CalendarGrid(
            daysInMonth = daysInMonth,
            selectedDay = selectedDay,
            onDaySelected = { selectedDay = it },
            moodColor = moodColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        MoodColorPicker(onColorSelected = { moodColor = it })
    }
}



@Composable
fun TopBarWithMonthSelector(
    selectedMonth: Month,
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
            text = selectedMonth.name.capitalize(),
            fontSize = 24.sp,
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
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate) -> Unit,
    moodColor: Color
) {
    val today = LocalDate.now().dayOfMonth
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
    ) {
        items(daysInMonth) { day ->
            val dayDate = LocalDate.of(LocalDate.now().year, LocalDate.now().month, day + 1)
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .background(
                        color = if (day + 1 == today) moodColor else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onDaySelected(dayDate) }
                    .border(
                        width = 2.dp,
                        color = if (dayDate == selectedDay) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${day + 1}",
                    color = if (day + 1 == today) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun MoodColorPicker(onColorSelected: (Color) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val moodColors = listOf(Color.Green, Color.Yellow, Color.Red, Color.Blue, Color.Magenta)
        moodColors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, shape = CircleShape)
                    .clickable { onColorSelected(color) }
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = CircleShape
                    )
            )
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


