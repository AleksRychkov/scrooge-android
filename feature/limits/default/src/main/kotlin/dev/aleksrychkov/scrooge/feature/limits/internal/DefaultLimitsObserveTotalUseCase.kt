package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.LimitDataEntity
import dev.aleksrychkov.scrooge.core.entity.LimitEntity
import dev.aleksrychkov.scrooge.core.entity.startEndOfMonth
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveTotalResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveTotalUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

internal class DefaultLimitsObserveTotalUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsObserveTotalUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun invoke(filter: FilterEntity): LimitsObserveTotalResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                LimitsObserveTotalResult.Success(observeLimitsData(filter = filter))
            }.getOrDefault(LimitsObserveTotalResult.Failure)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun observeLimitsData(
        filter: FilterEntity,
    ): Flow<ImmutableList<LimitDataEntity>> =
        dao.value
            .observeActualLimits()
            .flatMapLatest { limits ->
                loadLimitsData(limits = limits, filter = filter)
            }

    private suspend fun loadLimitsData(
        limits: List<LimitEntity>,
        filter: FilterEntity,
    ): Flow<ImmutableList<LimitDataEntity>> {
        if (limits.isEmpty() || !filter.isSingleMonth()) return flowOf(persistentListOf())
        val isCurrentMonth = filter.isCurrentMonth()

        val limitFlows: List<Flow<LimitDataEntity?>> = limits
            .filter { limit ->
                isCurrentMonth || limit.period == LimitEntity.Period.Monthly
            }
            .map { limit ->
                val (from, to) = limit.period.toTimestampPeriod(filter)
                dao.value.observeLimitData(limit, from, to)
            }

        return combine(limitFlows) { results ->
            results.filterNotNull()
                .filter { it.spentAmount > 0 }
                .sortedBy { it.limit.period.ordinal }
                .toImmutableList()
        }
    }

    private fun LimitEntity.Period.toTimestampPeriod(
        filter: FilterEntity
    ): Pair<Long, Long> {
        val filterDate = filter.period.from.date
        return when (this) {
            LimitEntity.Period.Daily -> {
                val today = Datestamp.now().date
                    .let {
                        LocalDate(year = filterDate.year, month = filterDate.month, day = it.day)
                    }
                    .let(Datestamp::from)
                today.value to today.value
            }

            LimitEntity.Period.Weekly -> {
                val today = Datestamp.now().date
                    .let {
                        LocalDate(year = filterDate.year, month = filterDate.month, day = it.day)
                    }
                val daysFromMonday = today.dayOfWeek.ordinal

                val startOfWeek = today.minus(daysFromMonday, DateTimeUnit.DAY)
                val endOfWeek = startOfWeek.plus(DayOfWeek.SUNDAY.ordinal, DateTimeUnit.DAY)

                Datestamp.from(startOfWeek).value to Datestamp.from(endOfWeek).value
            }

            LimitEntity.Period.Monthly -> {
                val monthPeriod = startEndOfMonth(filterDate.month, filterDate.year)
                monthPeriod.from.value to monthPeriod.to.value
            }
        }
    }

    private fun FilterEntity.isSingleMonth(): Boolean {
        val (from, to) = this.period.let { it.from.date to it.to.date }
        return from.year == to.year && from.month == to.month
    }

    private fun FilterEntity.isCurrentMonth(): Boolean {
        val today = Datestamp.now().date
        val filterDate = this.period.from.date
        return filterDate.year == today.year && filterDate.month == today.month
    }
}
