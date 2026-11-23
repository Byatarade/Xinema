package com.example.jetpack

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.jetpack.core.common.LocaleHelper
import com.example.jetpack.core.common.ThemeHelper
import com.example.jetpack.ui.navigation.AppNavHost
import com.example.jetpack.ui.theme.JetpackTheme

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appContainer = (application as CineStreamApp).container

        setContent {
            val context = LocalContext.current
            var currentTheme by remember { mutableStateOf(ThemeHelper.getTheme(context)) }
            val systemInDark = isSystemInDarkTheme()
            val isDark = ThemeHelper.isDarkTheme(currentTheme, systemInDark)

            JetpackTheme(darkTheme = isDark) {
                AppNavHost(
                    appContainer = appContainer,
                    currentTheme = currentTheme,
                    onThemeChange = { newTheme ->
                        currentTheme = newTheme
                        ThemeHelper.setTheme(context, newTheme)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
