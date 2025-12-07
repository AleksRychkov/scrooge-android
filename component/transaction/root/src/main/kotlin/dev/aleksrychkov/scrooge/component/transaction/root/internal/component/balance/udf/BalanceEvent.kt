package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant

internal sealed interface BalanceEvent {
    sealed interface External : BalanceEvent {
        data class SetPeriod(val instant: Instant) : External
    }

    sealed interface Internal : BalanceEvent {
        data object FailedToUpdateBalance : Internal
        data class UpdateBalance(
            val income: ImmutableList<BalanceItem> = persistentListOf(),
            val expense: ImmutableList<BalanceItem> = persistentListOf(),
            val total: ImmutableList<BalanceItem> = persistentListOf(),
        ) : Internal
    }
}
