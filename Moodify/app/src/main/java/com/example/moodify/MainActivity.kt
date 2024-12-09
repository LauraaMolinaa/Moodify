package com.example.moodify

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moodify.screens.DiaryScreenContent
import com.example.moodify.screens.HomeScreenContent
import com.example.moodify.screens.MoodBoardScreen
import com.example.moodify.screens.ResourceScreen
import com.example.moodify.screens.StatScreen
import com.example.moodify.ui.theme.AppPreferenceRepo
import com.example.moodify.ui.theme.MoodifyTheme
import android.content.Context
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

const val MOODIFY_DATASTORE ="moodify_datastore"
val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    MOODIFY_DATASTORE
)

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val ds = context.dataStore
            val dataStoreManager = AppPreferenceRepo(ds)
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            lifecycleScope.launch {
                val themeTemp:Boolean = dataStoreManager.getDarkTheme(context)
                println(themeTemp)
                isDarkTheme = themeTemp
            }

            val navController = rememberNavController()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route ?: BottomNavItem.Home.route
            val db = MoodifyDatabase(LocalContext.current)

            // Initialize the database with dummy data once
            LaunchedEffect(Unit) {
                if (!db.isInitialized) {
                    db.reset_db_data()
                    populateDbDummyData(db)
                    db.isInitialized = true
                }
            }

            // Fetch data for HomeScreen
            val diaryEntries = db.getDiaries()
            val lastDiaryEntry = diaryEntries.maxByOrNull { it.id }
            val totalDiaryEntries = diaryEntries.size

            MoodifyTheme(darkTheme = isDarkTheme) {
                MoodifyScaffold(
                    title = "Moodify",
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = {
                        isDarkTheme = !isDarkTheme
                        lifecycleScope.launch {
                            dataStoreManager.saveToDataStore(isDarkTheme)
                        }
                    },
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
                        composable(BottomNavItem.Home.route) {
                            HomeScreenContent(
                                db = db,
                                lastDiaryEntryDate = lastDiaryEntry?.date ?: "No entries",
                                totalDiaryEntries = totalDiaryEntries,
                                isDarkTheme = isDarkTheme,
                                onToggleTheme = { isDarkTheme = !isDarkTheme }
                            )
                        }

                        composable(BottomNavItem.MoodBoard.route) {
                            val moodboardRepository = MoodboardRepository(databaseHelper = db.databaseHelper)
                            val colorRepository = ColorRepository(databaseHelper = db.databaseHelper)
                            val diaryRepository = DiaryRepository(databaseHelper = db.databaseHelper)

                            MoodBoardScreen(
                                db = db,
                                isDarkTheme = isDarkTheme,
                                onToggleTheme = { isDarkTheme = !isDarkTheme },
                                moodboardRepository = moodboardRepository,
                                colorRepository = colorRepository,
                                diaryRepository = diaryRepository,
                                navController = navController
                            )
                        }

                        composable(
                            route = "${BottomNavItem.Diary.route}?date={date}&entry={entry}",
                            arguments = listOf(
                                navArgument("date") { defaultValue = "" },
                                navArgument("entry") { defaultValue = "" }
                            )
                        ) { backStackEntry ->
                            val date = backStackEntry.arguments?.getString("date") ?: ""
                            val entry = backStackEntry.arguments?.getString("entry") ?: ""

                            DiaryScreenContent(
                                isDarkTheme = isDarkTheme,
                                onToggleTheme = { isDarkTheme = !isDarkTheme },
                                date = date,
                                entry = entry
                            )
                        }

                        composable(BottomNavItem.Stats.route) { StatScreen(db) }
                        composable(BottomNavItem.Resources.route) { ResourceScreen() }
                    }
                }
            }
        }
    }

    private fun populateDbDummyData(db: MoodifyDatabase) {
        //what color is what number
        db.populateColorsTable()
        // Predefined colors
        //val moodColors = listOf("magenta", "yellow", "green", "blue", "white", "red")
        val moodColors = listOf("magenta", "yellow", "green", "blue", "red")

        // Dummy data entries
        val dummyData = listOf(
            Triple(
                "A comfortable bed, Warm morning sunlight, A supportive friend who checked in today",
                "Today started gently, with a soft, warm glow filtering through my bedroom window. I woke up feeling rested because my bed felt like a safe cocoon, the comfort I often take for granted. Later, when I hesitated over a new project at work, my friend messaged me out of the blue. Her encouraging words reminded me that I am not alone as I navigate my goals. I’m ending the day feeling optimistic and grateful.",
                -1
            ),
            Triple(
                "A pleasant walk during lunch break, Nutritious home-cooked meals, Access to a good healthcare plan",
                "My midday walk was refreshing—there’s a quiet path behind the office where the trees sway gently in the breeze. After work, I prepared a simple meal filled with vibrant vegetables, feeling thankful that I can nourish my body in such a natural way. Knowing I have reliable healthcare coverage also brings me peace of mind. Good health and security are true gifts.",
                -2
            ),
            Triple(
                "A favorite book that I’m rereading, A cozy sweater on a cool evening, Video calls with family abroad",
                "I spent part of the evening curled up on the couch, sinking into familiar chapters of a beloved novel. Its characters feel like old friends, warming my spirit as much as my thick-knit sweater warms my shoulders. Later, I hopped onto a video call and caught up with my sister, who lives miles away. Despite the distance, the laughter and love we share span continents, making me realize how connected I truly am.",
                -3
            ),
            Triple(
                "Public libraries and their endless knowledge, A hot shower after a long day, Humor that lightens tense moments",
                "Today I popped into the local library on a whim. The quiet aisles of books reminded me that I’m never far from wisdom and inspiration. Returning home, the tension in my shoulders eased under a steamy shower, washing away stress. I also found myself chuckling at a silly meme before bed, a tiny reminder that laughter can dissolve even the heaviest clouds.",
                -4
            ),
            Triple(
                "Fresh fruits and their natural sweetness, Music that uplifts my mood, The ability to learn something new each day",
                "This afternoon, I enjoyed a bowl of fresh strawberries—each bite tasted like summer sunshine. With music playing softly in the background, I felt my worries fade with every note. I spent some time learning a new skill online today, amazed by the wealth of knowledge just a click away. It’s empowering to know I can grow every day if I choose to.",
                -5
            ),
            Triple(
                "Quiet moments of solitude, A steady job that pays the bills, The kindness of strangers",
                "Amid a busy week, I took a few minutes in the morning just to sit quietly and breathe. That simple pause helped ground me. I’m grateful for my job, not just because it provides stability, but because it gives me purpose. And small acts by strangers—like someone holding the door open—remind me there’s goodness in the world I’m part of.",
                -6
            ),
            Triple(
                "A supportive boss who values work-life balance, Clean drinking water, A warm blanket to snuggle under",
                "This evening, I’m feeling particularly grateful for the understanding nature of my boss, who encouraged me to leave on time. I’m also mindful of simple privileges, like turning on the tap and having safe water. Before bed, I’m settling in under my favorite blanket, feeling its softness and appreciating the day’s gentle close.",
                -7
            ),
            Triple(
                "The internet to stay informed and connected, Green plants thriving on my windowsill, The resilience I’ve built from past challenges",
                "Scrolling through an online forum today, I realized how interconnected we all are, sharing knowledge and experiences. I noticed how well my little potted plants are growing, a silent reminder of life’s persistent cycle. Reflecting on my past hardships, I see how far I’ve come. My resilience is proof that I can withstand and grow through almost anything.",
                -8
            ),
            Triple(
                "Sunsets that paint the sky in fiery colors, A good night’s sleep that renews my energy, The curiosity that keeps me exploring",
                "I paused to admire the sunset this evening—an evolving canvas of oranges and pinks fading into dusky purple. Yesterday’s fatigue dissolved after a long, restorative sleep. I feel energized and curious about what tomorrow might hold. It’s curiosity that keeps life fresh, motivating me to try new paths and cherish the simple wonders around me.",
                -9
            ),
            Triple(
                "The ability to show compassion towards others, Calm breathing techniques when anxious, Hope for the future",
                "Today, I offered a kind word to someone who looked downcast, and it felt like a small but meaningful gift. When I felt a sudden wave of stress, I focused on my breath—slow, steady inhales and exhales—to find calm within myself. Despite the unknown roads ahead, I carry hope with me, feeling confident that as long as I remain compassionate, mindful, and hopeful, I will continue to find reasons to be grateful.",
                -10
            )
        )

        val today = LocalDate.now()

        // Populate data into the database
        dummyData.forEach { (gratitudes, diaryEntry, dayOffset) ->
            val date = today.plusDays(dayOffset.toLong())
            val formattedDate = date.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val colorName = moodColors.random()
            val colorId = db.getColors().find { it.name == colorName }?.id

            // Insert diary entry
            val diaryId = db.insert_diary(diaryEntry, formattedDate)

            // Insert gratitude entries
            val gratitudeItems = gratitudes.split(", ").map { it.trim() }
            val gratefulnessId = db.insert_gratefulness(formattedDate, diaryId.toInt())
            gratitudeItems.forEach { gratitude ->
                db.insert_gratefulness_entry(gratitude, gratefulnessId.toInt())
            }

            // Insert moodboard entry
            if (colorId != null) {
                db.insert_moodboard(formattedDate, colorId, diaryId.toInt())
            }
        }

//        db.deleteEntryWithNoDate()

        //dummy data
//        db.insert_statistics(5.5, 30.0)
        db.insert_statistics(3.4, 80.7)
//        db.insert_statistics(1.7, 30.0)
//        db.insert_statistics(6.0, 10.5)
//        db.insert_statistics(4.7, 67.0)
        /*
        db.insert_diary("diary 1", "03-03-2024")
        db.insert_diary("diary 2", "05-03-2024")
        db.insert_diary("diary 3", "09-03-2024")
        db.insert_diary("diary 4", "13-03-2024")
        db.insert_diary("diary 5", "20-03-2024")

        db.insert_moodboard("03-03-2024", 6, 1)
        db.insert_moodboard("05-03-2024", 1, 2)
        db.insert_moodboard("09-03-2024", 4, 3)
        db.insert_moodboard("13-03-2024", 2, 4)
        db.insert_moodboard("20-03-2024", 2, 5)

        db.insert_gratefulness("03-03-2024", 1)
        db.insert_gratefulness("05-03-2024", 2)
        db.insert_gratefulness("09-03-2024", 3)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 1", 1)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 1", 1)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 1", 1)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 2", 2)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 2", 2)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 2", 2)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 3", 3)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 3", 3)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 3", 3)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 4", 4)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 4", 4)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 4", 4)

        db.insert_gratefulness_entry("grateful 1 of gratefulnessId 5", 5)
        db.insert_gratefulness_entry("grateful 2 of gratefulnessId 5", 5)
        db.insert_gratefulness_entry("grateful 3 of gratefulnessId 5", 5)
         */
    }
}