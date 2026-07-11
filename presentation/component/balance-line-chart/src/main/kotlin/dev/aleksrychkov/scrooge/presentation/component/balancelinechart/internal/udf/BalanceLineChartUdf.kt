package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.Switcher
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTimelineUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Immutable
internal data class BalanceLineChartState(
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
            val amounts: ImmutableList<Long>,
        ) : Content
    }
}

internal sealed interface BalanceLineChartEvent {
    sealed interface External : BalanceLineChartEvent {
        data class SetFilter(val filter: FilterEntity) : External
        data object Retry : External
    }

    sealed interface Internal : BalanceLineChartEvent {
        data class Loaded(val result: ReportBalanceTimelineEntity) : Internal
        data object Failed : Internal
    }
}

internal sealed interface BalanceLineChartCommand {
    data class Load(val filter: FilterEntity) : BalanceLineChartCommand
}

internal class BalanceLineChartReducer :
    Reducer<BalanceLineChartState, BalanceLineChartEvent, BalanceLineChartCommand, Unit> {
    override fun reduce(
        event: BalanceLineChartEvent,
        state: BalanceLineChartState,
    ): ReducerResult<BalanceLineChartState, BalanceLineChartCommand, Unit> = when (event) {
        is BalanceLineChartEvent.External.SetFilter -> state.startLoading(event.filter, event)
        BalanceLineChartEvent.External.Retry -> state.startLoading(state.filter, event)
        BalanceLineChartEvent.Internal.Failed -> state.reduceWith(event) {
            state { copy(content = BalanceLineChartState.Content.Failure) }
        }
        is BalanceLineChartEvent.Internal.Loaded -> state.reduceWith(event) {
            state {
                copy(content = event.result.toContent())
            }
        }
    }

    private fun BalanceLineChartState.startLoading(
        nextFilter: FilterEntity,
        event: BalanceLineChartEvent,
    ): ReducerResult<BalanceLineChartState, BalanceLineChartCommand, Unit> = reduceWith(event) {
        state { copy(filter = nextFilter, content = BalanceLineChartState.Content.Loading) }
        command { listOf(BalanceLineChartCommand.Load(nextFilter)) }
    }
}

internal class BalanceLineChartActor(
    private val useCase: Lazy<ReportBalanceTimelineUseCase>,
) : Actor<BalanceLineChartCommand, BalanceLineChartEvent> {
    private val loadSwitcher = Switcher()

    override suspend fun process(command: BalanceLineChartCommand): Flow<BalanceLineChartEvent> {
        val filter = when (command) {
            is BalanceLineChartCommand.Load -> command.filter
        }
        return loadSwitcher.switch {
            when (val result = useCase.value(filter)) {
                ReportBalanceTimelineResult.Failure -> flowOf(BalanceLineChartEvent.Internal.Failed)
                is ReportBalanceTimelineResult.Success ->
                    flowOf(BalanceLineChartEvent.Internal.Loaded(result.result))
            }
        }
    }

    companion object {
        operator fun invoke() = BalanceLineChartActor(getLazy())
    }
}

internal fun ReportBalanceTimelineEntity.toContent(): BalanceLineChartState.Content {
    if (points.isEmpty()) return BalanceLineChartState.Content.Empty
    return BalanceLineChartState.Content.Data(
        labels = points.map { point ->
            "${point.month.month.name.take(
                MONTH_ABBREVIATION_LENGTH
            ).lowercase().replaceFirstChar(Char::uppercase)} ${point.month.year}"
        }.toImmutableList(),
        amounts = points.map { it.amount }.toImmutableList(),
    )
}

private const val MONTH_ABBREVIATION_LENGTH = 3
