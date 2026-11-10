package com.example.jetpack.core.common

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

    const val POSTER_SIZE_W342 = "w342"
    const val POSTER_SIZE_W500 = "w500"
    const val BACKDROP_SIZE_W780 = "w780"
    const val BACKDROP_SIZE_ORIGINAL = "original"
    const val PROFILE_SIZE_W185 = "w185"

    fun posterUrl(path: String?, size: String = POSTER_SIZE_W500): String? {
        return if (!path.isNullOrBlank()) "$IMAGE_BASE_URL$size$path" else null
    }

    fun backdropUrl(path: String?, size: String = BACKDROP_SIZE_W780): String? {
        return if (!path.isNullOrBlank()) "$IMAGE_BASE_URL$size$path" else null
    }

    fun profileUrl(path: String?, size: String = PROFILE_SIZE_W185): String? {
        return if (!path.isNullOrBlank()) "$IMAGE_BASE_URL$size$path" else null
    }

    fun youtubeWatchUrl(key: String): String = "https://www.youtube.com/watch?v=$key"
    fun youtubeThumbnailUrl(key: String): String = "https://img.youtube.com/vi/$key/hqdefault.jpg"
}

