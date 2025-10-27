package dev.aleksrychkov.scrooge.core.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.TagDao
import dev.aleksrychkov.scrooge.core.database.internal.mapper.TagMapper
import dev.aleksrychkov.scrooge.core.entity.TagEntity
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
internal class DefaultTagDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : TagDao {

    private companion object {
        const val DELETED_NAME_SUFFIX = "_de1eted"
    }

    private val database: Scrooge
        get() = db.value

    init {
        GlobalScope.launch {
            deleteTagsPermanently()
        }
    }

    override suspend fun get(): Flow<ImmutableList<TagEntity>> = withContext(readDispatcher) {
        database.tagQueries
            .selectAll()
            .asFlow()
            .mapToList(readDispatcher)
            .map { list ->
                list.map(TagMapper::toEntity).toImmutableList()
            }
    }

    override suspend fun getByName(name: String): TagEntity? = withContext(readDispatcher) {
        database.tagQueries
            .selectByName(name)
            .executeAsOneOrNull()
            ?.let(TagMapper::toEntity)
    }

    override suspend fun create(
        name: String,
        colorHex: String?,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.tagQueries.create(
            name = name,
            colorHex = colorHex,
        )
    }

    override suspend fun delete(
        id: Long,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        val tag = database.tagQueries.selectById(id).executeAsOne()
        database.tagQueries.delete(name = tag.name + DELETED_NAME_SUFFIX, id = id)
    }

    override suspend fun restore(id: Long): Unit = withContext(writeDispatcher + NonCancellable) {
        val tag = database.tagQueries.selectById(id).executeAsOne()
        val name = tag.name.replace(DELETED_NAME_SUFFIX, "")
        database.tagQueries.restore(name = name, id = id)
    }

    private suspend fun deleteTagsPermanently(): Unit =
        withContext(writeDispatcher + NonCancellable) {
            runCatching {
                database.tagQueries.cleanup()
            }
        }
}
