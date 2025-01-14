package com.vkasurinen.vknews.data.remote

import com.vkasurinen.vknews.data.remote.dto.ArticleDto
import com.vkasurinen.vknews.data.remote.dto.TopHeadlinesDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun getNews(
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): ArticleDto

    @GET("everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("sources") sources: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): ArticleDto

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): TopHeadlinesDto

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = "9d1a1df04e6f4e0889b74c1e323c72ad"
    }

}

