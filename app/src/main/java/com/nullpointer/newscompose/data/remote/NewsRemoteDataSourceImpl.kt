package com.nullpointer.newscompose.data.remote

import com.nullpointer.newscompose.core.utils.ExceptionManager
import com.nullpointer.newscompose.core.utils.InternetCheck
import com.nullpointer.newscompose.models.NewsApi
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.*

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
        return apiResponse.articles.map { it.fromApiNew() }
    }

    private fun NewsApi.fromApiNew(): NewsDB {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("Zulu")
        }
        val date = dateFormat.parse(publishedAt)
        val millis = date?.time ?: System.currentTimeMillis()
        return NewsDB(
            urlNew = url,
            title = title,
            author = author,
            timestamp = millis,
            urlImg = urlToImage,
            description = description
        )

    }
}