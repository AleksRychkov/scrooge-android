package dev.aleksrychkov.scrooge.component.transactions.internal.udf

import androidx.compose.runtime.Immutable

@Immutable
internal data class TransactionsState(
    val isLoading: Boolean = false,
)
