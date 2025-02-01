package com.vkasurinen.vknews.presentation.bookmark

import com.vkasurinen.vknews.domain.model.Article

data class BookMarkState(
    val isLoading: Boolean = false,
    val article: Article? = null,
    val articles: List<Article> = emptyList()
)