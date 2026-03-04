package dev.aleksrychkov.scrooge.feature.limits

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.LimitDataEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

fun interface LimitsObserveTotalUseCase {
    suspend operator fun invoke(filter: FilterEntity): LimitsObserveTotalResult
}

sealed interface LimitsObserveTotalResult {
    data class Success(val result: Flow<ImmutableList<LimitDataEntity>>) : LimitsObserveTotalResult
    data object Failure : LimitsObserveTotalResult
}
