package com.vkasurinen.vknews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vkasurinen.vknews.data.local.entities.ArticleEntity
import com.vkasurinen.vknews.data.local.entities.TopHeadlineEntity

@Database(
    entities = [ArticleEntity::class, TopHeadlineEntity::class],
    version = 1
)

abstract class NewsDatabase : RoomDatabase() {

    abstract val newsDao: NewsDao
    abstract val topHeadlinesDao: TopHeadlinesDao
}