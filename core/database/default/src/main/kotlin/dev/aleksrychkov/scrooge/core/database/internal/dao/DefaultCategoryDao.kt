package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.internal.mapper.CategoryMapper
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
internal class DefaultCategoryDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : CategoryDao {

    private companion object {
        const val DELETED_NAME_SUFFIX = "_de1eted"
    }

    private val database: Scrooge
        get() = db.value

    init {
        GlobalScope.launch {
            deleteCategoriesPermanently()
        }
    }

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
        type: TransactionType,
        iconId: String,
        color: Int,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.categoryQueries.create(
            name = name,
            type = type.type.toLong(),
            iconId = iconId,
            color = color.toLong(),
        )
    }

    override suspend fun delete(
        id: Long,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        val category = database.categoryQueries.getById(id).executeAsOne()
        database.categoryQueries.delete(name = category.name + DELETED_NAME_SUFFIX, id = id)
    }

    override suspend fun restore(id: Long): Unit = withContext(writeDispatcher + NonCancellable) {
        val category = database.categoryQueries.getById(id).executeAsOne()
        val name = category.name.replace(DELETED_NAME_SUFFIX, "")
        database.categoryQueries.restore(name = name, id = id)
    }

    private suspend fun deleteCategoriesPermanently(): Unit =
        withContext(writeDispatcher + NonCancellable) {
            runCatching {
                database.categoryQueries.cleanup()
            }
        }
}
