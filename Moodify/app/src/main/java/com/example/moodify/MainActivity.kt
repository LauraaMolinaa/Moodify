package com.example.moodify

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moodify.ui.theme.MoodifyTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route ?: BottomNavItem.Home.route

            MoodifyTheme(darkTheme = isDarkTheme) {
                MoodifyScaffold(
                    title = "Moodify",
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme },
                    selectedScreen = currentRoute,
                    onScreenSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.route
                    ) {
                        composable(BottomNavItem.Home.route) { HomeScreenContent() }
                        composable(BottomNavItem.MoodBoard.route) { MoodBoardScreenContent(isDarkTheme, onToggleTheme = { isDarkTheme = !isDarkTheme }) }
                        composable(BottomNavItem.Diary.route) { DiaryScreenContent(isDarkTheme, onToggleTheme = { isDarkTheme = !isDarkTheme }) }
                        // TODO: Add more routes as the screens are created
                    }
                }
            }
        }
    }
}