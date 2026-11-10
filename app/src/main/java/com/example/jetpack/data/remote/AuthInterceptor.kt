package com.example.jetpack.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val token: String,
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Build URL with api_key query param as fallback/additional support
        val urlWithApiKey = originalUrl.newBuilder()
            .apply {
                if (apiKey.isNotBlank()) {
                    addQueryParameter("api_key", apiKey)
                }
            }
            .build()

        val requestBuilder = originalRequest.newBuilder()
            .url(urlWithApiKey)
            .addHeader("Accept", "application/json")

        // Add Bearer token header if available
        if (token.isNotBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}

