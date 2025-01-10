package com.vkasurinen.vknews.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.vkasurinen.vknews.data.local.entities.SourceEntity

@ProvidedTypeConverter
class NewsTypeConvertor {

    @TypeConverter
    fun sourceToString(source: SourceEntity): String{
        return "${source.id},${source.name}"
    }

    @TypeConverter
    fun stringToSource(source: String): SourceEntity{
        return source.split(',').let { sourceArray ->
            SourceEntity(id = sourceArray[0], name = sourceArray[1])
        }
    }
}