package dev.aleksrychkov.scrooge.common.database.internal.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import dev.aleksrychkov.scrooge.common.database.Scrooge
import dev.aleksrychkov.scrooge.common.database.TagDao
import dev.aleksrychkov.scrooge.common.database.internal.mapper.TagMapper
import dev.aleksrychkov.scrooge.common.entity.TagEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultTagDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : TagDao {

    private val database: Scrooge
        get() = db.value

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
        database.tagQueries.delete(id)
    }
}
