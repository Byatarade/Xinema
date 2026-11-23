package com.example.jetpack.core.common

import android.content.Context
import android.content.SharedPreferences

object ThemeHelper {
    private const val PREFS_NAME = "cinedb_preferences"
    private const val KEY_THEME = "selected_theme"

    const val THEME_SYSTEM = "system"
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"

    fun getTheme(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_THEME, THEME_SYSTEM) ?: THEME_SYSTEM
    }

    fun setTheme(context: Context, theme: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_THEME, theme).apply()
    }

    fun isDarkTheme(themeSetting: String, isSystemDark: Boolean): Boolean {
        return when (themeSetting) {
            THEME_LIGHT -> false
            THEME_DARK -> true
            else -> isSystemDark
        }
    }
}
