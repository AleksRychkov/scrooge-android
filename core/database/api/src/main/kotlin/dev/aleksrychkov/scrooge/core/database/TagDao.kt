package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface TagDao {
    suspend fun get(): Flow<ImmutableList<TagEntity>>
    suspend fun getByName(name: String): TagEntity?
    suspend fun create(name: String, colorHex: String?)
    suspend fun delete(id: Long)
    suspend fun restore(id: Long)
}
