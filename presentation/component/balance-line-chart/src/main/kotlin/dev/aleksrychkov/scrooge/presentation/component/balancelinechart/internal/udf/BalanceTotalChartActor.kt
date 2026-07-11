package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Switcher
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTotalTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTotalTimelineUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class BalanceTotalChartActor(
    private val useCase: Lazy<ReportBalanceTotalTimelineUseCase>,
) : Actor<BalanceLineChartCommand, BalanceLineChartEvent> {
    private val loadSwitcher = Switcher()

    override suspend fun process(command: BalanceLineChartCommand): Flow<BalanceLineChartEvent> {
        val filter = when (command) {
            is BalanceLineChartCommand.Load -> command.filter
        }
        return loadSwitcher.switch {
            when (val result = useCase.value(filter)) {
                ReportBalanceTotalTimelineResult.Failure ->
                    flowOf(BalanceLineChartEvent.Internal.Failed)
                is ReportBalanceTotalTimelineResult.Success ->
                    flowOf(BalanceLineChartEvent.Internal.Loaded(result.result))
            }
        }
    }

    companion object {
        operator fun invoke() = BalanceTotalChartActor(getLazy())
    }
}
