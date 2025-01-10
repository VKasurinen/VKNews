package com.vkasurinen.vknews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vkasurinen.vknews.data.local.entities.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1
)

abstract class NewsDatabase : RoomDatabase() {

    abstract val newsDao: NewsDao

}