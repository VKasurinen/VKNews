package com.vkasurinen.vknews.data.remote.dto

import com.vkasurinen.vknews.data.local.entities.ArticleEntity

data class ArticleDto(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleData>
)

data class ArticleData(
    val source: SourceDto,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

data class SourceDto(
    val id: String?,
    val name: String?
)
