package com.nullpointer.newscompose.data.remote

import com.nullpointer.newscompose.models.NewsDB

interface NewsRemoteDataSource {
    suspend fun getLastNews(country: String,page:Int):List<NewsDB>
}