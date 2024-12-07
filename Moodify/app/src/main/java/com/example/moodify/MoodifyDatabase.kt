package com.example.moodify

import android.content.ClipDescription
import android.content.ContentValues
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import java.util.Date

class MoodifyDatabase(context: Context) {

    val databaseHelper = DatabaseHelper(context)
    private var _isInitialized: Boolean = false

    var isInitialized: Boolean
        get() = _isInitialized
        set(value) { _isInitialized = value }

    fun reset_db_data()
    {
        val db = databaseHelper.writableDatabase

        db.execSQL("DELETE FROM Statistics")
        db.execSQL("DELETE FROM GratefulnessEntry")
        db.execSQL("DELETE FROM Gratefulness")
        db.execSQL("DELETE FROM Moodboard")
        db.execSQL("DELETE FROM Diary")
        db.execSQL("DELETE FROM Color")

        db.execSQL("DELETE FROM sqlite_sequence")

    }

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

    fun insert_color(color: String, emotion: String) {
        // get the writable database
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("name", color)
            put("emotion", emotion)
        }

        // insert the data into the table
        db.insert("Color", null, values)
    }

    fun insert_gratefulness_entry(description: String, gratefulnessId: Int) {
        // get the writable database
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("description", description)
            put("gratefulness_id", gratefulnessId)
        }

        // insert the data into the table
        db.insert("GratefulnessEntry", null, values)
    }

    fun insert_gratefulness(date: String, diaryId: Int):Long {
        // get the writable database
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("date", date)
            put("diary_id", diaryId)
        }

        // insert the data into the table
        return db.insert("Gratefulness", null, values)
    }

    fun insert_diary(description: String, date: String): Long
    {
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("description", description)
            put("date", date)
        }

        // insert the data into the table
        return db.insert("Diary", null, values)
    }

    fun insert_moodboard(date: String, colorId: Int, diaryId: Int)
    {
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("date", date)
            put("color_id", colorId)
            put("diary_id", diaryId)
        }

        // insert the data into the table
        db.insert("Moodboard", null, values)
    }

    fun deleteEntryWithNoDate() {
        val db = databaseHelper.writableDatabase
        db.delete("Diary", "date IS NULL OR date = ?", arrayOf(""))
    }

    fun populateColorsTable() {
        insert_color("magenta","perfect") //perfect colorId 1
        insert_color("yellow", "happy") //happy colorId 2
        insert_color("green", "okay") //okay colorId 3
        insert_color("blue","sad") //sad colorId 4
        insert_color("white","anxious") //anxious colorId 5
        insert_color("red","depressed") //depressed colorId 6
    }

    fun getDiaries(): List<Diary> {
        val db = databaseHelper.readableDatabase
        val diaryList = mutableListOf<Diary>()
        val cursor = db.query("Diary", null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val description = getString(getColumnIndexOrThrow("description"))
                val date = getString(getColumnIndexOrThrow("date"))
                diaryList.add(Diary(id, description, date))
            }
            close()
        }
        return diaryList
    }

    fun getDiariesDescription(diaryId: Int): String? {
        val db = databaseHelper.readableDatabase
        var description: String? = null

        val where = "id = ?"
        val args = arrayOf(diaryId.toString())

        val cursor = db.query("Diary", arrayOf("description"), where, args, null, null, null)

        with(cursor) {
            if (moveToFirst()) {
                description = getString(getColumnIndexOrThrow("description"))
            }
            close()
        }

        return description
    }


    fun getStatistics(): List<Statistics> {
        val db = databaseHelper.readableDatabase
        val statisticsList = mutableListOf<Statistics>()
        val cursor = db.query("Statistics", null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val averageMood = getDouble(getColumnIndexOrThrow("average_mood"))
                val diaryAdherence = getDouble(getColumnIndexOrThrow("diary_adherence"))
                statisticsList.add(Statistics(id, averageMood, diaryAdherence))
            }
            close()
        }
        return statisticsList
    }

    /**
     * Retrieves all the entries from the Moodboard table in the database and converts
     * them into a list of Moodboard objects
     */
    fun getMoodboardData(): List<Moodboard> {
        val db = databaseHelper.readableDatabase
        val moodboardList = mutableListOf<Moodboard>()
        val cursor = db.query("Moodboard", null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val date = getString(getColumnIndexOrThrow("date"))
                val colorId = getInt(getColumnIndexOrThrow("color_id"))
                val diaryId = getInt(getColumnIndexOrThrow("diary_id"))
                moodboardList.add(Moodboard(id, date, colorId, diaryId))
            }
            close()
        }
        return moodboardList
    }

    fun getMoodboardDiaryId(date:String): Int? {
        val db = databaseHelper.readableDatabase
        var diaryId: Int? = null

        val where = "date = ?"
        val args = arrayOf(date)

        val cursor = db.query("Moodboard", arrayOf("diary_id"), where, args, null, null, null)

        with(cursor) {
            if (moveToFirst()) {
                diaryId = getInt(getColumnIndexOrThrow("diary_id"))
            }
            close()
        }

        return diaryId
    }

    fun updateMoodboardData(diaryId:Int, date: String): Int{
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("diary_id", diaryId)
        }

        val where = "date = ?"
        val args = arrayOf(date)

        return db.update("Moodboard", values, where, args)
    }

    /**
     * Retrieves all the color definitions from the Color table, mapping each color to its
     * associated ID and emotion
     */
    fun getColors(): List<Color> {
        val db = databaseHelper.readableDatabase
        val colorList = mutableListOf<Color>()
        val cursor = db.query("Color", null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val emotion = getString(getColumnIndexOrThrow("emotion"))
                colorList.add(Color(id, name, emotion))
            }
            close()
        }
        return colorList
    }

    //getting the gratefullness
    fun getGratefulnessEntries(diaryId: Int): List<String> {
        val db = databaseHelper.readableDatabase
        val gratefulnessEntries = mutableListOf<String>()

        val query = """
        SELECT GratefulnessEntry.description 
        FROM GratefulnessEntry
        INNER JOIN Gratefulness ON GratefulnessEntry.gratefulness_id = Gratefulness.id
        WHERE Gratefulness.diary_id = ?
    """

        val cursor = db.rawQuery(query, arrayOf(diaryId.toString()))

        with(cursor) {
            while (moveToNext()) {
                val description = getString(getColumnIndexOrThrow("description"))
                gratefulnessEntries.add(description)
            }
            close()
        }
        return gratefulnessEntries
    }


}