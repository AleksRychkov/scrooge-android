package dev.aleksrychkov.scrooge.core.udf

import kotlinx.coroutines.flow.Flow

interface Actor<Command : Any, Event : Any> {
    suspend fun process(command: Command): Flow<Event>
}
