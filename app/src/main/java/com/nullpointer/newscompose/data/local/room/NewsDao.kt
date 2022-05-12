package com.nullpointer.newscompose.data.local.room

import androidx.room.*
import com.nullpointer.newscompose.models.NewsDB
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNews(list: List<NewsDB>)

    @Query("SELECT * FROM table_news ORDER BY timestamp desc")
    fun getAllNews(): Flow<List<NewsDB>>

    @Transaction
    suspend fun updateAllNews(list: List<NewsDB>){
        deleterAllNews()
        addNews(list)
    }

    @Query("SELECT * FROM table_news WHERE title=:titleNew LIMIT 1")
    suspend fun getNewFromTitle(titleNew: String): NewsDB?

    @Query("DELETE FROM table_news")
    suspend fun deleterAllNews()
}