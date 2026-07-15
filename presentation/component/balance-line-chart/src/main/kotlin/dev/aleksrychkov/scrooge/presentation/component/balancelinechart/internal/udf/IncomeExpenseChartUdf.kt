package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportIncomeExpenseTimelineEntity
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.Switcher
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.feature.reports.ReportIncomeExpenseTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportIncomeExpenseTimelineUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Immutable
internal data class IncomeExpenseChartState(
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
            val income: ImmutableList<Double>,
            val expense: ImmutableList<Double>,
        ) : Content
    }
}

internal sealed interface IncomeExpenseChartEvent {
    sealed interface External : IncomeExpenseChartEvent {
        data class SetFilter(val filter: FilterEntity) : External
        data object Retry : External
    }

    sealed interface Internal : IncomeExpenseChartEvent {
        data class Loaded(val result: ReportIncomeExpenseTimelineEntity) : Internal
        data object Failed : Internal
    }
}

internal sealed interface IncomeExpenseChartCommand {
    data class Load(val filter: FilterEntity) : IncomeExpenseChartCommand
}

internal class IncomeExpenseChartReducer :
    Reducer<IncomeExpenseChartState, IncomeExpenseChartEvent, IncomeExpenseChartCommand, Unit> {
    override fun reduce(
        event: IncomeExpenseChartEvent,
        state: IncomeExpenseChartState,
    ): ReducerResult<IncomeExpenseChartState, IncomeExpenseChartCommand, Unit> = when (event) {
        is IncomeExpenseChartEvent.External.SetFilter -> state.startLoading(event.filter, event)
        IncomeExpenseChartEvent.External.Retry -> state.startLoading(state.filter, event)
        IncomeExpenseChartEvent.Internal.Failed -> state.reduceWith(event) {
            state { copy(content = IncomeExpenseChartState.Content.Failure) }
        }
        is IncomeExpenseChartEvent.Internal.Loaded -> state.reduceWith(event) {
            state { copy(content = event.result.toContent()) }
        }
    }

    private fun IncomeExpenseChartState.startLoading(
        nextFilter: FilterEntity,
        event: IncomeExpenseChartEvent,
    ): ReducerResult<IncomeExpenseChartState, IncomeExpenseChartCommand, Unit> = reduceWith(event) {
        state { copy(filter = nextFilter, content = IncomeExpenseChartState.Content.Loading) }
        command { listOf(IncomeExpenseChartCommand.Load(nextFilter)) }
    }
}

internal class IncomeExpenseChartActor(
    private val useCase: Lazy<ReportIncomeExpenseTimelineUseCase>,
) : Actor<IncomeExpenseChartCommand, IncomeExpenseChartEvent> {
    private val loadSwitcher = Switcher()

    override suspend fun process(command: IncomeExpenseChartCommand): Flow<IncomeExpenseChartEvent> {
        val filter = when (command) {
            is IncomeExpenseChartCommand.Load -> command.filter
        }
        return loadSwitcher.switch {
            when (val result = useCase.value(filter)) {
                ReportIncomeExpenseTimelineResult.Failure ->
                    flowOf(IncomeExpenseChartEvent.Internal.Failed)
                is ReportIncomeExpenseTimelineResult.Success ->
                    flowOf(IncomeExpenseChartEvent.Internal.Loaded(result.result))
            }
        }
    }

    companion object {
        operator fun invoke() = IncomeExpenseChartActor(getLazy())
    }
}

internal fun ReportIncomeExpenseTimelineEntity.toContent(): IncomeExpenseChartState.Content {
    if (points.isEmpty()) return IncomeExpenseChartState.Content.Empty
    return IncomeExpenseChartState.Content.Data(
        labels = points.map { point ->
            "${(point.month.month.ordinal + 1).toString().padStart(2, '0')}/" +
                (point.month.year % YEARS_IN_CENTURY).toString().padStart(2, '0')
        }.toImmutableList(),
        income = points.map { it.income / CENTS_IN_MAJOR_UNIT }.toImmutableList(),
        expense = points.map { it.expense / CENTS_IN_MAJOR_UNIT }.toImmutableList(),
    )
}

private const val YEARS_IN_CENTURY = 100
private const val CENTS_IN_MAJOR_UNIT = 100.0
