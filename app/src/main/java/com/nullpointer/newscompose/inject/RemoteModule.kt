package com.nullpointer.newscompose.inject

import com.nullpointer.newscompose.data.remote.NewsApiServices
import com.nullpointer.newscompose.data.remote.NewsRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    private const val BASE_URL = "BaseUrl"

    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl() = "https://newsapi.org/v2/"

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named(BASE_URL) baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl).build()
    }

    @Provides
    @Singleton
    fun provideNewsSource(retrofit: Retrofit): NewsApiServices =
        retrofit.create(NewsApiServices::class.java)

    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(
        newsApiServices: NewsApiServices,
    ): NewsRemoteDataSource = NewsRemoteDataSource(newsApiServices)
}