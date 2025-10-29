package dev.aleksrychkov.scrooge.feature.currency.internal

import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase

internal class DefaultGetLastUsedCurrencyUseCase : GetLastUsedCurrencyUseCase {
    override suspend fun invoke(): GetLastUsedCurrencyResult {
        return GetLastUsedCurrencyResult.Failure
    }
}
