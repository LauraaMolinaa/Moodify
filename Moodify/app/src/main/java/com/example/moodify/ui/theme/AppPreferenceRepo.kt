package com.example.moodify.ui.theme


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey

import androidx.datastore.preferences.core.edit
import com.example.moodify.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppPreferenceRepo(private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>) {
    private companion object {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }

    suspend fun getDarkTheme(context: Context): Boolean {
        val darkTheme: Flow<Boolean> = context.dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[IS_DARK_THEME] ?: false
            }
        return darkTheme.first()
    }

    suspend fun saveToDataStore(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }
}