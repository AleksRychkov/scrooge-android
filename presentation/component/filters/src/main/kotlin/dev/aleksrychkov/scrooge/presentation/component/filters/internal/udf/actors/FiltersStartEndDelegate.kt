package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.startEndOfYear
import dev.aleksrychkov.scrooge.feature.transaction.GetMinMaxTimestampUseCase
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersCommand
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

internal class FiltersStartEndDelegate(
    private val useCase: Lazy<GetMinMaxTimestampUseCase> = getLazy(),
) {
    private val tz: TimeZone by lazy { TimeZone.currentSystemDefault() }

    suspend operator fun invoke(cmd: FiltersCommand.GetFiltersStartEndYears): Flow<FiltersEvent> {
        var period = useCase.value.invoke()
        if (period == null) {
            val currentYear = Clock.System.now().toLocalDateTime(tz).year
            period = startEndOfYear(currentYear)
        }
        val fromDate = Instant.fromEpochMilliseconds(period.from).toLocalDateTime(tz)
        val toDate = Instant.fromEpochMilliseconds(period.to).toLocalDateTime(tz)
        return flowOf(
            FiltersEvent.Internal.SetMinMaxYearsPeriod(
                startYear = fromDate.year,
                endYear = toDate.year,
            )
        )
    }
}
