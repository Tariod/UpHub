package com.tariod.uphub.data.api

import com.tariod.uphub.data.preferences.UserPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val userPrefs: UserPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(
        chain.request()
            .newBuilder().apply {
                userPrefs.getCurrentToken().takeIf { it.isNotBlank() }
                    ?.let { token -> addHeader("Authorization", "Bearer $token") }
            }.build()
    )
}