package com.nullpointer.newscompose.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


@Entity(
    tableName = "table_news",
)
data class NewsDB(
    val title: String,
    val description: String?,
    val author: String?,
    val urlImg: String?,
    var urlNew: String,
    val timestamp: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
)