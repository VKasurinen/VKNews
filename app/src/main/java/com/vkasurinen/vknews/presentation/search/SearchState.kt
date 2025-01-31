package com.vkasurinen.vknews.presentation.search

import com.vkasurinen.vknews.domain.model.Article

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val filteredArticles: List<Article> = emptyList()
)