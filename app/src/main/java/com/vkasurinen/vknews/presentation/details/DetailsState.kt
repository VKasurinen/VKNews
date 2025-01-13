package com.vkasurinen.vknews.presentation.details

import com.vkasurinen.vknews.domain.model.Article

data class DetailsState(
    val isLoading: Boolean = false,
    val article: Article? = null,
    val articles: List<Article> = emptyList()
)
