package com.vkasurinen.vknews.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
data class ArticleEntity(
    @PrimaryKey val url: String,
    val author: String,
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String,
    val sourceId: String,
    val sourceName: String,
    val isBookmarked: Boolean = false
)