package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class BalanceLineChartReducerTest {

    private val reducer = BalanceLineChartReducer()

    @Test
    fun `When filter is set Then loading starts for the new filter`() {
        // Given
        val filter = FilterEntity()

        // When
        val result = reducer.reduce(
            event = BalanceLineChartEvent.External.SetFilter(filter),
            state = BalanceLineChartState(content = BalanceLineChartState.Content.Failure),
        )

        // Then
        assertEquals(filter, result.state.filter)
        assertEquals(BalanceLineChartState.Content.Loading, result.state.content)
        assertEquals(listOf(BalanceLineChartCommand.Load(filter)), result.commands)
    }

    @Test
    fun `When retry is requested Then current filter is loaded again`() {
        // Given
        val filter = FilterEntity()
        val state = BalanceLineChartState(
            filter = filter,
            content = BalanceLineChartState.Content.Failure,
        )

        // When
        val result = reducer.reduce(BalanceLineChartEvent.External.Retry, state)

        // Then
        assertEquals(BalanceLineChartState.Content.Loading, result.state.content)
        assertEquals(listOf(BalanceLineChartCommand.Load(filter)), result.commands)
    }

    @Test
    fun `When loading fails Then failure is shown`() {
        // Given
        val state = BalanceLineChartState()

        // When
        val result = reducer.reduce(BalanceLineChartEvent.Internal.Failed, state)

        // Then
        assertEquals(BalanceLineChartState.Content.Failure, result.state.content)
    }

    @Test
    fun `When an empty timeline is loaded Then empty content is shown`() {
        // Given
        val event = BalanceLineChartEvent.Internal.Loaded(ReportBalanceTimelineEntity())

        // When
        val result = reducer.reduce(event, BalanceLineChartState())

        // Then
        assertEquals(BalanceLineChartState.Content.Empty, result.state.content)
    }

    @Test
    fun `When a timeline is loaded Then points are mapped to chart content`() {
        // Given
        val timeline = ReportBalanceTimelineEntity(
            points = persistentListOf(
                ReportBalanceTimelineEntity.Point(LocalDate(2026, 1, 1), 120),
                ReportBalanceTimelineEntity.Point(LocalDate(2026, 2, 1), -30),
            ),
        )

        // When
        val content = timeline.toContent()

        // Then
        val data = assertInstanceOf(BalanceLineChartState.Content.Data::class.java, content)
        assertEquals(listOf("Jan 2026", "Feb 2026"), data.labels)
        assertEquals(listOf(1.2, -0.3), data.amounts)
    }
}
