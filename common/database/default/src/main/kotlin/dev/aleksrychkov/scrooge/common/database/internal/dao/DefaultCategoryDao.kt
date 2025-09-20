package dev.aleksrychkov.scrooge.common.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.common.database.CategoryDao
import dev.aleksrychkov.scrooge.common.database.Scrooge
import dev.aleksrychkov.scrooge.common.database.internal.mapper.CategoryMapper
import dev.aleksrychkov.scrooge.common.entity.CategoryEntity
import dev.aleksrychkov.scrooge.common.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultCategoryDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : CategoryDao {

    private val database: Scrooge
        get() = db.value

    override suspend fun get(
        type: TransactionType,
    ): Flow<ImmutableList<CategoryEntity>> = withContext(readDispatcher) {
        database.categoryQueries
            .selectAllByType(type.type.toLong())
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                list.map(CategoryMapper::toEntity).toImmutableList()
            }
    }

    override suspend fun getByName(
        name: String,
        type: TransactionType
    ): CategoryEntity? = withContext(readDispatcher) {
        database.categoryQueries
            .getByNameAndType(
                name = name,
                type = type.type.toLong(),
            )
            .executeAsOneOrNull()
            ?.let(CategoryMapper::toEntity)
    }

    override suspend fun create(
        name: String,
        type: TransactionType
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.categoryQueries.create(
            name = name,
            type = type.type.toLong(),
        )
    }

    override suspend fun delete(
        id: Long,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.categoryQueries.delete(id)
    }
}
