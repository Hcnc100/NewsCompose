package com.nullpointer.newscompose.models

data class NewApiResponse(
    val status:String,
    val code:String,
    val articles:List<NewsApi>
)
