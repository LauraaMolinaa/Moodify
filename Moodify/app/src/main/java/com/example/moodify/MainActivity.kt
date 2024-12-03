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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moodify.ui.theme.MoodifyTheme
import java.sql.Date
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route ?: BottomNavItem.Home.route
            val db = MoodifyDatabase(LocalContext.current)

            populateDbDummyData(db)

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

                        composable(BottomNavItem.Stats.route) { StatScreen() }
                        composable(BottomNavItem.Resources.route) { ResourceScreen() }
                        // TODO: Add more routes as the screens are created
                    }
                }
            }
        }
    }

    private fun populateDbDummyData(db: MoodifyDatabase) {
        //what color is what number

        db.populateColorsTable()

        //dummy data
        db.insert_statistics(5.5, 30.0)
        db.insert_statistics(3.4, 80.7)
        db.insert_statistics(1.7, 30.0)
        db.insert_statistics(6.0, 10.5)
        db.insert_statistics(4.7, 67.0)

//        will not work because the gratefulness table has a diary FK and the diary table has a gratefulness FK
        db.insert_diary("diary 1")
        db.insert_diary("diary 2")
        db.insert_diary("diary 3")

        db.insert_gratefulness("03-03-2024")
        db.insert_gratefulness("05-03-2024")
        db.insert_gratefulness("09-03-2024")

        db.insert_moodboard("03-03-2024", 6, 1)
        db.insert_moodboard("05-03-2024", 1, 2)
        db.insert_moodboard("09-03-2024", 4, 3)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 1", 1)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 1", 1)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 1", 1)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 2", 2)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 2", 2)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 2", 2)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 3", 3)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 3", 3)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 3", 3)

    }
}