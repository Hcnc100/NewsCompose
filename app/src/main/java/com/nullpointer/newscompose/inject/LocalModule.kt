package com.nullpointer.newscompose.inject

import android.content.Context
import androidx.room.Room
import com.nullpointer.newscompose.data.local.datasource.NewsLocalDataSource
import com.nullpointer.newscompose.data.local.room.NewsDao
import com.nullpointer.newscompose.data.local.room.NewsDatabase
import com.nullpointer.newscompose.data.local.datasource.NewsLocalDataSourceImpl
import com.nullpointer.newscompose.data.remote.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

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

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(
        newsDao: NewsDao
    ): NewsLocalDataSource = NewsLocalDataSourceImpl(newsDao)

}