package com.vkasurinen.vknews.presentation.homescreen

import com.vkasurinen.vknews.domain.model.Article

data class HomeState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val topHeadlines: List<Article> = emptyList()
)