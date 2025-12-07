package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity
import kotlinx.coroutines.flow.Flow

fun interface ReportTotalByTypeAndCurrencyUseCase {
    suspend operator fun invoke(
        fromTimestamp: Long,
        toTimestamp: Long,
    ): ReportTotalByTypeAndCurrencyResult
}

sealed interface ReportTotalByTypeAndCurrencyResult {
    data class Success(val flow: Flow<ReportAmountForPeriodByTypeAndCodeEntity>) :
        ReportTotalByTypeAndCurrencyResult

    data object Failure : ReportTotalByTypeAndCurrencyResult
}
