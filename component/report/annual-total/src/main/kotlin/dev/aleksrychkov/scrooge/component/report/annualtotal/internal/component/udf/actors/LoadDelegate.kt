package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.actors

import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.TotalMonthlyCommand
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.TotalMonthlyEvent
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountMonthlyResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountMonthlyUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadDelegate(
    private val useCase: Lazy<ReportTotalAmountMonthlyUseCase>,
) {
    suspend operator fun invoke(cmd: TotalMonthlyCommand.Load): Flow<TotalMonthlyEvent> {
        return when (val result = useCase.value(year = cmd.year)) {
            ReportTotalAmountMonthlyResult.Failure ->
                flowOf(TotalMonthlyEvent.Internal.FailedToLoad)

            is ReportTotalAmountMonthlyResult.Success ->
                flowOf(TotalMonthlyEvent.Internal.Success(result.result))
        }
    }
}
