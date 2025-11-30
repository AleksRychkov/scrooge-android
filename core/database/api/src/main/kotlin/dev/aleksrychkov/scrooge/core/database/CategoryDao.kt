package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface CategoryDao {
    suspend fun get(type: TransactionType): Flow<ImmutableList<CategoryEntity>>
    suspend fun getByName(name: String, type: TransactionType): CategoryEntity?
    suspend fun create(
        name: String,
        type: TransactionType,
        iconId: String,
        isUserMade: Boolean = true,
    )

    suspend fun delete(id: Long)
    suspend fun restore(id: Long)
}
