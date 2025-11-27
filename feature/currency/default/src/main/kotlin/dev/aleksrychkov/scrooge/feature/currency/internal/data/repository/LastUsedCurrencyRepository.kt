package dev.aleksrychkov.scrooge.feature.currency.internal.data.repository

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.internal.data.source.LastUsedCurrencySource

internal interface LastUsedCurrencyRepository {
    companion object {
        operator fun invoke(
            source: Lazy<LastUsedCurrencySource>
        ): LastUsedCurrencyRepository =
            DefaultLastUsedCurrencyRepository(source = source)
    }

    suspend fun getLastUsedCurrency(): CurrencyEntity?
    suspend fun setLastUsedCurrency(currency: CurrencyEntity)
}

private class DefaultLastUsedCurrencyRepository(
    private val source: Lazy<LastUsedCurrencySource>,
) : LastUsedCurrencyRepository {
    override suspend fun getLastUsedCurrency(): CurrencyEntity? =
        source.value.get()

    override suspend fun setLastUsedCurrency(currency: CurrencyEntity) {
        source.value.set(currency = currency)
    }
}
