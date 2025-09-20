package dev.aleksrychkov.scrooge.common.database

import dev.aleksrychkov.scrooge.common.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface TagDao {
    suspend fun get(): Flow<ImmutableList<TagEntity>>
    suspend fun create(name: String, colorHex: String?)
    suspend fun delete(id: Long)
}
