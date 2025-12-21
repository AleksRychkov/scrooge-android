package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.actors

import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalCommand
import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.PeriodTotalEvent
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class LoadDelegate(
    private val useCase: Lazy<ReportTotalAmountUseCase>,
) {
    suspend operator fun invoke(cmd: PeriodTotalCommand.Load): Flow<PeriodTotalEvent> {
        val result = useCase.value.invoke(
            fromTimestamp = cmd.fromTimestamp,
            toTimestamp = cmd.toTimestamp,
        )
        return when (result) {
            ReportTotalAmountResult.Failure ->
                flowOf(PeriodTotalEvent.Internal.LoadFailed)

            is ReportTotalAmountResult.Success -> result.result.map {
                PeriodTotalEvent.Internal.LoadSuccess(it)
            }
        }
    }
}
