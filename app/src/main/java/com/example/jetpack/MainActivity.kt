package com.example.jetpack

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.jetpack.core.common.LocaleHelper
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
            JetpackTheme {
                AppNavHost(
                    appContainer = appContainer,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

