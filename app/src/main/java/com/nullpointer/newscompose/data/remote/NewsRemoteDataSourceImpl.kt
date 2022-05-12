package com.nullpointer.newscompose.data.remote

import com.nullpointer.newscompose.core.utils.InternetCheck
import com.nullpointer.newscompose.core.utils.InternetCheckError
import com.nullpointer.newscompose.core.utils.ServerTimeOut
import com.nullpointer.newscompose.domain.ApiException
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.withTimeoutOrNull

class NewsRemoteDataSourceImpl(
    private val newsApiServices: NewsApiServices
):NewsRemoteDataSource {
    override suspend fun getLastNews(country: String, page: Int): List<NewsDB> {
        if(!InternetCheck.isNetworkAvailable()) throw InternetCheckError()
        val apiResponse = withTimeoutOrNull(3_000){
            newsApiServices.topHeadLines(country, page).body()
        }?:throw ServerTimeOut()
        if (apiResponse.status == "error") throw ApiException(apiResponse.code)
        return apiResponse.articles.map(NewsDB::fromApiNew)
    }
}