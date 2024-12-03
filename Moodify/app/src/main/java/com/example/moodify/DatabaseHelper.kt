package com.example.moodify

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//source https://developer.android.com/training/data-storage/sqlite
//source https://www.codersarts.com/post/integrating-sqlite-in-android-app-using-kotlin-a-step-by-step-guide

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 12
        const val DATABASE_NAME = "moodify.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_STATISTICS_TABLE)
        db.execSQL(CREATE_COLOR_TABLE)
        db.execSQL(CREATE_DIARY_TABLE)
        db.execSQL(CREATE_MOODBOARD_TABLE)
        db.execSQL(CREATE_GRATEFULNESS_TABLE)
        db.execSQL(CREATE_GRATEFULNESS_ENTRY_TABLE)

    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("PRAGMA foreign_keys=ON;")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldData: Int, newData: Int) {
        db.execSQL("DROP TABLE IF EXISTS Statistics")
        db.execSQL("DROP TABLE IF EXISTS Color")
        db.execSQL("DROP TABLE IF EXISTS GratefulnessEntry")
        db.execSQL("DROP TABLE IF EXISTS Gratefulness")
        db.execSQL("DROP TABLE IF EXISTS Diary")
        db.execSQL("DROP TABLE IF EXISTS Moodboard")
        onCreate(db)
    }

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
            name TEXT, 
            emotion TEXT
        )
    """

    private val CREATE_DIARY_TABLE = """ 
        CREATE TABLE Diary(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            description TEXT, 
            date TEXT
        )
    """

    private val CREATE_MOODBOARD_TABLE = """ 
        CREATE TABLE Moodboard(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            date TEXT, 
            color_id INTEGER,
            diary_id INTEGER,
            FOREIGN KEY(color_id) REFERENCES Color(id),
            FOREIGN KEY(diary_id) REFERENCES Diary(id)
        )
    """

    private val CREATE_GRATEFULNESS_TABLE = """ 
        CREATE TABLE Gratefulness(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            date TEXT,
            diary_id INTEGER,
            FOREIGN KEY(diary_id) REFERENCES Diary(id)
        )
        
    """

    private val CREATE_GRATEFULNESS_ENTRY_TABLE = """ 
        CREATE TABLE GratefulnessEntry(
            id INTEGER PRIMARY KEY AUTOINCREMENT,  
            description TEXT, 
            gratefulnes_id INT,
            FOREIGN KEY(gratefulnes_id) REFERENCES Gratefulness(id)
        )
    """
    //date format is day-month-year like DD-MM-YYYY

}