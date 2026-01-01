package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountMonthlyResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountMonthlyUseCase
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.TotalMonthlyCommand
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.TotalMonthlyEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadDelegate(
    private val useCase: Lazy<ReportTotalAmountMonthlyUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: TotalMonthlyCommand.Load): Flow<TotalMonthlyEvent> {
        return when (val result = useCase.value(filter = cmd.filter)) {
            ReportTotalAmountMonthlyResult.Failure ->
                flowOf(TotalMonthlyEvent.Internal.FailedToLoad)

            is ReportTotalAmountMonthlyResult.Success ->
                flowOf(TotalMonthlyEvent.Internal.Success(result.result))
        }
    }
}
