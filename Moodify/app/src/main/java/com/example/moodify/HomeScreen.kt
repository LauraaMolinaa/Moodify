package com.example.moodify

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodify.ui.theme.MoodifyTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    var selectedScreen by remember { mutableStateOf("Home") }

    MoodifyTheme(darkTheme = isDarkTheme) {
        MoodifyScaffold(
            title = "Moodify",
            isDarkTheme = isDarkTheme,
            onToggleTheme = { isDarkTheme = !isDarkTheme },
            selectedScreen = selectedScreen,
            onScreenSelected = { selectedScreen = it }
        ) {
            when (selectedScreen) {
                "Home" -> HomeScreenContent()
                "MoodBoard" -> MoodBoardScreenContent(isDarkTheme = isDarkTheme, onToggleTheme = { isDarkTheme = !isDarkTheme })
                "Resources" -> ResourceScreen()
            }
        }
    }
}


@Composable
fun HomeScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainButtonsWithPlaceholders()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoodBoardScreenContent(isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    MoodBoardScreen(
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme
    )
}


@Composable
fun MainButtonsWithPlaceholders() {
    // Placeholders
    // TODO: Link data after more screens and database are created
    val sections = listOf(
        "Mood Tracker" to "Last entry: 2023-11-12\nYour progress: 70%",
        "Diary" to "Last entry: 2023-11-10\nEntries: 24",
        "Statistics" to "Average mood: 4.2\nThis month’s insights",
        "Resources" to "Explore resources"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        sections.forEach { (title, description) ->
            // Outer Box to create a shadow effect
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
                // Inner Card with gradient and rounded corners
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Navigate to respective screen */ },
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



@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun LightModePreview() {
    MoodifyTheme {
        HomeScreen()
    }
}
