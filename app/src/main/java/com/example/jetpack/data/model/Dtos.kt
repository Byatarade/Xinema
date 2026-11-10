package com.example.jetpack.data.model

import com.example.jetpack.domain.model.Cast
import com.example.jetpack.domain.model.Genre
import com.example.jetpack.domain.model.Movie
import com.example.jetpack.domain.model.MovieDetail
import com.example.jetpack.domain.model.TvShow
import com.example.jetpack.domain.model.Video
import com.google.gson.annotations.SerializedName

data class MovieResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?, // For multi-search / trending TV
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("media_type") val mediaType: String?
) {
    fun toDomain(): Movie {
        return Movie(
            id = id,
            title = title ?: name ?: "Untitled",
            overview = overview ?: "",
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate ?: firstAirDate,
            voteAverage = voteAverage ?: 0.0,
            voteCount = voteCount ?: 0,
            genreIds = genreIds ?: emptyList(),
            mediaType = mediaType ?: "movie"
        )
    }
}

data class TvResponseDto(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TvShowDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class TvShowDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("genre_ids") val genreIds: List<Int>?
) {
    fun toDomain(): TvShow {
        return TvShow(
            id = id,
            name = name ?: "Untitled Series",
            overview = overview ?: "",
            posterPath = posterPath,
            backdropPath = backdropPath,
            firstAirDate = firstAirDate,
            voteAverage = voteAverage ?: 0.0,
            voteCount = voteCount ?: 0,
            genreIds = genreIds ?: emptyList()
        )
    }
}

data class MovieDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("vote_count") val voteCount: Int?,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("status") val status: String?
) {
    fun toDomain(casts: List<Cast> = emptyList(), videos: List<Video> = emptyList(), similar: List<Movie> = emptyList()): MovieDetail {
        return MovieDetail(
            id = id,
            title = title ?: "Untitled",
            tagline = tagline,
            overview = overview ?: "",
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            runtime = runtime,
            voteAverage = voteAverage ?: 0.0,
            voteCount = voteCount ?: 0,
            genres = genres?.map { it.toDomain() } ?: emptyList(),
            status = status ?: "Unknown",
            casts = casts,
            videos = videos,
            similar = similar
        )
    }
}

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    fun toDomain() = Genre(id = id, name = name)
}

data class CreditsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<CastDto>?
)

data class CastDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("character") val character: String?,
    @SerializedName("profile_path") val profilePath: String?
) {
    fun toDomain() = Cast(id = id, name = name, character = character, profilePath = profilePath)
}

data class VideosDto(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<VideoDto>?
)

data class VideoDto(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String
) {
    fun toDomain() = Video(id = id, key = key, name = name, site = site, type = type)
}

