package com.example.jetpack.di

import android.content.Context
import com.example.jetpack.BuildConfig
import com.example.jetpack.core.common.Constants
import com.example.jetpack.core.common.LocaleHelper
import com.example.jetpack.data.remote.AuthInterceptor
import com.example.jetpack.data.remote.TmdbApiService
import com.example.jetpack.data.repository.MovieRepositoryImpl
import com.example.jetpack.domain.repository.MovieRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val movieRepository: MovieRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val authInterceptor by lazy {
        AuthInterceptor(
            token = BuildConfig.TMDB_READ_ACCESS_TOKEN,
            apiKey = BuildConfig.TMDB_API_KEY,
            languageProvider = { LocaleHelper.getTmdbLanguage(context) }
        )
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val tmdbApiService: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }

    override val movieRepository: MovieRepository by lazy {
        MovieRepositoryImpl(tmdbApiService)
    }
}

