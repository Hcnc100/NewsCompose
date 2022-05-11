package com.nullpointer.newscompose.inject

import com.nullpointer.newscompose.domain.NewsRepoImpl
import com.nullpointer.newscompose.domain.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideNewRepository(repoImpl: NewsRepoImpl): NewsRepository
}