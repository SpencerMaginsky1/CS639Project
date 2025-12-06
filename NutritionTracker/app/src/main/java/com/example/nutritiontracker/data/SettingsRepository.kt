package com.example.nutritiontracker.data

import android.content.Context
import android.content.SharedPreferences

class SettingsRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_AGE = "age"
        private const val KEY_GENDER = "gender"
        private const val KEY_HEIGHT = "height"
        private const val KEY_WEIGHT = "weight"
        private const val KEY_BODY_FAT = "body_fat"
        private const val KEY_ACTIVITY_LEVEL = "activity_level"
        private const val KEY_GOAL_TYPE = "goal_type"
        private const val KEY_DIET_TYPE = "diet_type"
        private const val KEY_ALLERGIES = "allergies"
    }

    fun saveSettings(settings: SettingsData) {
        sharedPreferences.edit().apply {
            putString(KEY_NAME, settings.name)
            putString(KEY_AGE, settings.age)
            putString(KEY_GENDER, settings.gender)
            putString(KEY_HEIGHT, settings.height)
            putString(KEY_WEIGHT, settings.weight)
            putString(KEY_BODY_FAT, settings.bodyFat)
            putString(KEY_ACTIVITY_LEVEL, settings.activityLevel)
            putString(KEY_GOAL_TYPE, settings.goalType)
            putString(KEY_DIET_TYPE, settings.dietType)
            putString(KEY_ALLERGIES, settings.allergies)
            apply()
        }
    }

    fun loadSettings(): SettingsData {
        return SettingsData(
            name = sharedPreferences.getString(KEY_NAME, "") ?: "",
            age = sharedPreferences.getString(KEY_AGE, "") ?: "",
            gender = sharedPreferences.getString(KEY_GENDER, "") ?: "",
            height = sharedPreferences.getString(KEY_HEIGHT, "") ?: "",
            weight = sharedPreferences.getString(KEY_WEIGHT, "") ?: "",
            bodyFat = sharedPreferences.getString(KEY_BODY_FAT, "") ?: "",
            activityLevel = sharedPreferences.getString(KEY_ACTIVITY_LEVEL, "") ?: "",
            goalType = sharedPreferences.getString(KEY_GOAL_TYPE, "") ?: "",
            dietType = sharedPreferences.getString(KEY_DIET_TYPE, "") ?: "",
            allergies = sharedPreferences.getString(KEY_ALLERGIES, "") ?: ""
        )
    }

    fun clearSettings() {
        sharedPreferences.edit().clear().apply()
    }
}
