package dev.aleksrychkov.scrooge.component.transactionslist.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class TransactionsListState(
    val isLoading: Boolean = false,
    val transactions: ImmutableList<TransactionsGroup> = persistentListOf(),
)

@Immutable
internal data class TransactionsGroup(
    val date: String,
    val transactions: ImmutableList<TransactionEntity>,
)
