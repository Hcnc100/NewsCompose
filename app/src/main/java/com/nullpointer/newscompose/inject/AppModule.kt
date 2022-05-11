package com.nullpointer.newscompose.inject

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nullpointer.newscompose.data.local.NewsDao
import com.nullpointer.newscompose.data.local.NewsDatabase
import com.nullpointer.newscompose.data.remote.NewsDataSource
import com.nullpointer.newscompose.domain.NewsRepoImpl
import com.nullpointer.newscompose.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = "https://newsapi.org/v2/"

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("BaseUrl") baseUrl: String,
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl).build()
    }

    @Provides
    @Singleton
    fun provideNewsSource(retrofit: Retrofit): NewsDataSource =
        retrofit.create(NewsDataSource::class.java)


    @Provides
    @Singleton
    fun provideNewsDatabase(
        @ApplicationContext context: Context,
    ): NewsDatabase = Room.databaseBuilder(
        context,
        NewsDatabase::class.java,
        "NEWS_DATABASE"
    ).build()

    @Provides
    @Singleton
    fun provideNewsDao(
        newsDatabase: NewsDatabase,
    ): NewsDao = newsDatabase.getNewDao()


}