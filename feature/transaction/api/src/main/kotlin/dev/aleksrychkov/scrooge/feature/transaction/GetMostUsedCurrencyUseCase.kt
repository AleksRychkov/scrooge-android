package dev.aleksrychkov.scrooge.feature.transaction

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity

fun interface GetMostUsedCurrencyUseCase {
    suspend operator fun invoke(filter: FilterEntity): GetMostUsedCurrencyResult
}

sealed interface GetMostUsedCurrencyResult {
    data class Success(val currency: CurrencyEntity) : GetMostUsedCurrencyResult
    data object Empty : GetMostUsedCurrencyResult
}
