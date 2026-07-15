package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class BalanceTotalChartState(
    val filter: FilterEntity = FilterEntity(),
    val content: Content = Content.Loading,
) {
    @Immutable
    sealed interface Content {
        data object Loading : Content
        data object Empty : Content
        data object Failure : Content
        data class Data(
            val labels: ImmutableList<String>,
            val amounts: ImmutableList<Double>,
        ) : Content
    }
}

internal sealed interface BalanceTotalChartEvent {
    sealed interface External : BalanceTotalChartEvent {
        data class SetFilter(val filter: FilterEntity) : External
        data object Retry : External
    }

    sealed interface Internal : BalanceTotalChartEvent {
        data class Loaded(val result: ReportBalanceTimelineEntity) : Internal
        data object Failed : Internal
    }
}

internal sealed interface BalanceTotalChartCommand {
    data class Load(val filter: FilterEntity) : BalanceTotalChartCommand
}

internal class BalanceTotalChartReducer :
    Reducer<BalanceTotalChartState, BalanceTotalChartEvent, BalanceTotalChartCommand, Unit> {
    override fun reduce(
        event: BalanceTotalChartEvent,
        state: BalanceTotalChartState,
    ): ReducerResult<BalanceTotalChartState, BalanceTotalChartCommand, Unit> = when (event) {
        is BalanceTotalChartEvent.External.SetFilter -> state.startLoading(event.filter, event)
        BalanceTotalChartEvent.External.Retry -> state.startLoading(state.filter, event)
        BalanceTotalChartEvent.Internal.Failed -> state.reduceWith(event) {
            state { copy(content = BalanceTotalChartState.Content.Failure) }
        }
        is BalanceTotalChartEvent.Internal.Loaded -> state.reduceWith(event) {
            state {
                copy(content = event.result.toContent())
            }
        }
    }

    private fun BalanceTotalChartState.startLoading(
        nextFilter: FilterEntity,
        event: BalanceTotalChartEvent,
    ): ReducerResult<BalanceTotalChartState, BalanceTotalChartCommand, Unit> = reduceWith(event) {
        state { copy(filter = nextFilter, content = BalanceTotalChartState.Content.Loading) }
        command { listOf(BalanceTotalChartCommand.Load(nextFilter)) }
    }
}

internal fun ReportBalanceTimelineEntity.toContent(): BalanceTotalChartState.Content {
    if (points.isEmpty()) return BalanceTotalChartState.Content.Empty
    return BalanceTotalChartState.Content.Data(
        labels = points.map { point ->
            "${(point.month.month.ordinal + 1).toString().padStart(2, '0')}/" +
                (point.month.year % YEARS_IN_CENTURY).toString().padStart(2, '0')
        }.toImmutableList(),
        amounts = points.map { it.amount / CENTS_IN_MAJOR_UNIT }.toImmutableList(),
    )
}

private const val YEARS_IN_CENTURY = 100
private const val CENTS_IN_MAJOR_UNIT = 100.0
