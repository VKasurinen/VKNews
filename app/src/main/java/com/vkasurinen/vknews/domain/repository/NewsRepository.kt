package com.vkasurinen.vknews.domain.repository

import androidx.paging.PagingData
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(sources: List<String>): Flow<Resource<List<Article>>>

//    suspend fun searchNews(searchQuery: String, sources: List<String>): Flow<Resource<List<Article>>>

//    suspend fun upsertArticle(article: Article)
//
//    suspend fun deleteArticle(article: Article)
//
//    suspend fun getArticles(): Flow<Resource<List<Article>>>

    suspend fun getArticle(url: String): Flow<Resource<Article>>?

    suspend fun getTopHeadlines(country: String): Flow<Resource<List<Article>>>
    suspend fun getTopHeadlineArticle(url: String): Flow<Resource<Article>>

    suspend fun upsertArticle(article: Article)

    suspend fun getBookmarkedArticles(): Flow<Resource<List<Article>>>
}