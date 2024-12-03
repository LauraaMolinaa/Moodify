package com.example.moodify

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//source https://developer.android.com/training/data-storage/sqlite
//source https://www.codersarts.com/post/integrating-sqlite-in-android-app-using-kotlin-a-step-by-step-guide

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    //what color is what number
    //perfect colorId 1
    //happy colorId 2
    //okay colorId 3
    //sad colorId 4
    //anxious colorId 5
    //depressed colorId 6

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_DB)
        db.execSQL(CREATE_STATISTICS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldData: Int, newData: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "moodify.db"
    }

    private val CREATE_DB = """
        CREATE DATABASE Moodify
    """

    private val CREATE_STATISTICS_TABLE = """
        CREATE TABLE Statistics(
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            average_mood REAL, 
            diary_adherence REAL
        )
    """

    private val CREATE_COLOR_TABLE = """ 
        CREATE TABLE Color(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            name TEXT
        )
    """

    private val CREATE_GRATEFULNESS_ENTRY_TABLE = """ 
        CREATE TABLE GratefulnessEntry(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            description TEXT, 
            FOREIGN KEY(gratefulnes_id) REFERENCES Gratefulness(id)
        )
    """

    private val CREATE_GRATEFULNESS_TABLE = """ 
        CREATE TABLE Gratefulness(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            date TEXT, 
            FOREIGN KEY(dairy_id) REFERENCES Diary(id)
        )
    """

    private val CREATE_DIARY_TABLE = """ 
        CREATE TABLE Diary(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            description TEXT, 
            FOREIGN KEY(gratefulness_id) REFERENCES Gratefulness(id)
        )
    """

    private val CREATE_MOODBOARD_TABLE = """ 
        CREATE TABLE Moodboard(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            date TEXT, 
            FOREIGN KEY(color_id) REFERENCES Color(id),
            FOREIGN KEY(diary_id) REFERENCES Diary(id)
        )
    """
    //date format is day-month-year like DD-MM-YYYY

}