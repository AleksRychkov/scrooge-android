package dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Clock
import kotlin.time.Instant

@Immutable
internal data class BalanceState(
    val isLoading: Boolean = false,
    val income: ImmutableList<BalanceItem> = persistentListOf(),
    val expense: ImmutableList<BalanceItem> = persistentListOf(),
    val total: ImmutableList<BalanceItem> = persistentListOf(),
    val period: Instant = Clock.System.now(),
)

internal data class BalanceItem(
    val currency: CurrencyEntity,
    val value: String,
)
