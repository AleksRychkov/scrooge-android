package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersCommand
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class ResolveCurrencyDelegate(
    private val mostUsedCurrencyUseCase: Lazy<GetMostUsedCurrencyUseCase> = getLazy(),
    private val lastUsedCurrencyUseCase: Lazy<GetLastUsedCurrencyUseCase> = getLazy(),
) {
    suspend operator fun invoke(command: FiltersCommand.ResolveCurrency): Flow<FiltersEvent> {
        val mostUsed = mostUsedCurrencyUseCase.value.invoke(filter = command.filter)
        val currency = when (mostUsed) {
            is GetMostUsedCurrencyResult.Success -> mostUsed.currency
            GetMostUsedCurrencyResult.Empty -> lastUsedCurrency()
        }
        return flowOf(
            FiltersEvent.Internal.SetAutomaticCurrency(
                filter = command.filter,
                currency = currency,
                submitWhenResolved = command.submitWhenResolved,
            )
        )
    }

    private suspend fun lastUsedCurrency(): CurrencyEntity {
        return when (val lastUsed = lastUsedCurrencyUseCase.value.invoke()) {
            is GetLastUsedCurrencyResult.Success -> lastUsed.currency
            GetLastUsedCurrencyResult.Empty -> CurrencyEntity.RUB
        }
    }
}
