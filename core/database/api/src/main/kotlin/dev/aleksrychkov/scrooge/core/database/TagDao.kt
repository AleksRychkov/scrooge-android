package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.Flow

interface TagDao {
    companion object {
        const val TAG_DELIMITER = "!"
    }

    suspend fun get(): Flow<ImmutableSet<TagEntity>>
    suspend fun getByName(name: String): TagEntity?
    suspend fun create(name: String)
    suspend fun delete(id: Long)
}
