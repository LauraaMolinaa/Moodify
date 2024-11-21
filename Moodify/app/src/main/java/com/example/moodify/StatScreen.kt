package com.example.moodify

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
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer

@Composable
fun StatScreen() {
    val totalValue = 100f  // total percentage for the pie chart
    // defining the mood colors and their descriptions
    val moodColors = listOf(
        Slice(20f, Color.Red) to "Angry/Bad Day",
        Slice(15f, Color.Green) to "Calm/Relaxed",
        Slice(30f, Color.Yellow) to "Happy/Positive",
        Slice(20f, Color.Cyan) to "Sad/Down",
        Slice(15f, Color.Magenta) to "Energetic/Creative"
    ).map { (slice, description) ->
        // calculating the actual percentage of each slice
        slice to "$description (${(slice.value / totalValue * 100).toInt()}%)"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        PieChart(
            pies = Pies(moodColors.map { it.first }),
            modifier = Modifier
                .height(340.dp)
                .fillMaxWidth(),
            sliceDrawer = FilledSliceDrawer(thickness = 50f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Legend(moodColors)
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
@Preview
@Composable
fun PreviewStatScreen() {
    Surface {
        StatScreen()
    }
}