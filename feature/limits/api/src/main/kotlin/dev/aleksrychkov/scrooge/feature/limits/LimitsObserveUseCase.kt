package dev.aleksrychkov.scrooge.feature.limits

import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface LimitsObserveUseCase {
    suspend operator fun invoke(): LimitsObserveResult
}

sealed interface LimitsObserveResult {
    data class Success(val result: Flow<ImmutableList<LimitEntity>>) : LimitsObserveResult
    data object Failure : LimitsObserveResult
}
