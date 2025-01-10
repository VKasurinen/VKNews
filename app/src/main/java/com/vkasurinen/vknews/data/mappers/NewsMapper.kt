package com.vkasurinen.vknews.data.mappers

import com.vkasurinen.vknews.data.local.entities.ArticleEntity
import com.vkasurinen.vknews.data.remote.dto.ArticleDto
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.model.Source

fun ArticleDto.toDomainModel(): List<Article> {
    return articles.map {
        Article(
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            source = Source(it.source.id, it.source.name),
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}

fun ArticleEntity.toDomainModel(): Article {
    return Article(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        source = Source(sourceId, sourceName),
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}

fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        author = author,
        content = content,
        description = description,
        publishedAt = publishedAt,
        sourceId = source.id,
        sourceName = source.name,
        title = title,
        url = url,
        urlToImage = urlToImage
    )
}