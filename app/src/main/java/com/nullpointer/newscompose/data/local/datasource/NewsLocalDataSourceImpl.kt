package com.nullpointer.newscompose.data.local.datasource

import com.nullpointer.newscompose.data.local.room.NewsDao
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.flow.Flow

class NewsLocalDataSourceImpl(
    private val newsDao: NewsDao,
):NewsLocalDataSource{

    override val listNews: Flow<List<NewsDB>> = newsDao.getAllNews()

    override suspend fun addListNews(listNews: List<NewsDB>) =
        newsDao.addNews(listNews)

    override suspend fun updateAllNews(listNews: List<NewsDB>) =
        newsDao.updateAllNews(listNews)

    override suspend fun getNewFromTitle(title: String) =
        newsDao.getNewFromTitle(title)

    override suspend fun deleterAllNews() =
        newsDao.deleterAllNews()
}