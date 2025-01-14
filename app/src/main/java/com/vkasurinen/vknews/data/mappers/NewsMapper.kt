package com.vkasurinen.vknews.data.mappers

import com.vkasurinen.vknews.data.local.entities.ArticleEntity
import com.vkasurinen.vknews.data.local.entities.TopHeadlineEntity
import com.vkasurinen.vknews.data.remote.dto.ArticleData
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.model.Source

fun ArticleData.toEntity(): ArticleEntity {
    return ArticleEntity(
        author = author ?: "",
        content = content ?: "",
        description = description ?: "",
        publishedAt = publishedAt ?: "",
        sourceId = source.id ?: "",
        sourceName = source.name ?: "",
        title = title ?: "",
        url = url,
        urlToImage = urlToImage ?: ""
    )
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

fun TopHeadlineEntity.toDomainModel(): Article {
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

fun ArticleData.toTopHeadlineEntity(): TopHeadlineEntity {
    return TopHeadlineEntity(
        author = author ?: "",
        content = content ?: "",
        description = description ?: "",
        publishedAt = publishedAt ?: "",
        sourceId = source.id ?: "",
        sourceName = source.name ?: "",
        title = title ?: "",
        url = url,
        urlToImage = urlToImage ?: ""
    )
}

