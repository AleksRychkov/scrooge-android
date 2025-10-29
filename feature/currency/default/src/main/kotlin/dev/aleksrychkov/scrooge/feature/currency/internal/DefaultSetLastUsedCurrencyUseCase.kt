package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.feature.currency.SetLastUsedCurrencyUseCase

internal class DefaultSetLastUsedCurrencyUseCase : SetLastUsedCurrencyUseCase {
    override suspend fun invoke(currency: CurrencyEntity) {
        // todo
    }
}
