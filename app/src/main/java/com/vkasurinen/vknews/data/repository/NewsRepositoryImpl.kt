package com.vkasurinen.vknews.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.vkasurinen.vknews.data.local.NewsDao
import com.vkasurinen.vknews.data.local.NewsDatabase
import com.vkasurinen.vknews.data.mappers.toDomainModel
import com.vkasurinen.vknews.data.mappers.toEntity
import com.vkasurinen.vknews.data.remote.NewsApi
import com.vkasurinen.vknews.data.remote.NewsPagingSource
import com.vkasurinen.vknews.data.remote.SearchNewsPagingSource
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class NewsRepositoryImpl(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) : NewsRepository {
    override suspend fun getNews(sources: List<String>): Flow<Resource<PagingData<Article>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val pagingDataFlow = Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        NewsPagingSource(
                            newsApi = newsApi,
                            sources = sources.joinToString(separator = ",")
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.map { articleEntity ->
                        articleEntity.toDomainModel()
                    }
                }
                pagingDataFlow.collect { pagingData ->
                    emit(Resource.Success(pagingData))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun searchNews(searchQuery: String, sources: List<String>): Flow<Resource<PagingData<Article>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val pagingDataFlow = Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        SearchNewsPagingSource(
                            api = newsApi,
                            searchQuery = searchQuery,
                            sources = sources.joinToString(separator = ",")
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.map { articleEntity ->
                        articleEntity.toDomainModel()
                    }
                }
                pagingDataFlow.collect { pagingData ->
                    emit(Resource.Success(pagingData))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun upsertArticle(article: Article) {
        newsDao.upsert(article.toEntity())
    }

    override suspend fun deleteArticle(article: Article) {
        newsDao.delete(article.toEntity())
    }

    override suspend fun getArticles(): Flow<Resource<List<Article>>> {
        return flow {
            emit(Resource.Loading())
            try {
                newsDao.getArticles().collect { articleEntities ->
                    val articles = articleEntities.map { articleEntity ->
                        articleEntity.toDomainModel()
                    }
                    emit(Resource.Success(articles))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun getArticle(url: String): Article? {
        return newsDao.getArticle(url = url)?.toDomainModel()
    }
}