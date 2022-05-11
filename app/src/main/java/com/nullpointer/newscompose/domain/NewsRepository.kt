package com.nullpointer.newscompose.domain

import com.nullpointer.newscompose.models.NewsApi
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    val listNews:Flow<List<NewsDB>>
    suspend fun requestNews(country:String):Int
    suspend fun concatenateNews(country:String,intPager:Int):Int
    suspend fun getNew(title:String):NewsDB?
    suspend fun deleterAllNews()
}