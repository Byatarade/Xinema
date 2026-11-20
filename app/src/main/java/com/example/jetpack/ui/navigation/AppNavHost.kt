package com.example.jetpack.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpack.di.AppContainer
import com.example.jetpack.ui.screens.detail.DetailScreen
import com.example.jetpack.ui.screens.detail.DetailViewModel
import com.example.jetpack.ui.screens.explore.ExploreScreen
import com.example.jetpack.ui.screens.explore.ExploreViewModel
import com.example.jetpack.ui.screens.home.HomeScreen
import com.example.jetpack.ui.screens.home.HomeViewModel
import com.example.jetpack.ui.screens.search.SearchScreen
import com.example.jetpack.ui.screens.search.SearchViewModel
import com.example.jetpack.ui.theme.DarkBackground
import com.example.jetpack.ui.theme.DarkSurface
import com.example.jetpack.ui.theme.LightText
import com.example.jetpack.ui.theme.MutedText
import com.example.jetpack.ui.theme.NetflixRed

@Composable
fun AppNavHost(
    appContainer: AppContainer,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkSurface,
                    tonalElevation = 0.dp
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
                                selectedIconColor = LightText,
                                unselectedIconColor = MutedText.copy(alpha = 0.6f),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        },
        containerColor = DarkBackground,
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.provideFactory(appContainer.movieRepository)
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
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
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
                    }
                )
            }

            composable(Screen.Downloads.route) {
                val searchViewModel: SearchViewModel = viewModel(
                    factory = SearchViewModel.provideFactory(appContainer.movieRepository)
                )
                SearchScreen(
                    viewModel = searchViewModel,
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
                    }
                )
            }

            composable(Screen.Library.route) {
                val exploreViewModel: ExploreViewModel = viewModel(
                    factory = ExploreViewModel.provideFactory(appContainer.movieRepository)
                )
                ExploreScreen(
                    viewModel = exploreViewModel,
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
                    }
                )
            }

            composable(Screen.Search.route) {
                val searchViewModel: SearchViewModel = viewModel(
                    factory = SearchViewModel.provideFactory(appContainer.movieRepository)
                )
                SearchScreen(
                    viewModel = searchViewModel,
                    onMovieClick = { movieId ->
                        navController.navigate(Screen.Detail.createRoute(movieId))
                    }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
                val detailViewModel: DetailViewModel = viewModel(
                    key = "detail_$movieId",
                    factory = DetailViewModel.provideFactory(movieId, appContainer.movieRepository)
                )
                DetailScreen(
                    viewModel = detailViewModel,
                    onBackClick = { navController.popBackStack() },
                    onMovieClick = { nextMovieId ->
                        navController.navigate(Screen.Detail.createRoute(nextMovieId))
                    }
                )
            }
        }
    }
}

