package com.nullpointer.newscompose.data.remote

import com.nullpointer.newscompose.core.utils.ExceptionManager
import com.nullpointer.newscompose.core.utils.InternetCheck
import com.nullpointer.newscompose.domain.ApiException
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.withTimeoutOrNull

class NewsRemoteDataSourceImpl(
    private val newsApiServices: NewsApiServices
):NewsRemoteDataSource {

    private suspend fun <T> callApiWithTimeout(
        timeout: Long = 3_000,
        callApi: suspend () -> T,
    ): T {
        if (!InternetCheck.isNetworkAvailable()) throw Exception(ExceptionManager.NO_NETWORK_ERROR)
        return withTimeoutOrNull(timeout) { callApi() }!!
    }

    override suspend fun getLastNews(country: String, page: Int): List<NewsDB> {
        val apiResponse = callApiWithTimeout {
            newsApiServices.topHeadLines(country, page)
        }
        if (apiResponse.status == "error") throw Exception(apiResponse.code)
        return apiResponse.articles.map(NewsDB::fromApiNew)
    }
}