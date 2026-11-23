package com.example.jetpack.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.R
import com.example.jetpack.domain.model.Movie
import com.example.jetpack.ui.components.ErrorView
import com.example.jetpack.ui.components.HeroBanner
import com.example.jetpack.ui.components.LoadingView
import com.example.jetpack.ui.components.SectionRow
import com.example.jetpack.ui.theme.NetflixRed

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMovieClick: (Int, String) -> Unit,
    onSearchClick: () -> Unit = {},
    onSeeAllClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                uiState.isLoading && uiState.trending.isEmpty() -> {
                    LoadingView()
                }
                uiState.error != null && uiState.trending.isEmpty() -> {
                    ErrorView(
                        message = uiState.error ?: stringResource(R.string.load_failed),
                        onRetry = { viewModel.loadHomeData() }
                    )
                }
                else -> {
                    val scrollState = rememberScrollState()

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

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        HeroBanner(
                            movie = uiState.heroMovie,
                            onMovieClick = { id -> onMovieClick(id, "movie") },
                            onPlayClick = { id -> onMovieClick(id, "movie") }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionRow(
                            title = stringResource(R.string.trending_now),
                            movies = uiState.trending,
                            onMovieClick = { id -> onMovieClick(id, "movie") },
                            onSeeAllClick = onSeeAllClick
                        )

                        if (tvAsMovies.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            SectionRow(
                                title = stringResource(R.string.series),
                                movies = tvAsMovies,
                                onMovieClick = { id -> onMovieClick(id, "tv") },
                                onSeeAllClick = onSeeAllClick
                            )
                        }

                        if (uiState.topRated.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            SectionRow(
                                title = stringResource(R.string.korean_drama),
                                movies = uiState.topRated,
                                onMovieClick = { id -> onMovieClick(id, "movie") },
                                onSeeAllClick = onSeeAllClick
                            )
                        }

                        if (uiState.popular.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            SectionRow(
                                title = stringResource(R.string.popular_movies),
                                movies = uiState.popular,
                                onMovieClick = { id -> onMovieClick(id, "movie") },
                                onSeeAllClick = onSeeAllClick
                            )
                        }

                        if (uiState.nowPlaying.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            SectionRow(
                                title = stringResource(R.string.now_playing),
                                movies = uiState.nowPlaying,
                                onMovieClick = { id -> onMovieClick(id, "movie") },
                                onSeeAllClick = onSeeAllClick
                            )
                        }

                        Spacer(modifier = Modifier.height(40.dp))
                    }

                    // Floating Top Bar (Logo on Left, Search on Right)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Xinema Brand
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_xinema_logo),
                                contentDescription = "Xinema Logo",
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = "XINEMA",
                                color = NetflixRed,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                        }

                        // Search Button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                                .clickable { onSearchClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
