package com.vkasurinen.vknews.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vkasurinen.vknews.data.local.entities.TopHeadlineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopHeadlinesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(topHeadline: TopHeadlineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(topHeadlines: List<TopHeadlineEntity>)

    @Query("SELECT * FROM TopHeadlineEntity")
    fun getTopHeadlines(): Flow<List<TopHeadlineEntity>>
}
