package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import kotlinx.coroutines.flow.Flow

fun interface ReportTotalAmountUseCase {
    suspend operator fun invoke(
        fromTimestamp: Long,
        toTimestamp: Long,
    ): ReportTotalAmountResult
}

sealed interface ReportTotalAmountResult {
    data class Success(val result: Flow<ReportTotalAmountEntity>) : ReportTotalAmountResult
    data object Failure : ReportTotalAmountResult
}
