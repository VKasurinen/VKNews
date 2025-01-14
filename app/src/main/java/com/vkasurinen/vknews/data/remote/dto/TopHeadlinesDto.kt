package com.vkasurinen.vknews.data.remote.dto

data class TopHeadlinesDto(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleData>
)