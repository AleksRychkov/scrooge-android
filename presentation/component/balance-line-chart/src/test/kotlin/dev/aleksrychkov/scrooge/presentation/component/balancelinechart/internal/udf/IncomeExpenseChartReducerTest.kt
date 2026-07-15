package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportIncomeExpenseTimelineEntity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class IncomeExpenseChartReducerTest {

    private val reducer = IncomeExpenseChartReducer()

    @Test
    fun `When filter is set Then loading starts for the new filter`() {
        // Given
        val filter = FilterEntity()

        // When
        val result = reducer.reduce(
            event = IncomeExpenseChartEvent.External.SetFilter(filter),
            state = IncomeExpenseChartState(content = IncomeExpenseChartState.Content.Failure),
        )

        // Then
        assertEquals(filter, result.state.filter)
        assertEquals(IncomeExpenseChartState.Content.Loading, result.state.content)
        assertEquals(listOf(IncomeExpenseChartCommand.Load(filter)), result.commands)
    }

    @Test
    fun `When timeline is loaded Then income and expense are mapped to major units`() {
        // Given
        val timeline = ReportIncomeExpenseTimelineEntity(
            points = persistentListOf(
                ReportIncomeExpenseTimelineEntity.Point(
                    month = LocalDate(2026, 1, 1),
                    income = 12_050,
                    expense = 3_025,
                ),
            ),
        )

        // When
        val content = timeline.toContent()

        // Then
        val data = assertInstanceOf(IncomeExpenseChartState.Content.Data::class.java, content)
        assertEquals(listOf("Jan 2026"), data.labels)
        assertEquals(listOf(120.5), data.income)
        assertEquals(listOf(30.25), data.expense)
    }

    @Test
    fun `When empty timeline is loaded Then empty content is shown`() {
        // Given
        val event = IncomeExpenseChartEvent.Internal.Loaded(
            ReportIncomeExpenseTimelineEntity(),
        )

        // When
        val result = reducer.reduce(event, IncomeExpenseChartState())

        // Then
        assertEquals(IncomeExpenseChartState.Content.Empty, result.state.content)
    }
}
