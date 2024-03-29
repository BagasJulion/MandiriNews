package com.bagasjulion.mandirinews.api


import com.bagasjulion.mandirinews.data.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//const val BASE_URL =  "https://newsapi.org/v2/"
//const val API_KEY = "3957e28cfe7f4102828a8d908d969bef"

interface ApiService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("page") p: Int,
        @Query("apiKey") apiKey: String
    ): Response<News>

    @GET("everything")
    suspend fun getEverything(
        @Query("q") q: String,
        @Query("page") p: Int,
        @Query("apiKey") apiKey: String,
        @Query("sortBy") sort: String
    ): Response<News>

}