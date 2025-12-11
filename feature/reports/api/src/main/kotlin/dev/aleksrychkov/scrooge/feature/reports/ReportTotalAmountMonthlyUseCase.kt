package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity

fun interface ReportTotalAmountMonthlyUseCase {
    suspend operator fun invoke(year: Int): ReportTotalAmountMonthlyResult
}

sealed interface ReportTotalAmountMonthlyResult {
    data class Success(
        val result: ReportTotalAmountMonthlyEntity,
    ) : ReportTotalAmountMonthlyResult

    data object Failure : ReportTotalAmountMonthlyResult
}
