package com.example.moodify

import android.content.ContentValues
import android.content.Context
import androidx.compose.ui.platform.LocalContext

class MoodifyDatabase(context: Context) {

    private val databaseHelper = DatabaseHelper(context)

    fun insert_statistics(averageMood: Double, diaryAdherence: Double) {
        // get the writable database
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("average_mood", averageMood)
            put("diary_adherence", diaryAdherence)
        }

        // insert the data into the table
        db.insert("Statistics", null, values)
    }
}