package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.database.DatabaseProvider
import dev.aleksrychkov.scrooge.core.database.internal.mapper.CategoryMapper
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultCategoryDao(
    private val dbProvider: DatabaseProvider,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : CategoryDao {

    private val database: Scrooge
        get() = dbProvider.scrooge

    override suspend fun get(
        type: TransactionType,
    ): Flow<ImmutableList<CategoryEntity>> = withContext(readDispatcher) {
        database.categoryQueries
            .selectAllByType(
                type = type.type.toLong(),
                mapper = CategoryMapper::toEntity,
            )
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.toImmutableList() }
    }

    override suspend fun get(id: Long): CategoryEntity? = withContext(readDispatcher) {
        database.categoryQueries
            .getById(
                id = id,
                mapper = CategoryMapper::toEntity,
            )
            .executeAsOneOrNull()
    }

    override suspend fun getByName(
        name: String,
        type: TransactionType
    ): CategoryEntity? = withContext(readDispatcher) {
        database.categoryQueries
            .getByNameAndType(
                name = name,
                type = type.type.toLong(),
                mapper = CategoryMapper::toEntity,
            )
            .executeAsOneOrNull()
    }

    override suspend fun create(
        name: String,
        type: TransactionType,
        iconId: String,
        color: Int,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.categoryQueries.transaction {
            val tType = type.type.toLong()
            val orderIndex = database.categoryQueries
                .lastOrderIndex(tType).executeAsOneOrNull() ?: 0L

            database.categoryQueries.create(
                name = name,
                type = tType,
                iconId = iconId,
                color = color.toLong(),
                orderIndex = orderIndex + 1,
            )
        }
    }

    override suspend fun update(
        id: Long,
        name: String,
        type: TransactionType,
        iconId: String,
        color: Int
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.categoryQueries.update(
            name = name,
            type = type.type.toLong(),
            iconId = iconId,
            color = color.toLong(),
            id = id,
        )
    }

    override suspend fun delete(
        id: Long,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.categoryQueries.transaction {
            val itemToDelete = database.categoryQueries.getById(id = id).executeAsOneOrNull()
            database.categoryQueries.delete(id = id)
            itemToDelete?.let { category ->
                database.categoryQueries.updateIndexingAfterDelete(
                    type = category.type,
                    deletedOrderIndex = category.orderIndex,
                )
            }
        }
    }

    override suspend fun updateOrderIndex(
        list: List<CategoryDao.CategoryOrder>
    ) = withContext(writeDispatcher + NonCancellable) {
        database.categoryQueries.transaction {
            list.forEach {
                database.categoryQueries.setOrderIndex(
                    id = it.id,
                    orderIndex = it.orderIndex.toLong(),
                )
            }
        }
    }
}
