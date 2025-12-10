package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.actors

import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalCommand
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalEvent
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadDelegate(
    private val useCase: Lazy<ReportTotalByTypeAndCurrencyUseCase>,
) {
    suspend operator fun invoke(cmd: PeriodTotalCommand.Load): Flow<PeriodTotalEvent> {
        val result = useCase.value.invoke(
            fromTimestamp = cmd.fromTimestamp,
            toTimestamp = cmd.toTimestamp,
        )
        return when (result) {
            ReportTotalByTypeAndCurrencyResult.Failure -> flowOf(PeriodTotalEvent.Internal.LoadFailed)
            is ReportTotalByTypeAndCurrencyResult.Success -> flowOf(
                PeriodTotalEvent.Internal.LoadSuccess(result.result)
            )
        }
    }
}
