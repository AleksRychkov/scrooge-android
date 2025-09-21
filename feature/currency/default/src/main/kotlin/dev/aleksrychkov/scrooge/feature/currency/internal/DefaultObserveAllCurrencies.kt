package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.common.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.currency.ObserveAllCurrencies
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class DefaultObserveAllCurrencies : ObserveAllCurrencies {
    override suspend fun invoke(): Result<Flow<ImmutableList<CurrencyEntity>>> =
        runSuspendCatching {
            flowOf(CurrencyEntity.entries.toImmutableList())
        }
}
