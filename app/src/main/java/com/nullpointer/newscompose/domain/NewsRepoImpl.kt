package com.nullpointer.newscompose.domain

import com.nullpointer.newscompose.data.local.NewsLocalDataSource
import com.nullpointer.newscompose.data.remote.NewsRemoteDataSource
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepoImpl @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {


    override val listNews: Flow<List<NewsDB>> =
        newsLocalDataSource.listNews

    override suspend fun requestNews(country: String): Int {
        val apiResponse = newsRemoteDataSource.getLastNews(country, 1).body()
        if (apiResponse!!.status == "error") throw ApiException(apiResponse.code)
        val listNewsDB = apiResponse.articles.map(NewsDB::fromApiNew)
        // * deleter all news saved
        // * and save the news for request only if the news received is no empty
        if (listNewsDB.isNotEmpty()) newsLocalDataSource.updateAllNews(listNewsDB)
        return listNewsDB.size
    }

    override suspend fun concatenateNews(country: String, intPager: Int): Int {
        val apiResponse = newsRemoteDataSource.getLastNews(country, intPager).body()
        if (apiResponse!!.status == "error") throw ApiException(apiResponse.code)
        val listNewsDB = apiResponse.articles.map(NewsDB::fromApiNew)
        // ? add new news only
        if (listNewsDB.isNotEmpty()) newsLocalDataSource.addListNews(listNewsDB)
        return listNewsDB.size
    }

    override suspend fun getNew(title: String): NewsDB? =
        newsLocalDataSource.getNewFromTitle(title)

    override suspend fun deleterAllNews() =
        newsLocalDataSource.deleterAllNews()
}

