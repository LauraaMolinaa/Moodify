package com.example.moodify.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodify.MoodifyScaffold
import com.example.moodify.ui.theme.MoodifyTheme

@Composable
fun HomeScreenContent(
    lastDiaryEntryDate: String,
    totalDiaryEntries: Int,
    averageMood: Double,
    progress: Double,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainButtonsWithDynamicData(
            lastDiaryEntryDate = lastDiaryEntryDate,
            totalDiaryEntries = totalDiaryEntries,
            averageMood = averageMood,
            progress = progress
        )
    }
}


@Composable
fun MainButtonsWithDynamicData(
    lastDiaryEntryDate: String,
    totalDiaryEntries: Int,
    averageMood: Double,
    progress: Double
) {
    val sections = listOf(
        "Mood Tracker" to "Last entry: $lastDiaryEntryDate\nYour progress: $progress%",
        "Diary" to "Last entry: $lastDiaryEntryDate\nEntries: $totalDiaryEntries",
        "Statistics" to "Average mood: $averageMood\nThis month’s insights",
        "Resources" to "Explore resources"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        sections.forEach { (title, description) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(4.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Navigate to respective screen */ },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                    )
                                )
                            )
                            .padding(24.dp)
                            .animateContentSize()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = title,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = description,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LightModePreview() {
    MoodifyTheme {
        HomeScreenContent(
            lastDiaryEntryDate = "03-03-2024",
            totalDiaryEntries = 3,
            averageMood = 4.7,
            progress = 67.0,
            isDarkTheme = false,
            onToggleTheme = {}
        )
    }
}
