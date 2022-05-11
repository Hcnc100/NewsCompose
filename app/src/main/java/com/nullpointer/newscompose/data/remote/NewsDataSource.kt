package com.nullpointer.newscompose.data.remote

import com.nullpointer.newscompose.BuildConfig
import com.nullpointer.newscompose.models.NewApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDataSource {
    companion object {
        private const val SIZE_TO_REQUEST = 7
    }

    // * every call to api limit result to 10 results
    // * this for paginate

    // ! https://newsapi.org/docs/endpoints/top-headlines

    // ! save the api key in "local.properties" file
    // * this for no show your api key

    @GET("top-headlines?apiKey=${BuildConfig.API_KEY_NEWS}&pageSize=$SIZE_TO_REQUEST")
    suspend fun topHeadLines(
        @Query("country") country:String,
        @Query("page") page:Int
    ):Response<NewApiResponse>
}