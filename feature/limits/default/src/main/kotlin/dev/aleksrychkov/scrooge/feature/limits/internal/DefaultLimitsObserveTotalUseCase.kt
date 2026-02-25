package dev.aleksrychkov.scrooge.feature.limits.internal

import dev.aleksrychkov.scrooge.core.database.LimitsDao
import dev.aleksrychkov.scrooge.core.entity.Datestamp
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
import kotlinx.datetime.minus
import kotlinx.datetime.plus

internal class DefaultLimitsObserveTotalUseCase(
    private val dao: Lazy<LimitsDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : LimitsObserveTotalUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun invoke(): LimitsObserveTotalResult = withContext(ioDispatcher) {
        runSuspendCatching {
            LimitsObserveTotalResult.Success(observeLimitsData())
        }.getOrDefault(LimitsObserveTotalResult.Failure)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun observeLimitsData(): Flow<ImmutableList<LimitDataEntity>> =
        dao.value.observeActualLimits()
            .flatMapLatest { limits -> loadLimitsData(limits) }

    private suspend fun loadLimitsData(
        limits: List<LimitEntity>
    ): Flow<ImmutableList<LimitDataEntity>> {
        if (limits.isEmpty()) return flowOf(persistentListOf())

        val limitFlows: List<Flow<LimitDataEntity?>> = limits.map { limit ->
            val (from, to) = limit.period.toTimestampPeriod()
            dao.value.observeLimitData(limit, from, to)
        }

        return combine(limitFlows) { results ->
            results.filterNotNull()
                .filter { it.spentAmount > 0 }
                .toImmutableList()
        }
    }

    private fun LimitEntity.Period.toTimestampPeriod(): Pair<Long, Long> = when (this) {
        LimitEntity.Period.Daily -> {
            val today = Datestamp.now()
            today.value to today.value
        }

        LimitEntity.Period.Weekly -> {
            val today = Datestamp.now().date
            val daysFromMonday = today.dayOfWeek.ordinal

            val startOfWeek = today.minus(daysFromMonday, DateTimeUnit.DAY)
            val endOfWeek = startOfWeek.plus(DayOfWeek.SUNDAY.ordinal, DateTimeUnit.DAY)

            Datestamp.from(startOfWeek).value to Datestamp.from(endOfWeek).value
        }

        LimitEntity.Period.Monthly -> {
            val today = Datestamp.now().date
            val monthPeriod = startEndOfMonth(today.month, today.year)

            monthPeriod.from.value to monthPeriod.to.value
        }
    }
}
