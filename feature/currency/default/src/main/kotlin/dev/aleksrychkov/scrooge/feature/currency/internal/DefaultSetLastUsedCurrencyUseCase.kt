package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.currency.SetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.currency.internal.data.repository.LastUsedCurrencyRepository

internal class DefaultSetLastUsedCurrencyUseCase(
    val repository: Lazy<LastUsedCurrencyRepository>
) : SetLastUsedCurrencyUseCase {
    override suspend fun invoke(currency: CurrencyEntity) {
        runSuspendCatching {
            repository.value.setLastUsedCurrency(currency)
        }
    }
}
