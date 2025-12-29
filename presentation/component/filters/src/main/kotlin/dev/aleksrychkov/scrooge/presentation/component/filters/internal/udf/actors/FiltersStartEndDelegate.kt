package dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.startEndOfYear
import dev.aleksrychkov.scrooge.feature.transaction.GetMinMaxTimestampUseCase
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersCommand
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FiltersStartEndDelegate(
    private val useCase: Lazy<GetMinMaxTimestampUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: FiltersCommand.GetFiltersStartEndYears): Flow<FiltersEvent> {
        var period = useCase.value.invoke()
        if (period == null) {
            val currentYear = Datestamp.now().date.year
            period = startEndOfYear(currentYear)
        }
        val fromDate = period.from.date
        val toDate = period.to.date
        return flowOf(
            FiltersEvent.Internal.SetMinMaxYearsPeriod(
                startYear = fromDate.year,
                endYear = toDate.year,
            )
        )
    }
}
