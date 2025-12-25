package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.period

import com.arkivanov.decompose.ComponentContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

internal interface PeriodComponent {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            year: Int,
        ): PeriodComponent = DefaultPeriodComponent(
            componentContext = componentContext,
            year = year,
        )
    }

    val state: PeriodState
}

private class DefaultPeriodComponent(
    componentContext: ComponentContext,
    private val year: Int,
) : PeriodComponent, ComponentContext by componentContext {

    private companion object {
        const val ALL_YEARS_RANGE = 50
    }

    private val currentYear: Int =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    override val state: PeriodState = PeriodState(
        selectedYear = year,
        allYears = (currentYear - ALL_YEARS_RANGE..currentYear + ALL_YEARS_RANGE)
            .map { it }
            .toImmutableList(),
        currentYear = currentYear,
    )
}
