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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moodify.MoodifyDatabase
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer

@Composable
fun StatScreen(db: MoodifyDatabase) {

    // all mood entries from the Moodboard table
    val moodEntries = db.getMoodboardData()

    // determine the total number of mood entries
    val totalEntries = moodEntries.size

    // calculate the frequency of each colorId in the mood entries
    val moodFrequencies = moodEntries.groupingBy { it.colorId }.eachCount()
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
            Text("No data available for this month.")
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