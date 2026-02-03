package dev.aleksrychkov.scrooge.feature.limits

import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import kotlinx.collections.immutable.ImmutableList

fun interface LimitsGetUseCase {
    suspend operator fun invoke(): LimitsGetResult
}

sealed interface LimitsGetResult {
    data class Success(val result: ImmutableList<LimitEntity>) : LimitsGetResult
    data object Failure : LimitsGetResult
}
