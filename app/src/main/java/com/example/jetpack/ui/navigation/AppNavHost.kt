package com.example.jetpack.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpack.core.common.ThemeHelper
import com.example.jetpack.di.AppContainer
import com.example.jetpack.ui.screens.detail.DetailScreen
import com.example.jetpack.ui.screens.detail.DetailViewModel
import com.example.jetpack.ui.screens.explore.ExploreScreen
import com.example.jetpack.ui.screens.explore.ExploreViewModel
import com.example.jetpack.ui.screens.home.HomeScreen
import com.example.jetpack.ui.screens.home.HomeViewModel
import com.example.jetpack.ui.screens.search.SearchScreen
import com.example.jetpack.ui.screens.search.SearchViewModel
import com.example.jetpack.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    appContainer: AppContainer,
    currentTheme: String = ThemeHelper.THEME_SYSTEM,
    onThemeChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val showBottomBar = bottomNavItems.any { it.route == currentRoute }
    val navIconSelectedColor = if (currentTheme == ThemeHelper.THEME_DARK) Color.White else Color.Black

    Scaffold(
        bottomBar = {
            if (showBottomBar && !isLandscape) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                screen.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = screen.title
                                    )
                                }
                            },
                            alwaysShowLabel = false,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = navIconSelectedColor,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Navigation Rail when device is in Landscape
            if (showBottomBar && isLandscape) {
                NavigationRail(
                    containerColor = MaterialTheme.colorScheme.surface,
                    header = {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationRailItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                screen.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = screen.title
                                    )
                                }
                            },
                            alwaysShowLabel = false,
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = navIconSelectedColor,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.fillMaxSize()
                ) {
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.provideFactory(appContainer.movieRepository)
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate(Screen.Detail.createRoute(movieId, mediaType))
                    },
                    onSearchClick = {
                        navController.navigate(Screen.Search.route)
                    },
                    onSeeAllClick = {
                        navController.navigate(Screen.Explore.route)
                    }
                )
            }

            composable(Screen.Explore.route) {
                val exploreViewModel: ExploreViewModel = viewModel(
                    factory = ExploreViewModel.provideFactory(appContainer.movieRepository)
                )
                ExploreScreen(
                    viewModel = exploreViewModel,
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate(Screen.Detail.createRoute(movieId, mediaType))
                    }
                )
            }

            composable(Screen.Downloads.route) {
                val searchViewModel: SearchViewModel = viewModel(
                    factory = SearchViewModel.provideFactory(appContainer.movieRepository)
                )
                SearchScreen(
                    viewModel = searchViewModel,
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate(Screen.Detail.createRoute(movieId, mediaType))
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Library.route) {
                SettingsScreen(
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Search.route) {
                val searchViewModel: SearchViewModel = viewModel(
                    factory = SearchViewModel.provideFactory(appContainer.movieRepository)
                )
                SearchScreen(
                    viewModel = searchViewModel,
                    onMovieClick = { movieId, mediaType ->
                        navController.navigate(Screen.Detail.createRoute(movieId, mediaType))
                    }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType },
                    navArgument("mediaType") {
                        type = NavType.StringType
                        defaultValue = "movie"
                    }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                val mediaType = backStackEntry.arguments?.getString("mediaType") ?: "movie"
                val detailViewModel: DetailViewModel = viewModel(
                    key = "detail_${mediaType}_$movieId",
                    factory = DetailViewModel.provideFactory(movieId, mediaType, appContainer.movieRepository)
                )
                DetailScreen(
                    viewModel = detailViewModel,
                    onBackClick = { navController.popBackStack() },
                    onMovieClick = { nextMovieId, nextMediaType ->
                        navController.navigate(Screen.Detail.createRoute(nextMovieId, nextMediaType))
                    }
                )
            }
        }
    }
}
}
}
