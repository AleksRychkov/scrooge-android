package dev.aleksrychkov.scrooge.component.transactions.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class TransactionsActor : Actor<TransactionsCommand, TransactionsEvent> {
    override suspend fun process(command: TransactionsCommand): Flow<TransactionsEvent> {
        return when (command) {
            TransactionsCommand.Loading -> {
                flowOf(TransactionsEvent.Internal.Finish)
            }
        }
    }
}
