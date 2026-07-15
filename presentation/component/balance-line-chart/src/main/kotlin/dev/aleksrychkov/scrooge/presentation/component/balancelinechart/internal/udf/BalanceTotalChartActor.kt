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
) : Actor<BalanceTotalChartCommand, BalanceTotalChartEvent> {
    private val loadSwitcher = Switcher()

    override suspend fun process(command: BalanceTotalChartCommand): Flow<BalanceTotalChartEvent> {
        val filter = when (command) {
            is BalanceTotalChartCommand.Load -> command.filter
        }
        return loadSwitcher.switch {
            when (val result = useCase.value(filter)) {
                ReportBalanceTotalTimelineResult.Failure ->
                    flowOf(BalanceTotalChartEvent.Internal.Failed)
                is ReportBalanceTotalTimelineResult.Success ->
                    flowOf(BalanceTotalChartEvent.Internal.Loaded(result.result))
            }
        }
    }

    companion object {
        operator fun invoke() = BalanceTotalChartActor(getLazy())
    }
}
