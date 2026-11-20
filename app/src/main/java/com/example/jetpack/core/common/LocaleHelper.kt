package com.example.jetpack.core.common

import android.app.LocaleManager
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object LocaleHelper {
    private const val PREFS_NAME = "cinedb_preferences"
    private const val KEY_LANGUAGE = "selected_language"

    const val LANGUAGE_EN = "en"
    const val LANGUAGE_IN = "in"

    fun getLanguage(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, null) ?: getDefaultLanguage()
    }

    private fun getDefaultLanguage(): String {
        val systemLang = Locale.getDefault().language
        return if (systemLang.equals("in", ignoreCase = true) || systemLang.equals("id", ignoreCase = true)) {
            LANGUAGE_IN
        } else {
            LANGUAGE_EN
        }
    }

    fun setLanguage(context: Context, languageCode: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                val localeTag = if (languageCode == LANGUAGE_IN) "id" else languageCode
                val localeManager = context.getSystemService(LocaleManager::class.java)
                localeManager?.applicationLocales = LocaleList.forLanguageTags(localeTag)
            } catch (_: Exception) {
            }
        }
    }

    fun wrapContext(context: Context): Context {
        val languageCode = getLanguage(context)
        val locale = if (languageCode == LANGUAGE_IN) {
            Locale.forLanguageTag("id-ID")
        } else {
            Locale.forLanguageTag("en-US")
        }
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
