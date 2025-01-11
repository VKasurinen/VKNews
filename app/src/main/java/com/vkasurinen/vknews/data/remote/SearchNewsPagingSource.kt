package com.vkasurinen.vknews.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vkasurinen.vknews.data.local.entities.ArticleEntity
import com.vkasurinen.vknews.data.mappers.toEntity

class SearchNewsPagingSource(
    private val api: NewsApi,
    private val searchQuery: String,
    private val sources: String
) : PagingSource<Int, ArticleEntity>() {

    override fun getRefreshKey(state: PagingState<Int, ArticleEntity>): Int? {
        return state.anchorPosition?.let { anchorPage ->
            val page = state.closestPageToPosition(anchorPage)
            page?.nextKey?.minus(1) ?: page?.prevKey?.plus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        val page = params.key ?: 1
        return try {
            val newsResponse = api.searchNews(searchQuery = searchQuery, sources = sources, page = page)
            totalNewsCount += newsResponse.articles.size
            val articles = newsResponse.articles.distinctBy { it.title } // Remove duplicates
                .map {
                    Log.d("SearchNewsPagingSource", "Mapping article data to entity: $it")
                    it.toEntity()
                }

            Log.d("SearchNewsPagingSource", "Loaded articles: $articles")

            LoadResult.Page(
                data = articles,
                nextKey = if (totalNewsCount == newsResponse.totalResults) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}