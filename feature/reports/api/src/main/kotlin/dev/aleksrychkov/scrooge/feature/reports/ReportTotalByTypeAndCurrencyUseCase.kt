package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity

fun interface ReportTotalByTypeAndCurrencyUseCase {
    suspend operator fun invoke(
        fromTimestamp: Long,
        toTimestamp: Long,
    ): ReportTotalByTypeAndCurrencyResult
}

sealed interface ReportTotalByTypeAndCurrencyResult {
    data class Success(val result: ReportTotalAmountEntity) : ReportTotalByTypeAndCurrencyResult
    data object Failure : ReportTotalByTypeAndCurrencyResult
}
