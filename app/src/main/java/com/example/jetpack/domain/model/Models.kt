package com.example.jetpack.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val genreIds: List<Int> = emptyList(),
    val mediaType: String = "movie"
)

data class TvShow(
    val id: Int,
    val name: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val firstAirDate: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val genreIds: List<Int> = emptyList()
)

data class Genre(
    val id: Int,
    val name: String
)

data class Cast(
    val id: Int,
    val name: String,
    val character: String?,
    val profilePath: String?
)

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val tagline: String?,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val runtime: Int?,
    val voteAverage: Double,
    val voteCount: Int,
    val genres: List<Genre>,
    val status: String,
    val casts: List<Cast> = emptyList(),
    val videos: List<Video> = emptyList(),
    val similar: List<Movie> = emptyList()
)

