package dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportCategoryTimelineEntity
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.Switcher
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.feature.reports.ReportCategoryTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportCategoryTimelineUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Immutable
internal data class CategoryLineChartState(
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
            val series: ImmutableList<Series>,
        ) : Content
    }

    @Immutable
    data class Series(
        val name: String,
        val color: Int,
        val amounts: ImmutableList<Double>,
    )
}

internal sealed interface CategoryLineChartEvent {
    sealed interface External : CategoryLineChartEvent {
        data class SetFilter(val filter: FilterEntity) : External
        data object Retry : External
    }

    sealed interface Internal : CategoryLineChartEvent {
        data class Loaded(val result: ReportCategoryTimelineEntity) : Internal
        data object Failed : Internal
    }
}

internal sealed interface CategoryLineChartCommand {
    data class Load(val filter: FilterEntity) : CategoryLineChartCommand
}

internal class CategoryLineChartReducer :
    Reducer<CategoryLineChartState, CategoryLineChartEvent, CategoryLineChartCommand, Unit> {
    override fun reduce(
        event: CategoryLineChartEvent,
        state: CategoryLineChartState,
    ): ReducerResult<CategoryLineChartState, CategoryLineChartCommand, Unit> = when (event) {
        is CategoryLineChartEvent.External.SetFilter -> state.startLoading(event.filter, event)
        CategoryLineChartEvent.External.Retry -> state.startLoading(state.filter, event)
        CategoryLineChartEvent.Internal.Failed -> state.reduceWith(event) {
            state { copy(content = CategoryLineChartState.Content.Failure) }
        }
        is CategoryLineChartEvent.Internal.Loaded -> state.reduceWith(event) {
            state { copy(content = event.result.toContent()) }
        }
    }

    private fun CategoryLineChartState.startLoading(
        nextFilter: FilterEntity,
        event: CategoryLineChartEvent,
    ): ReducerResult<CategoryLineChartState, CategoryLineChartCommand, Unit> = reduceWith(event) {
        state { copy(filter = nextFilter, content = CategoryLineChartState.Content.Loading) }
        command { listOf(CategoryLineChartCommand.Load(nextFilter)) }
    }
}

internal class CategoryLineChartActor(
    private val useCase: Lazy<ReportCategoryTimelineUseCase>,
) : Actor<CategoryLineChartCommand, CategoryLineChartEvent> {
    private val loadSwitcher = Switcher()

    override suspend fun process(command: CategoryLineChartCommand): Flow<CategoryLineChartEvent> {
        val filter = when (command) {
            is CategoryLineChartCommand.Load -> command.filter
        }
        return loadSwitcher.switch {
            when (val result = useCase.value(filter)) {
                ReportCategoryTimelineResult.Failure -> flowOf(CategoryLineChartEvent.Internal.Failed)
                is ReportCategoryTimelineResult.Success ->
                    flowOf(CategoryLineChartEvent.Internal.Loaded(result.result))
            }
        }
    }

    companion object {
        operator fun invoke() = CategoryLineChartActor(getLazy())
    }
}

internal fun ReportCategoryTimelineEntity.toContent(): CategoryLineChartState.Content {
    if (series.isEmpty()) return CategoryLineChartState.Content.Empty
    val labels = series.first().points.map { point ->
        val month = point.month.month.name.take(MONTH_ABBREVIATION_LENGTH)
            .lowercase()
            .replaceFirstChar(Char::uppercase)
        "$month ${point.month.year}"
    }.toImmutableList()
    return CategoryLineChartState.Content.Data(
        labels = labels,
        series = series.map { categorySeries ->
            CategoryLineChartState.Series(
                name = categorySeries.category.name,
                color = categorySeries.category.color,
                amounts = categorySeries.points
                    .map { it.amount / CENTS_IN_MAJOR_UNIT }
                    .toImmutableList(),
            )
        }.toImmutableList(),
    )
}

private const val MONTH_ABBREVIATION_LENGTH = 3
private const val CENTS_IN_MAJOR_UNIT = 100.0
