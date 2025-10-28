package dev.aleksrychkov.scrooge.feature.currency

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

fun interface GetLastUsedCurrencyUseCase {
    suspend operator fun invoke(): GetLastUsedCurrencyResult
}

sealed interface GetLastUsedCurrencyResult {
    data class Success(val currency: CurrencyEntity) : GetLastUsedCurrencyResult
    data object Failure : GetLastUsedCurrencyResult
}
