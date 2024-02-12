package com.asabirov.search.data.di

import com.asabirov.search.BuildConfig
import com.asabirov.search.data.interceptor.ApiKeyInterceptor
import com.asabirov.search.data.remote.GoogleMapsApi
import com.asabirov.search.data.repository.SearchRepositoryImpl
import com.asabirov.search.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchDataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleMapsApi(client: OkHttpClient): GoogleMapsApi {
        return Retrofit.Builder()
            .baseUrl(GoogleMapsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSearchRepository(api: GoogleMapsApi): SearchRepository {
        return SearchRepositoryImpl(api)
    }
}