package com.example.jetpack.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.domain.model.Movie
import com.example.jetpack.ui.components.ErrorView
import com.example.jetpack.ui.components.HeroBanner
import com.example.jetpack.ui.components.LoadingView
import com.example.jetpack.ui.components.SectionRow
import com.example.jetpack.ui.theme.DarkBackground
import com.example.jetpack.ui.theme.NetflixRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CINESTREAM",
                        color = NetflixRed,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        letterSpacing = 2.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground.copy(alpha = 0.95f)
                )
            )
        },
        containerColor = DarkBackground,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            when {
                uiState.isLoading && uiState.trending.isEmpty() -> {
                    LoadingView()
                }
                uiState.error != null && uiState.trending.isEmpty() -> {
                    ErrorView(
                        message = uiState.error ?: "Gagal memuat data",
                        onRetry = { viewModel.loadHomeData() }
                    )
                }
                else -> {
                    val scrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        HeroBanner(
                            movie = uiState.heroMovie,
                            onMovieClick = onMovieClick,
                            onPlayClick = onMovieClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionRow(
                            title = "Now Playing in Theaters",
                            movies = uiState.nowPlaying,
                            onMovieClick = onMovieClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionRow(
                            title = "Trending This Week",
                            movies = uiState.trending,
                            onMovieClick = onMovieClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionRow(
                            title = "Popular on CineStream",
                            movies = uiState.popular,
                            onMovieClick = onMovieClick
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionRow(
                            title = "Critically Acclaimed & Top Rated",
                            movies = uiState.topRated,
                            onMovieClick = onMovieClick
                        )

                        // Convert TV shows to movies for preview section
                        val tvAsMovies = uiState.tvShows.map { tv ->
                            Movie(
                                id = tv.id,
                                title = tv.name,
                                overview = tv.overview,
                                posterPath = tv.posterPath,
                                backdropPath = tv.backdropPath,
                                releaseDate = tv.firstAirDate,
                                voteAverage = tv.voteAverage,
                                voteCount = tv.voteCount,
                                genreIds = tv.genreIds,
                                mediaType = "tv"
                            )
                        }

                        if (tvAsMovies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            SectionRow(
                                title = "Popular TV Shows",
                                movies = tvAsMovies,
                                onMovieClick = onMovieClick
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

