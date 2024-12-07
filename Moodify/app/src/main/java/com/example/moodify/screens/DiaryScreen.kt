package com.example.moodify.screens


import android.content.ContentValues
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodify.DatabaseHelper
import com.example.moodify.MoodifyDatabase
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun DiaryScreenContent(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    date: String = "",
    entry: String = ""
) {
    val moodifyDatabase = MoodifyDatabase(LocalContext.current)
    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }
    var input3 by remember { mutableStateOf("") }
    var entry by remember { mutableStateOf("") }
    var aiResponse by remember { mutableStateOf("") }
    var index by remember { mutableStateOf(0) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .padding(24.dp)
                    .animateContentSize(),
                contentAlignment = Alignment.Center // Centers the content inside the Box
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, // Centers items horizontally
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "Gratitude",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    // First input field
                    OutlinedTextField(
                        value = input1,
                        onValueChange = { input1 = it },
                        label = { Text("Input 1") },
                    )
                    // Second input field
                    OutlinedTextField(
                        value = input2,
                        onValueChange = { input2 = it },
                        label = { Text("Input 2") },
                    )
                    // Third input field
                    OutlinedTextField(
                        value = input3,
                        onValueChange = { input3 = it },
                        label = { Text("Input 3") },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    )
                    .padding(24.dp)
                    .animateContentSize(),
                contentAlignment = Alignment.Center // Centers the content inside the Box
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, // Centers items horizontally
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "Diary",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = entry,
                        onValueChange = { entry = it },
                        label = { Text("Diary entry") },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), // Ensures the Box takes all available height
                contentAlignment = Alignment.Center // Centers content within the Box
            ) {
                Text(
                    text = if (aiResponse.isEmpty()) "Click 'Give me a prompt' to get a prompt related to your 3 gratitudes" else aiResponse,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Ensures buttons are spaced out
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    aiResponse = saveData(
                        db = moodifyDatabase,
                        input1 = input1,
                        input2 = input2,
                        input3 = input3,
                        entry = entry,
                        index = index,
                    ) {
                        input1 = ""
                        input2 = ""
                        input3 = ""
                        entry = ""
                    }

                    index = index + 3
                },
                modifier = Modifier.weight(1f) // Ensures both buttons have equal width
            ) {
                Text(text = "Save", color = Color.Black)
            }

            Spacer(modifier = Modifier.width(16.dp)) // Adds space between the buttons

            Button(
                onClick = {
                    coroutineScope.launch {
                        aiResponse = TestAI(input1, input2, input3).toString()
                    }
                },
                modifier = Modifier.weight(1f) // Ensures both buttons have equal width
            ) {
                Text(text = "Give me a prompt", color = Color.Black)
            }
        }


    }
}

suspend fun TestAI(input1: String, input2: String, input3: String): String? {
    val generativeModel =
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyCLpbLftJdeBtm26I87u8xovOTT15q07xc" ,
        )

    val prompt = "Using these three gratitude entry can you give a small sentence (prompt) on what we should write about in our diary entry. The first gratitude is ${input1}, second is ${input2} and the last one is ${input3} "
    val response = generativeModel.generateContent(prompt)
    return response.text
}
fun saveData(
    db: MoodifyDatabase,
    input1: String,
    input2: String,
    input3: String,
    entry: String,
    index: Int,
    onClearInputs: () -> Unit
): String {

    //getting today's date
    val date = LocalDateTime.now().toString()

    try {
        var diaryId = db.insert_diary(entry, date)

        var gratefulnessId = db.insert_gratefulness(date,diaryId.toInt())

        //var diaryId = db.getDiaries()
        db.insert_gratefulness_entry(input1, gratefulnessId.toInt())
        db.insert_gratefulness_entry(input2, gratefulnessId.toInt())
        db.insert_gratefulness_entry(input3, gratefulnessId.toInt())

        // Clear inputs after saving
        onClearInputs()
        return "Your data has been saved!"
    } catch (e: Exception) {
        e.printStackTrace()
        return "Failed to save data: ${e.message}"
    }
}
