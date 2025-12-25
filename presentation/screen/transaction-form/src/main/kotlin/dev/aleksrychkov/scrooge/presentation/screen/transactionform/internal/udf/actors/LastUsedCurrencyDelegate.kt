package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.SetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf.FormEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class LastUsedCurrencyDelegate(
    private val getLastUsedCurrency: Lazy<GetLastUsedCurrencyUseCase>,
    private val setLastUsedCurrency: Lazy<SetLastUsedCurrencyUseCase>,
    private val defaultCurrency: CurrencyEntity = CurrencyEntity.RUB,
) {

    suspend fun get(): Flow<FormEvent> {
        val result = getLastUsedCurrency.value.invoke()
        val currency = if (result is GetLastUsedCurrencyResult.Success) {
            result.currency
        } else {
            defaultCurrency
        }
        return flowOf(FormEvent.Internal.LastUsedCurrency(currency = currency))
    }

    suspend fun set(cmd: FormCommand.SetLastUsedCurrency): Flow<FormEvent> {
        setLastUsedCurrency.value.invoke(currency = cmd.currency)
        return emptyFlow()
    }
}
