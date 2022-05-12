package com.nullpointer.newscompose.data.local.datasource

import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.flow.Flow

interface NewsLocalDataSource {

    val listNews:Flow<List<NewsDB>>

    suspend fun addListNews(listNews:List<NewsDB>)
    suspend fun updateAllNews(listNews: List<NewsDB>)
    suspend fun getNewFromTitle(title:String):NewsDB?
    suspend fun deleterAllNews()
}