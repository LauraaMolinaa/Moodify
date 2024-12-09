package com.example.moodify.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.moodify.MoodifyDatabase
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun StatScreen(db: MoodifyDatabase) {

    // state to track the selected month and year
    var selectedMonthYear by remember { mutableStateOf(YearMonth.now()) }

    // filter mood entries by selected month
    val moodEntriesForMonth = db.getMoodEntriesForMonth(selectedMonthYear)

    // determine the total number of mood entries for the selected month
    val totalEntries = moodEntriesForMonth.size

    // calculate the frequency of each colorId in the mood entries for the selected month
    val moodFrequencies = moodEntriesForMonth.groupingBy { it.colorId }.eachCount()
    // retrieve all colors and their associated emotions from the Color table
    val moodColors = db.getColors()

    // Calculate percentage and map to slices
    val slicesWithDescriptions = moodColors.mapNotNull { color ->
        val count = moodFrequencies[color.id] ?: 0 // if the color isn't present, defaults to 0
        if (count > 0) {
            val percentage = (count.toFloat() / totalEntries) * 100
            Slice(percentage, Color(android.graphics.Color.parseColor(color.name))) to "${color.emotion} (${percentage.toInt()}%)"
        } else {
            // If the count is 0, excludes the color from the pie chart and legend by
            // returning null
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        TopBarWithMonthSelector(
            selectedMonth = selectedMonthYear.month,
            selectedYear = selectedMonthYear.year,
            onPreviousMonth = { selectedMonthYear = selectedMonthYear.minusMonths(1) },
            onNextMonth = { selectedMonthYear = selectedMonthYear.plusMonths(1) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (slicesWithDescriptions.isNotEmpty()) {
            PieChart(
                pies = Pies(slicesWithDescriptions.map { it.first }),
                modifier = Modifier
                    .height(340.dp)
                    .fillMaxWidth(),
                sliceDrawer = FilledSliceDrawer(thickness = 50f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Legend(slicesWithDescriptions)
        } else {
            // display a message if there's no data for the selected month
            Text("No data available for ${selectedMonthYear.month.name.capitalize()} ${selectedMonthYear.year}")
        }
    }
}

@Composable
fun Legend(moodColors: List<Pair<Slice, String>>) {
    Column {
        // display mood descriptions with percentages
        moodColors.forEach { (slice, description) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp, 20.dp)
                        .background(slice.color)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = description
                )
            }
        }
    }
}

// Resource (library) inspired from: https://github.com/fracassi-marco/JetChart :)