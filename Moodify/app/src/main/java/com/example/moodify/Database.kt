package com.example.moodify

import java.sql.Date

class Database {

    //what color is what number
    //perfect colorId 1
    //happy colorId 2
    //okay colorId 3
    //sad colorId 4
    //anxious colorId 5
    //depressed colorId 6

    companion object {
        private val DATABASE_NAME = "moodify.db"

        private const val CREATE_TABLE_MOODBOARD = """ 
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            color_id INT, 
            date TEXT, 
            diary_id INT 
        """
        //date format is day-month-year like DD-MM-YYYY
    }
}