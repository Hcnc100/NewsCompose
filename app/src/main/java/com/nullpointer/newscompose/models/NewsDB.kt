package com.nullpointer.newscompose.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


@Entity(
    tableName = "table_news",
    indices = [Index(value = ["title", "timestamp"], unique = true)]
)
data class NewsDB(
    val title: String,
    val description: String?,
    val author: String?,
    val urlImg: String?,
    var urlNew: String,
    val timestamp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
) {
    companion object {
        fun fromApiNew(newsApi: NewsApi): NewsDB {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("Zulu")
            }
            val date = dateFormat.parse(newsApi.publishedAt)
            val millis = date?.time ?: System.currentTimeMillis()
            return NewsDB(
                title = newsApi.title,
                description = newsApi.description,
                author = newsApi.author,
                urlImg = newsApi.urlToImage,
                urlNew = newsApi.url,
                timestamp = millis
            )
        }
    }
}