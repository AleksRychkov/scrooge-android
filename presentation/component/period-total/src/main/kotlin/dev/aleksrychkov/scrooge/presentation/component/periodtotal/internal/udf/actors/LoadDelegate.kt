package dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.actors

import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountUseCase
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalCommand
import dev.aleksrychkov.scrooge.presentation.component.periodtotal.internal.udf.PeriodTotalEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class LoadDelegate(
    private val useCase: Lazy<ReportTotalAmountUseCase>,
) {
    suspend operator fun invoke(cmd: PeriodTotalCommand.Load): Flow<PeriodTotalEvent> {
        return when (val result = useCase.value.invoke(period = cmd.period)) {
            ReportTotalAmountResult.Failure ->
                flowOf(PeriodTotalEvent.Internal.LoadFailed)

            is ReportTotalAmountResult.Success -> result.result.map {
                PeriodTotalEvent.Internal.LoadSuccess(it)
            }
        }
    }
}
