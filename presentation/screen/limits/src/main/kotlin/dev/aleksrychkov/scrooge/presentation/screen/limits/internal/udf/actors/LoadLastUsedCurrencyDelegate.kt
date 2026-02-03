package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class LoadLastUsedCurrencyDelegate(
    private val getLastUsedCurrencyUseCase: Lazy<GetLastUsedCurrencyUseCase> = getLazy(),
) {
    suspend operator fun invoke(): Flow<LimitsEvent> =
        when (val result = getLastUsedCurrencyUseCase.value.invoke()) {
            GetLastUsedCurrencyResult.Empty -> emptyFlow()
            is GetLastUsedCurrencyResult.Success -> flowOf(
                LimitsEvent.Internal.CurrencyResult(currency = result.currency)
            )
        }
}
