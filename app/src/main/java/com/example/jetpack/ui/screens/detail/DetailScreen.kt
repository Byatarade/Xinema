package com.example.jetpack.ui.screens.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.jetpack.core.common.Constants
import com.example.jetpack.domain.model.Cast
import com.example.jetpack.domain.model.Video
import com.example.jetpack.ui.components.ErrorView
import com.example.jetpack.ui.components.LoadingView
import com.example.jetpack.ui.components.RatingBadge
import com.example.jetpack.ui.components.SectionRow
import com.example.jetpack.ui.theme.DarkBackground
import com.example.jetpack.ui.theme.DarkSurface
import com.example.jetpack.ui.theme.DarkSurfaceVariant
import com.example.jetpack.ui.theme.LightText
import com.example.jetpack.ui.theme.MutedText
import com.example.jetpack.ui.theme.NetflixRed

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = LightText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
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
        ) {
            when {
                uiState.isLoading -> LoadingView()
                uiState.error != null -> {
                    ErrorView(
                        message = uiState.error ?: "Gagal memuat detail film",
                        onRetry = { viewModel.loadDetail() }
                    )
                }
                uiState.movie != null -> {
                    val movie = uiState.movie!!
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        // Backdrop Header with Gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                        ) {
                            AsyncImage(
                                model = Constants.backdropUrl(movie.backdropPath)
                                    ?: Constants.posterUrl(movie.posterPath),
                                contentDescription = movie.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                DarkBackground.copy(alpha = 0.7f),
                                                DarkBackground
                                            )
                                        )
                                    )
                            )
                        }

                        // Meta details
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Text(
                                text = movie.title,
                                color = LightText,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            movie.tagline?.takeIf { it.isNotBlank() }?.let { tagline ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"$tagline\"",
                                    color = MutedText,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (movie.voteAverage > 0) {
                                    RatingBadge(rating = movie.voteAverage)
                                }
                                movie.releaseDate?.take(4)?.let { year ->
                                    Text(text = year, color = MutedText, fontSize = 13.sp)
                                }
                                movie.runtime?.takeIf { it > 0 }?.let { minutes ->
                                    Text(text = "${minutes / 60}h ${minutes % 60}m", color = MutedText, fontSize = 13.sp)
                                }
                                Text(
                                    text = movie.status,
                                    color = NetflixRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Genres FlowRow
                            if (movie.genres.isNotEmpty()) {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    movie.genres.forEach { genre ->
                                        Box(
                                            modifier = Modifier
                                                .background(DarkSurfaceVariant, RoundedCornerShape(12.dp))
                                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = genre.name,
                                                color = LightText,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action buttons: Trailer + Watchlist
                            val trailer = movie.videos.firstOrNull { it.site.equals("YouTube", ignoreCase = true) && (it.type.equals("Trailer", ignoreCase = true) || it.type.equals("Teaser", ignoreCase = true)) }

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = {
                                        if (trailer != null) {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(Constants.youtubeWatchUrl(trailer.key))
                                            )
                                            context.startActivity(intent)
                                        }
                                    },
                                    enabled = trailer != null,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NetflixRed,
                                        contentColor = LightText
                                    ),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = if (trailer != null) "Watch Trailer" else "No Trailer", fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                OutlinedButton(
                                    onClick = { viewModel.toggleWatchlist() },
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = LightText),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isWatchlist) Icons.Default.Check else Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = if (uiState.isWatchlist) "In Watchlist" else "My List")
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Overview / Synopsis
                            Text(
                                text = "Storyline",
                                color = LightText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = movie.overview.ifBlank { "Tidak ada ringkasan sinopsis." },
                                color = MutedText,
                                style = MaterialTheme.typography.bodyMedium,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Cast list
                            if (movie.casts.isNotEmpty()) {
                                Text(
                                    text = "Top Cast",
                                    color = LightText,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    items(movie.casts, key = { it.id }) { cast ->
                                        CastItem(cast = cast)
                                    }
                                }
                            }

                            // Trailers section
                            if (movie.videos.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Videos & Clips",
                                    color = LightText,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(movie.videos.take(5), key = { it.id }) { video ->
                                        VideoThumbnailItem(
                                            video = video,
                                            onClick = {
                                                val intent = Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(Constants.youtubeWatchUrl(video.key))
                                                )
                                                context.startActivity(intent)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Similar movies
                        if (movie.similar.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            SectionRow(
                                title = "More Like This",
                                movies = movie.similar,
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

@Composable
fun CastItem(cast: Cast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(76.dp)
    ) {
        AsyncImage(
            model = Constants.profileUrl(cast.profilePath),
            contentDescription = cast.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(DarkSurfaceVariant),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = cast.name,
            color = LightText,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        cast.character?.let { charName ->
            Text(
                text = charName,
                color = MutedText,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun VideoThumbnailItem(
    video: Video,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(DarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = Constants.youtubeThumbnailUrl(video.key),
                    contentDescription = video.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Play icon overlay
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Black.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = NetflixRed,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(
                text = video.name,
                color = LightText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}

