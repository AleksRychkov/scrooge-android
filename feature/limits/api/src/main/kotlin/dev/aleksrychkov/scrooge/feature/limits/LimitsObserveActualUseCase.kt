package dev.aleksrychkov.scrooge.feature.limits

import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface LimitsObserveActualUseCase {
    suspend operator fun invoke(): LimitsObserveActualResult
}

sealed interface LimitsObserveActualResult {
    data class Success(val limits: Flow<ImmutableList<LimitEntity>>) : LimitsObserveActualResult
    data object Failure : LimitsObserveActualResult
}
