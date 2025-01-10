package com.vkasurinen.vknews.data.remote

import com.vkasurinen.vknews.data.remote.dto.ArticleDto
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

    companion object {
        const val BASE_URL = "https://api.rawg.io/api/"
        const val API_KEY = "d189fe14fd79460c98b7cc5b4b6e6307"
    }

}

