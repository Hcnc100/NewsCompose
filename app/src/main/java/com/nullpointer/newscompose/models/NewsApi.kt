package com.nullpointer.newscompose.models

import com.google.gson.annotations.SerializedName

data class NewsApi(
    val title: String,
    val description: String?,
    val author: String?,
    val urlToImage: String?,
    val url: String,
    val publishedAt:String
)