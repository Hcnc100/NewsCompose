package com.nullpointer.newscompose.data.local

import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.flow.Flow

class NewsLocalDataSource(
    private val newsDao: NewsDao,
) {

    val listNews: Flow<List<NewsDB>> = newsDao.getAllNews()

    suspend fun addListNews(listNews: List<NewsDB>) =
        newsDao.addNews(listNews)

    suspend fun updateAllNews(listNews: List<NewsDB>) =
        newsDao.updateAllNews(listNews)

    suspend fun getNewFromTitle(title: String) =
        newsDao.getNewFromTitle(title)

    suspend fun deleterAllNews() =
        newsDao.deleterAllNews()
}