package com.example.moodify

import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import java.util.Date

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

    fun insert_color(color: String) {
        // get the writable database
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("name", color)
        }

        // insert the data into the table
        db.insert("Color", null, values)
    }

    fun insert_gratefulness_entry(description: String, gratefulnessId: Int) {
        // get the writable database
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("description", description)
            put("gratefulnes_id", gratefulnessId)
        }

        // insert the data into the table
        db.insert("GratefulnessEntry", null, values)
    }

    fun insert_gratefulness(date: String) {
        // get the writable database
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("date", date)
            //put("dairy_id", diaryId)
        }

        // insert the data into the table
        db.insert("Gratefulness", null, values)
    }

    fun insert_diary(description: String)
    {
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("description", description)
            //put("gratefulnes_id", gratefulnessId)
        }

        // insert the data into the table
        db.insert("Diary", null, values)
    }

    fun insert_moodboard(date: String, colorId: Int, diaryId: Int)
    {
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("date", date)
            put("colorId", colorId)
            put("diaryId", diaryId)
        }

        // insert the data into the table
        db.insert("Moodboard", null, values)
    }

    fun populateColorsTable() {
        insert_color("perfect") //perfect colorId 1
        insert_color("happy") //happy colorId 2
        insert_color("okay") //okay colorId 3
        insert_color("sad") //sad colorId 4
        insert_color("anxious") //anxious colorId 5
        insert_color("depressed") //depressed colorId 6

    }


}