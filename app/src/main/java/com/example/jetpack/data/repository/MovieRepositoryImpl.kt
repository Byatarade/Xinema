package com.example.jetpack.data.repository

import com.example.jetpack.core.common.Resource
import com.example.jetpack.data.remote.TmdbApiService
import com.example.jetpack.domain.model.Movie
import com.example.jetpack.domain.model.MovieDetail
import com.example.jetpack.domain.model.TvShow
import com.example.jetpack.domain.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(
    private val api: TmdbApiService
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getTrendingMovies()
            val movies = response.results.map { it.toDomain() }
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load trending movies", e))
        }
    }

    override fun getNowPlayingMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getNowPlaying()
            val movies = response.results.map { it.toDomain() }
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load now playing movies", e))
        }
    }

    override fun getPopularMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getPopularMovies()
            val movies = response.results.map { it.toDomain() }
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load popular movies", e))
        }
    }

    override fun getTopRatedMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getTopRatedMovies()
            val movies = response.results.map { it.toDomain() }
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load top rated movies", e))
        }
    }

    override fun getPopularTvShows(): Flow<Resource<List<TvShow>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.getPopularTvShows()
            val tvShows = response.results.map { it.toDomain() }
            emit(Resource.Success(tvShows))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load TV shows", e))
        }
    }

    override fun getMovieDetail(movieId: Int, mediaType: String): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading)
        try {
            coroutineScope {
                if (mediaType == "tv") {
                    val detailDeferred = async { api.getTvDetail(movieId) }
                    val creditsDeferred = async { runCatching { api.getTvCredits(movieId) }.getOrNull() }
                    val videosDeferred = async { runCatching { api.getTvVideos(movieId) }.getOrNull() }
                    val similarDeferred = async { runCatching { api.getSimilarTvShows(movieId) }.getOrNull() }

                    val detailDto = detailDeferred.await()
                    val creditsDto = creditsDeferred.await()
                    val videosDto = videosDeferred.await()
                    val similarDto = similarDeferred.await()

                    val casts = creditsDto?.cast?.take(15)?.map { it.toDomain() } ?: emptyList()
                    val videos = videosDto?.results?.map { it.toDomain() } ?: emptyList()
                    val similar = similarDto?.results?.map { it.toMovie() } ?: emptyList()

                    val movieDetail = detailDto.toDomain(
                        casts = casts,
                        videos = videos,
                        similar = similar
                    )
                    emit(Resource.Success(movieDetail))
                } else {
                    val detailDeferred = async { api.getMovieDetail(movieId) }
                    val creditsDeferred = async { runCatching { api.getMovieCredits(movieId) }.getOrNull() }
                    val videosDeferred = async { runCatching { api.getMovieVideos(movieId) }.getOrNull() }
                    val similarDeferred = async { runCatching { api.getSimilarMovies(movieId) }.getOrNull() }

                    val detailDto = detailDeferred.await()
                    val creditsDto = creditsDeferred.await()
                    val videosDto = videosDeferred.await()
                    val similarDto = similarDeferred.await()

                    val casts = creditsDto?.cast?.take(15)?.map { it.toDomain() } ?: emptyList()
                    val videos = videosDto?.results?.map { it.toDomain() } ?: emptyList()
                    val similar = similarDto?.results?.map { it.toDomain() } ?: emptyList()

                    val movieDetail = detailDto.toDomain(
                        casts = casts,
                        videos = videos,
                        similar = similar
                    )
                    emit(Resource.Success(movieDetail))
                }
            }
        } catch (e: Exception) {
            val userFriendlyMessage = if (e.message?.contains("404") == true) {
                "Konten tidak ditemukan atau belum tersedia"
            } else {
                e.localizedMessage ?: "Failed to load movie details"
            }
            emit(Resource.Error(userFriendlyMessage, e))
        }
    }

    override fun search(query: String): Flow<Resource<List<Movie>>> = flow {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            emit(Resource.Success(emptyList()))
            return@flow
        }
        emit(Resource.Loading)
        try {
            val response = api.searchMulti(query = trimmed)
            // Filter out people (actors/directors) and entries without posters to prevent 404s
            val results = response.results
                .filter { it.mediaType != "person" && (it.posterPath != null || it.backdropPath != null) }
                .map { it.toDomain() }
            emit(Resource.Success(results))
        } catch (e: Exception) {
            val userFriendlyMessage = if (e.message?.contains("404") == true) {
                "Hasil tidak ditemukan"
            } else {
                e.localizedMessage ?: "Search failed"
            }
            emit(Resource.Error(userFriendlyMessage, e))
        }
    }

    override fun discoverByGenre(genreId: Int): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading)
        try {
            val response = api.discoverMoviesByGenre(genreId = genreId)
            val movies = response.results.map { it.toDomain() }
            emit(Resource.Success(movies))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load genre movies", e))
        }
    }
}

