package dev.aleksrychkov.scrooge.presentation.component.balancelinechart.internal.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class BalanceTotalChartReducerTest {

    private val reducer = BalanceTotalChartReducer()

    @Test
    fun `When filter is set Then loading starts for the new filter`() {
        // Given
        val filter = FilterEntity()

        // When
        val result = reducer.reduce(
            event = BalanceTotalChartEvent.External.SetFilter(filter),
            state = BalanceTotalChartState(content = BalanceTotalChartState.Content.Failure),
        )

        // Then
        assertEquals(filter, result.state.filter)
        assertEquals(BalanceTotalChartState.Content.Loading, result.state.content)
        assertEquals(listOf(BalanceTotalChartCommand.Load(filter)), result.commands)
    }

    @Test
    fun `When retry is requested Then current filter is loaded again`() {
        // Given
        val filter = FilterEntity()
        val state = BalanceTotalChartState(
            filter = filter,
            content = BalanceTotalChartState.Content.Failure,
        )

        // When
        val result = reducer.reduce(BalanceTotalChartEvent.External.Retry, state)

        // Then
        assertEquals(BalanceTotalChartState.Content.Loading, result.state.content)
        assertEquals(listOf(BalanceTotalChartCommand.Load(filter)), result.commands)
    }

    @Test
    fun `When loading fails Then failure is shown`() {
        // Given
        val state = BalanceTotalChartState()

        // When
        val result = reducer.reduce(BalanceTotalChartEvent.Internal.Failed, state)

        // Then
        assertEquals(BalanceTotalChartState.Content.Failure, result.state.content)
    }

    @Test
    fun `When an empty timeline is loaded Then empty content is shown`() {
        // Given
        val event = BalanceTotalChartEvent.Internal.Loaded(ReportBalanceTimelineEntity())

        // When
        val result = reducer.reduce(event, BalanceTotalChartState())

        // Then
        assertEquals(BalanceTotalChartState.Content.Empty, result.state.content)
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
        val data = assertInstanceOf(BalanceTotalChartState.Content.Data::class.java, content)
        assertEquals(listOf("01/26", "02/26"), data.labels)
        assertEquals(listOf(1.2, -0.3), data.amounts)
    }
}
