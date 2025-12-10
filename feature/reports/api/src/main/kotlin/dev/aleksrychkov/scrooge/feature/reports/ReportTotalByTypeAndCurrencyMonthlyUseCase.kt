package dev.aleksrychkov.scrooge.feature.reports

fun interface ReportTotalByTypeAndCurrencyMonthlyUseCase {
    suspend operator fun invoke(
        fromTimestamp: Long,
        toTimestamp: Long,
    )
}

sealed interface ReportTotalByTypeAndCurrencyMonthlyResult
