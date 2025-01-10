package com.vkasurinen.vknews.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vkasurinen.vknews.data.local.entities.ArticleEntity
import com.vkasurinen.vknews.data.mappers.toEntity

class NewsPagingSource(
    private val newsApi: NewsApi,
    private val sources: String
) : PagingSource<Int, ArticleEntity>() {


    override fun getRefreshKey(state: PagingState<Int, ArticleEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        val page = params.key ?: 1
        return try {
            val newsResponse = newsApi.getNews(sources = sources, page = page)
            totalNewsCount += newsResponse.articles.size
            val articles = newsResponse.articles.distinctBy { it.title } //Remove duplicates
                .map { it.toEntity() }

            LoadResult.Page(
                data = articles,
                nextKey = if (totalNewsCount == newsResponse.totalResults) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(
                throwable = e
            )
        }
    }
}