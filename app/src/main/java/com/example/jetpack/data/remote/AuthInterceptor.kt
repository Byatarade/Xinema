package com.example.jetpack.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val token: String,
    private val apiKey: String,
    private val languageProvider: () -> String = { "en-US" }
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val urlBuilder = originalUrl.newBuilder()

        if (apiKey.isNotBlank() && originalUrl.queryParameter("api_key") == null) {
            urlBuilder.addQueryParameter("api_key", apiKey)
        }

        // Dynamically add language parameter if not explicitly specified
        if (originalUrl.queryParameter("language") == null) {
            urlBuilder.addQueryParameter("language", languageProvider())
        }

        val requestBuilder = originalRequest.newBuilder()
            .url(urlBuilder.build())
            .addHeader("Accept", "application/json")

        // Add Bearer token header if available
        if (token.isNotBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}

