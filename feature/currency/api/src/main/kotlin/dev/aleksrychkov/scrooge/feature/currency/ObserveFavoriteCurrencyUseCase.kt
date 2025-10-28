package dev.aleksrychkov.scrooge.feature.currency

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface ObserveFavoriteCurrencyUseCase {
    suspend operator fun invoke(): ObserveFavoriteCurrencyResult
}

sealed interface ObserveFavoriteCurrencyResult {
    data class Success(
        val favorite: Flow<ImmutableList<CurrencyEntity>>,
    ) : ObserveFavoriteCurrencyResult

    data object Failure : ObserveFavoriteCurrencyResult
}
