package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity

fun interface ReportTotalAmountMonthlyUseCase {
    suspend operator fun invoke(period: PeriodTimestampEntity): ReportTotalAmountMonthlyResult
}

sealed interface ReportTotalAmountMonthlyResult {
    data class Success(
        val result: ReportTotalAmountMonthlyEntity,
    ) : ReportTotalAmountMonthlyResult

    data object Failure : ReportTotalAmountMonthlyResult
}
