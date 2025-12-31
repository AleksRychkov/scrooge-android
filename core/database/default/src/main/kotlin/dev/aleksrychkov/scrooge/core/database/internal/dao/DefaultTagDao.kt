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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultTagDao(
    private val db: Lazy<Scrooge>,
    private val readDispatcher: CoroutineDispatcher,
    private val writeDispatcher: CoroutineDispatcher,
) : TagDao {

    private val database: Scrooge by lazy { db.value }

    override suspend fun get(): Flow<ImmutableList<TagEntity>> = withContext(readDispatcher) {
        database.tagQueries
            .selectAll(mapper = TagMapper::toEntity)
            .asFlow()
            .mapToList(readDispatcher)
            .map { list -> list.toImmutableList() }
    }

    override suspend fun getByName(name: String): TagEntity? = withContext(readDispatcher) {
        database.tagQueries
            .selectByName(name = name, mapper = TagMapper::toEntity)
            .executeAsOneOrNull()
    }

    override suspend fun create(
        name: String,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.tagQueries.create(name = name)
    }

    override suspend fun delete(
        id: Long,
    ): Unit = withContext(writeDispatcher + NonCancellable) {
        database.tagQueries.delete(id = id)
    }
}
