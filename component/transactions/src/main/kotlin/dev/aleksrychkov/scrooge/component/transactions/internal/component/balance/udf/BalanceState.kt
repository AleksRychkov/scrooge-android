package dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class BalanceState(
    val isLoading: Boolean = false,
    val income: ImmutableList<BalanceItem> = persistentListOf(
        BalanceItem(
            currency = CurrencyEntity.RUB,
            value = "123.12 ${CurrencyEntity.RUB.currencySymbol}"
        ),
        BalanceItem(
            currency = CurrencyEntity.EUR,
            value = "222.12 ${CurrencyEntity.EUR.currencySymbol}"
        ),
    ),
    val expense: ImmutableList<BalanceItem> = persistentListOf(
        BalanceItem(
            currency = CurrencyEntity.RUB,
            value = "10.12 ${CurrencyEntity.RUB.currencySymbol}"
        ),
        BalanceItem(
            currency = CurrencyEntity.EUR,
            value = "20.12 ${CurrencyEntity.EUR.currencySymbol}"
        ),
    ),
    val total: ImmutableList<BalanceItem> = persistentListOf(
        BalanceItem(
            currency = CurrencyEntity.RUB,
            value = "113.00 ${CurrencyEntity.RUB.currencySymbol}"
        ),
        BalanceItem(
            currency = CurrencyEntity.EUR,
            value = "200.12 ${CurrencyEntity.EUR.currencySymbol}"
        ),
    ),
)

internal data class BalanceItem(
    val currency: CurrencyEntity,
    val value: String,
)
