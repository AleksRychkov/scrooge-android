package dev.aleksrychkov.scrooge.feature.limits

import dev.aleksrychkov.scrooge.core.entity.LimitEntity

fun interface LimitsCreateUseCase {
    suspend operator fun invoke(entity: LimitEntity): LimitsCreateResult
}

sealed interface LimitsCreateResult {
    data object Success : LimitsCreateResult
    data object Failure : LimitsCreateResult
}
