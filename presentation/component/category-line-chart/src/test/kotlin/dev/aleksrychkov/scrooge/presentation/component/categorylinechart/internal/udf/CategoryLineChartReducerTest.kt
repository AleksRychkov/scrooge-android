package dev.aleksrychkov.scrooge.presentation.component.categorylinechart.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportCategoryTimelineEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class CategoryLineChartReducerTest {

    private val reducer = CategoryLineChartReducer()

    @Test
    fun `When filter is set Then loading starts for the new filter`() {
        // Given
        val filter = FilterEntity()

        // When
        val result = reducer.reduce(
            CategoryLineChartEvent.External.SetFilter(filter),
            CategoryLineChartState(content = CategoryLineChartState.Content.Failure),
        )

        // Then
        assertEquals(CategoryLineChartState.Content.Loading, result.state.content)
        assertEquals(listOf(CategoryLineChartCommand.Load(filter)), result.commands)
    }

    @Test
    fun `When retry is requested Then current filter is loaded again`() {
        // Given
        val filter = FilterEntity()
        val state = CategoryLineChartState(filter, CategoryLineChartState.Content.Failure)

        // When
        val result = reducer.reduce(CategoryLineChartEvent.External.Retry, state)

        // Then
        assertEquals(CategoryLineChartState.Content.Loading, result.state.content)
        assertEquals(listOf(CategoryLineChartCommand.Load(filter)), result.commands)
    }

    @Test
    fun `When category timeline is empty Then empty content is shown`() {
        // Given
        val event = CategoryLineChartEvent.Internal.Loaded(ReportCategoryTimelineEntity())

        // When
        val result = reducer.reduce(event, CategoryLineChartState())

        // Then
        assertEquals(CategoryLineChartState.Content.Empty, result.state.content)
    }

    @Test
    fun `When categories change Then series and zero filled gaps are mapped`() {
        // Given
        val food = category(id = 1, name = "Food", color = 1)
        val taxi = category(id = 2, name = "Taxi", color = 2)
        val timeline = ReportCategoryTimelineEntity(
            series = persistentListOf(
                series(food, 10, 0),
                series(taxi, 0, 20),
            ),
        )

        // When
        val content = timeline.toContent()

        // Then
        val data = assertInstanceOf(CategoryLineChartState.Content.Data::class.java, content)
        assertEquals(listOf("Jan 2026", "Feb 2026"), data.labels)
        assertEquals(listOf("Food", "Taxi"), data.series.map { it.name })
        assertEquals(listOf(0.1, 0.0), data.series.first().amounts)
        assertEquals(listOf(0.0, 0.2), data.series.last().amounts)
    }

    private fun category(id: Long, name: String, color: Int) = CategoryEntity(
        id = id,
        name = name,
        type = TransactionType.Expense,
        iconId = CategoryEntity.DEFAULT_ICON_ID,
        color = color,
    )

    private fun series(
        category: CategoryEntity,
        january: Long,
        february: Long,
    ) = ReportCategoryTimelineEntity.CategorySeries(
        category = category,
        points = persistentListOf(
            ReportCategoryTimelineEntity.Point(LocalDate(2026, 1, 1), january),
            ReportCategoryTimelineEntity.Point(LocalDate(2026, 2, 1), february),
        ),
    )
}
