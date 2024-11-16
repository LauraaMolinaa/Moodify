package com.example.moodify

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodify.ui.theme.MoodifyTheme
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun HomeScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    MoodifyTheme(darkTheme = isDarkTheme) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            val (topBar, mainContent, bottomBar) = createRefs()

            // Top bar with app name and theme toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Moodify",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    painter = painterResource(id = if (isDarkTheme) R.drawable.ic_sun else R.drawable.ic_moon),
                    contentDescription = "Toggle Theme",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { isDarkTheme = !isDarkTheme }
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(12.dp),
                    tint = Color.White
                )
            }

            // Main content area with placeholders, flexible height and scrollable
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(mainContent) {
                        top.linkTo(topBar.bottom, margin = 20.dp)
                        bottom.linkTo(bottomBar.top, margin = 20.dp)
                        height = Dimension.fillToConstraints
                    }
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                MainButtonsWithPlaceholders()
            }

            // Bottom navigation bar anchored to the bottom of the screen
            BottomNavigationBar(
                modifier = Modifier
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun MainButtonsWithPlaceholders() {
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
            // Outer Box to create a custom shadow effect
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
                                color = MaterialTheme.colorScheme.primary, // Ensures high contrast for readability
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = description,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f), // Higher opacity for better contrast
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf("Home") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navItems = listOf("Home", "Stats", "Diary", "Resources")
        navItems.forEach { item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                        selectedItem = item
                    }
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = item,
                    color = if (selectedItem == item) MaterialTheme.colorScheme.secondary else Color.White.copy(alpha = 0.7f), // Better contrast for inactive items
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(4.dp)
                )
                if (selectedItem == item) {
                    Box(
                        modifier = Modifier
                            .width(20.dp)  // Wider underline for a clearer selected state
                            .height(3.dp)
                            .background(MaterialTheme.colorScheme.secondary, RectangleShape)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LightModePreview() {
    MoodifyTheme {
        HomeScreen()
    }
}
