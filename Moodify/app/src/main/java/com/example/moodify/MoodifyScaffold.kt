package com.example.moodify

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MoodifyScaffold(
    title: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    selectedScreen: String,
    onScreenSelected: (String) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = title,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedScreen,
                onItemSelected = onScreenSelected,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}

@Composable
fun TopBar(
    title: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Icon(
            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_sun else R.drawable.ic_moon),
            contentDescription = "Toggle Theme",
            modifier = Modifier
                .size(48.dp)
                .clickable { onToggleTheme() }
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .padding(12.dp),
            tint = Color.White
        )
    }
}