package com.example.moodify

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class BottomNavItem(val route: String, val label: String, val iconResId: Int) {
    Home("home", "Home", R.drawable.ic_home),
    MoodBoard("mood_board", "MoodBoard", R.drawable.ic_mood_board),
    Diary("diary", "Diary", R.drawable.ic_diary),
    Stats("stats", "Stats", R.drawable.ic_stats),
    Resources("resources", "Resources", R.drawable.ic_resources)
}

@Composable
fun BottomNavigationBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.MoodBoard,
        BottomNavItem.Diary,
        BottomNavItem.Stats,
        BottomNavItem.Resources
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navItems.forEach { item ->
            val isSelected = selectedItem == item.route
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        onClick = { onItemSelected(item.route) },
                        indication = null, // Remove the click animation
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = item.iconResId),
                    contentDescription = item.label,
                    tint = if (isSelected) MaterialTheme.colorScheme.secondary else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item.label,
                    color = if (isSelected) MaterialTheme.colorScheme.secondary else Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
