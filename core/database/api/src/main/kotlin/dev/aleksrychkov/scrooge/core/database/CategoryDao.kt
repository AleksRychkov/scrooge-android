package dev.aleksrychkov.scrooge.core.database

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface CategoryDao {
    suspend fun get(type: TransactionType): Flow<ImmutableList<CategoryEntity>>
    suspend fun get(id: Long): CategoryEntity?
    suspend fun getByName(name: String, type: TransactionType): CategoryEntity?
    suspend fun create(name: String, type: TransactionType, iconId: String, color: Int)
    suspend fun update(id: Long, name: String, type: TransactionType, iconId: String, color: Int)
    suspend fun delete(id: Long)
    suspend fun updateOrderIndex(list: List<CategoryOrder>)

    class CategoryOrder(val id: Long, val orderIndex: Int)
}
