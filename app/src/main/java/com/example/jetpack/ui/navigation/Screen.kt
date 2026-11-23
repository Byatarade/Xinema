package com.example.jetpack.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Explore : Screen("explore", "Explore", Icons.Default.Explore)
    data object Downloads : Screen("downloads", "Downloads", Icons.Default.FileDownload)
    data object Library : Screen("library", "Library", Icons.Default.VideoLibrary)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    data object Search : Screen("search", "Search", Icons.Default.Search)

    data object Detail : Screen("detail/{movieId}?mediaType={mediaType}") {
        fun createRoute(movieId: Int, mediaType: String = "movie"): String = "detail/$movieId?mediaType=$mediaType"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Explore,
    Screen.Downloads,
    Screen.Settings
)



