package com.example.jetpack.domain.repository

import com.example.jetpack.core.common.Resource
import com.example.jetpack.domain.model.Movie
import com.example.jetpack.domain.model.MovieDetail
import com.example.jetpack.domain.model.TvShow
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<Resource<List<Movie>>>
    fun getNowPlayingMovies(): Flow<Resource<List<Movie>>>
    fun getPopularMovies(): Flow<Resource<List<Movie>>>
    fun getTopRatedMovies(): Flow<Resource<List<Movie>>>
    fun getPopularTvShows(): Flow<Resource<List<TvShow>>>
    fun getMovieDetail(movieId: Int, mediaType: String = "movie"): Flow<Resource<MovieDetail>>
    fun search(query: String): Flow<Resource<List<Movie>>>
    fun discoverByGenre(genreId: Int): Flow<Resource<List<Movie>>>
}

