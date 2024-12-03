package com.example.moodify


//Example usage:
//val diary = Diary(id = 1, description = "Had a great day", date = "01-12-2024")
//val color = Color(id = 1, name = "Purple", emotion = "Perfect")
//val moodboard = Moodboard(id = 1, date = "01-12-2024", colorId = color.id, diaryId = diary.id)
//val gratefulness = Gratefulness(id = 1, date = "01-12-2024", diaryId = diary.id)
//val gratefulnessEntry = GratefulnessEntry(id = 1, description = "Grateful for friends", gratefulnessId = gratefulness.id)


data class Color(
    val id: Int,
    val name: String,
    val emotion: String
)

data class Statistics(
    val id: Int,
    val averageMood: Double,
    val diaryAdherence: Double
)

data class Diary(
    val id: Int,
    val description: String,
    val date: String // Stored in `DD-MM-YYYY` format
)

data class Moodboard(
    val id: Int,
    val date: String, // Stored in `DD-MM-YYYY` format
    val colorId: Int, // Foreign key referencing `Color.id`
    val diaryId: Int // Foreign key referencing `Diary.id`
)

data class Gratefulness(
    val id: Int,
    val date: String, // Stored in `DD-MM-YYYY` format
    val diaryId: Int // Foreign key referencing `Diary.id`
)

data class GratefulnessEntry(
    val id: Int,
    val description: String,
    val gratefulnessId: Int // Foreign key referencing `Gratefulness.id`
)
