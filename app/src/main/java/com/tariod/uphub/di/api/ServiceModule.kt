package com.tariod.uphub.di.api

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.tariod.uphub.BuildConfig
import com.tariod.uphub.data.api.AuthInterceptor
import com.tariod.uphub.data.api.ErrorHandler
import com.tariod.uphub.data.api.github.GithubErrorHandler
import com.tariod.uphub.data.api.github.GithubService
import com.tariod.uphub.data.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }

    @Provides
    @Singleton
    fun provideAuthInterceptor(userPreferences: UserPreferences) = AuthInterceptor(userPreferences)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler = GithubErrorHandler()

    @Provides
    @Singleton
    fun provideRetrofit(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().baseUrl(GithubService.ENDPOINT).client(okhttpClient)
        .addConverterFactory(converterFactory).addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    fun <T> provideService(
        okhttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
        service: Class<T>
    ): T = provideRetrofit(okhttpClient, converterFactory).create(service)

    @Provides
    @Singleton
    fun provideGithubService(okhttpClient: OkHttpClient, converterFactory: GsonConverterFactory) =
        provideService(okhttpClient, converterFactory, GithubService::class.java)
}