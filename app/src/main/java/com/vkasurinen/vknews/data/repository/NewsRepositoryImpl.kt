package com.vkasurinen.vknews.data.repository

import android.util.Log
import com.vkasurinen.vknews.data.local.NewsDao
import com.vkasurinen.vknews.data.local.TopHeadlinesDao
import com.vkasurinen.vknews.data.mappers.toDomainModel
import com.vkasurinen.vknews.data.mappers.toEntity
import com.vkasurinen.vknews.data.mappers.toTopHeadlineEntity
import com.vkasurinen.vknews.data.remote.NewsApi
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.repository.NewsRepository
import com.vkasurinen.vknews.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class NewsRepositoryImpl(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao,
    private val topHeadlinesDao: TopHeadlinesDao
) : NewsRepository {
    override suspend fun getNews(sources: List<String>): Flow<Resource<List<Article>>> {
        return flow {
            emit(Resource.Loading(true))
            val localArticles = newsDao.getArticles().firstOrNull() ?: emptyList()

            val shouldLoadLocalArticles = localArticles.isNotEmpty()

            if (shouldLoadLocalArticles) {
                emit(Resource.Success(
                    data = localArticles.map { articleEntity ->
                        articleEntity.toDomainModel()
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            val articlesFromApi = try {
                newsApi.getNews(sources = sources.joinToString(separator = ","), page = 1)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading news"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading news"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading news"))
                return@flow
            }

            val articleEntities = articlesFromApi.articles.map { articleData ->
                articleData.toEntity()
            }

            newsDao.upsert(articleEntities)

            emit(Resource.Success(
                articleEntities.map { it.toDomainModel() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun searchNews(searchQuery: String, sources: List<String>): Flow<Resource<List<Article>>> {
        return flow {
            emit(Resource.Loading(true))
            val localArticles = newsDao.getArticles().firstOrNull() ?: emptyList()

            val shouldLoadLocalArticles = localArticles.isNotEmpty()

            if (shouldLoadLocalArticles) {
                emit(Resource.Success(
                    data = localArticles.map { articleEntity ->
                        articleEntity.toDomainModel()
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val articlesFromApi = try {
                newsApi.searchNews(searchQuery = searchQuery, sources = sources.joinToString(separator = ","), page = 1)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error searching news"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error searching news"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error searching news"))
                return@flow
            }

            val articleEntities = articlesFromApi.articles.map { articleData ->
                articleData.toEntity()
            }

            newsDao.upsert(articleEntities)

            emit(Resource.Success(
                articleEntities.map { it.toDomainModel() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun upsertArticle(article: Article) {
        Log.d("NewsRepositoryImpl", "Inserting article: $article")
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
                    Log.d("NewsRepositoryImpl", "Retrieved articles from database: $articleEntities")
                    val articles = articleEntities.map { it.toDomainModel() }
                    emit(Resource.Success(articles))
                }
            } catch (e: Exception) {
                Log.e("NewsRepositoryImpl", "Error fetching articles: ${e.message}")
                emit(Resource.Error(e.message ?: "Unknown error"))
            }
        }
    }



    override suspend fun getArticle(url: String): Flow<Resource<Article>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val articleEntity = newsDao.getArticle(url = url)
                if (articleEntity != null) {
                    emit(Resource.Success(articleEntity.toDomainModel()))
                } else {
                    emit(Resource.Error("Article not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error fetching article: ${e.message}"))
            }
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getTopHeadlines(country: String): Flow<Resource<List<Article>>> {
        return flow {
            emit(Resource.Loading(true))
            val localTopHeadlines = topHeadlinesDao.getTopHeadlines().firstOrNull() ?: emptyList()

            val shouldLoadLocalTopHeadlines = localTopHeadlines.isNotEmpty()

            if (shouldLoadLocalTopHeadlines) {
                emit(Resource.Success(
                    data = localTopHeadlines.map { topHeadlineEntity ->
                        topHeadlineEntity.toDomainModel()
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val topHeadlinesFromApi = try {
                newsApi.getTopHeadlines(country = country)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error fetching top headlines"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error fetching top headlines"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error fetching top headlines"))
                return@flow
            }

            val topHeadlineEntities = topHeadlinesFromApi.articles.map { articleData ->
                articleData.toTopHeadlineEntity()
            }

            topHeadlinesDao.upsert(topHeadlineEntities)

            emit(Resource.Success(
                topHeadlineEntities.map { it.toDomainModel() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getTopHeadlineArticle(url: String): Flow<Resource<Article>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val articleEntity = topHeadlinesDao.getArticle(url = url)
                if (articleEntity != null) {
                    emit(Resource.Success(articleEntity.toDomainModel()))
                } else {
                    emit(Resource.Error("Article not found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error fetching article: ${e.message}"))
            }
            emit(Resource.Loading(false))
        }
    }

}