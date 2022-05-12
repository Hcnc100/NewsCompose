package com.nullpointer.newscompose.data.remote

class NewsRemoteDataSource(
    private val newsApiServices: NewsApiServices
) {
    suspend fun getLastNews(country:String,page:Int) = newsApiServices.topHeadLines(country,page)
}