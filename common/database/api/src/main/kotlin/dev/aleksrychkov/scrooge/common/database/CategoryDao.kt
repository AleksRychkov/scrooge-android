package dev.aleksrychkov.scrooge.common.database

import dev.aleksrychkov.scrooge.common.entity.CategoryEntity
import dev.aleksrychkov.scrooge.common.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface CategoryDao {
    suspend fun get(type: TransactionType): Flow<ImmutableList<CategoryEntity>>
    suspend fun getByName(name: String, type: TransactionType): CategoryEntity?
    suspend fun create(name: String, type: TransactionType)
    suspend fun delete(id: Long)
}
