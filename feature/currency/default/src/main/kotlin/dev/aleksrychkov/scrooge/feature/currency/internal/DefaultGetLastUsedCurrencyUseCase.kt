package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.LastUsedCurrencyRepository

internal class DefaultGetLastUsedCurrencyUseCase(
    private val repository: Lazy<LastUsedCurrencyRepository>,
) : GetLastUsedCurrencyUseCase {
    override suspend fun invoke(): GetLastUsedCurrencyResult =
        runSuspendCatching {
            val currency = repository.value.getLastUsedCurrency()
            if (currency == null) {
                GetLastUsedCurrencyResult.Empty
            } else {
                GetLastUsedCurrencyResult.Success(currency = currency)
            }
        }.getOrDefault(GetLastUsedCurrencyResult.Empty)
}
