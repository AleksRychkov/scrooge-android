package dev.aleksrychkov.scrooge.feature.limits

import dev.aleksrychkov.scrooge.core.entity.LimitEntity

fun interface LimitsUpdateUseCase {
    suspend operator fun invoke(entity: LimitEntity): LimitsUpdateResult
}

sealed interface LimitsUpdateResult {
    data object Success : LimitsUpdateResult
    data object Failure : LimitsUpdateResult
}
