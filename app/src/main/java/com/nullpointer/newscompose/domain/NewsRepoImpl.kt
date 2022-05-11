package com.nullpointer.newscompose.domain

import com.nullpointer.newscompose.data.local.NewsDao
import com.nullpointer.newscompose.data.remote.NewsDataSource
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(
    private val newsDataSource: NewsDataSource,
    private val newsDao: NewsDao,
) : NewsRepository {


    override val listNews: Flow<List<NewsDB>> =
        newsDao.getAllNews()

    override suspend fun requestNews(country: String): Int {
        val apiResponse = newsDataSource.topHeadLines(country, 1).body()
        if (apiResponse!!.status == "error") throw ApiException(apiResponse.code)
        val listNewsDB = apiResponse.articles.map(NewsDB::fromApiNew)
        // * deleter all news saved
        // * and save the news for request only if the news received is no empty
        if (listNewsDB.isNotEmpty()) newsDao.updateAllNews(listNewsDB)
        return listNewsDB.size
    }

    override suspend fun concatenateNews(country: String, intPager: Int): Int {
        val apiResponse = newsDataSource.topHeadLines(country, intPager).body()
        if (apiResponse!!.status == "error") throw ApiException(apiResponse.code)
        val listNewsDB = apiResponse.articles.map(NewsDB::fromApiNew)
        // ? add new news only
        if (listNewsDB.isNotEmpty()) newsDao.addNews(listNewsDB)
        return listNewsDB.size
    }

    override suspend fun getNew(title: String): NewsDB? =
        newsDao.getNewFromTitle(title)

    override suspend fun deleterAllNews() =
        newsDao.deleterAllNews()
}

