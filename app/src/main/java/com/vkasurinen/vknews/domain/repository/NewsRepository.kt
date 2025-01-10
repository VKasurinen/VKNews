package com.vkasurinen.vknews.domain.repository

import androidx.paging.PagingData
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(sources: List<String>): Flow<Resource<PagingData<Article>>>

    suspend fun searchNews(searchQuery: String, sources: List<String>): Flow<Resource<PagingData<Article>>>

    suspend fun upsertArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    suspend fun getArticles(): Flow<Resource<List<Article>>>

    suspend fun getArticle(url: String): Article?

}