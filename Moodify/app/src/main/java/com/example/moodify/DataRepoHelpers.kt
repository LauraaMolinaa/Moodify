package com.example.moodify

import android.content.ContentValues

class StatisticsRepository(private val databaseHelper: DatabaseHelper) {

    fun getAllStatistics(): List<Statistics> {
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
}

class ColorRepository(private val databaseHelper: DatabaseHelper) {

    fun getAllColors(): List<Color> {
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
}


class DiaryRepository(private val databaseHelper: DatabaseHelper) {

    fun getAllDiaries(): List<Diary> {
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
}


class MoodboardRepository(private val databaseHelper: DatabaseHelper) {

    fun getAllMoodboards(): List<Moodboard> {
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

    fun insertOrUpdateMoodboard(date: String, colorId: Int, diaryId: Int?) {
        val db = databaseHelper.writableDatabase

        // Check if a moodboard entry already exists for the given date
        val cursor = db.rawQuery("SELECT id FROM Moodboard WHERE date = ?", arrayOf(date))

        val values = ContentValues().apply {
            put("date", date)
            put("color_id", colorId)
            if (diaryId != null) {
                put("diary_id", diaryId)
            } else {
                putNull("diary_id")
            }
        }

        if (cursor.moveToFirst()) {
            // Update existing entry
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            db.update("Moodboard", values, "id = ?", arrayOf(id.toString()))
            println("Moodboard updated for date: $date with colorId: $colorId and diaryId: $diaryId")
        } else {
            // Insert new entry
            db.insert("Moodboard", null, values)
            println("Moodboard inserted for date: $date with colorId: $colorId and diaryId: $diaryId")
        }

        cursor.close()
    }
}



class GratefulnessRepository(private val databaseHelper: DatabaseHelper) {

    fun getAllGratefulness(): List<Gratefulness> {
        val db = databaseHelper.readableDatabase
        val gratefulnessList = mutableListOf<Gratefulness>()
        val cursor = db.query("Gratefulness", null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val date = getString(getColumnIndexOrThrow("date"))
                val diaryId = getInt(getColumnIndexOrThrow("diary_id"))
                gratefulnessList.add(Gratefulness(id, date, diaryId))
            }
            close()
        }
        return gratefulnessList
    }
}


class GratefulnessEntryRepository(private val databaseHelper: DatabaseHelper) {

    fun getAllGratefulnessEntries(): List<GratefulnessEntry> {
        val db = databaseHelper.readableDatabase
        val gratefulnessEntryList = mutableListOf<GratefulnessEntry>()
        val cursor = db.query("GratefulnessEntry", null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("id"))
                val description = getString(getColumnIndexOrThrow("description"))
                val gratefulnessId = getInt(getColumnIndexOrThrow("gratefulness_id"))
                gratefulnessEntryList.add(GratefulnessEntry(id, description, gratefulnessId))
            }
            close()
        }
        return gratefulnessEntryList
    }
}

