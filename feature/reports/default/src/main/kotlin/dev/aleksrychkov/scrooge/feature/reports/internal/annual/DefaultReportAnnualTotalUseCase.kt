package dev.aleksrychkov.scrooge.feature.reports.internal.annual

import dev.aleksrychkov.scrooge.core.database.TransactionDao
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.annual.ReportAnnualTotalResult
import dev.aleksrychkov.scrooge.feature.reports.annual.ReportAnnualTotalUseCase
import dev.aleksrychkov.scrooge.feature.reports.annual.entity.AnnualTotalReportEntity
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal class DefaultReportAnnualTotalUseCase(
    private val transactionDao: Lazy<TransactionDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportAnnualTotalUseCase {

    private val timeZone: TimeZone
        get() = TimeZone.currentSystemDefault()

    @Suppress("LongMethod", "MagicNumber")
    override suspend fun invoke(
        year: Int,
    ): ReportAnnualTotalResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val (from, to) = getYearStartEndTimestamp(year)
                val allData = transactionDao.value.get(from, to).firstOrNull()
                    ?: return@runSuspendCatching ReportAnnualTotalResult.Empty
                if (allData.isEmpty()) return@runSuspendCatching ReportAnnualTotalResult.Empty

                val income = mutableMapOf<Int, MutableMap<CurrencyEntity, Long>>()
                val expense = mutableMapOf<Int, MutableMap<CurrencyEntity, Long>>()
                val total = mutableMapOf<Int, MutableMap<CurrencyEntity, Long>>()

                val allCurrencies = mutableSetOf<CurrencyEntity>()

                allData.forEach { transaction ->
                    val month = transaction.timestamp.getMonth()
                    val currency = transaction.currency
                    val type = transaction.type
                    allCurrencies.add(currency)
                    val map = when (type) {
                        TransactionType.Income -> income
                        TransactionType.Expense -> expense
                    }
                    val innerMap = map.getOrDefault(month, mutableMapOf())
                    val amount = innerMap.getOrDefault(currency, 0L)
                    innerMap[currency] = amount + transaction.amount
                    map[month] = innerMap

                    val innerTotal = total.getOrDefault(month, mutableMapOf())
                    val totalAmount = innerTotal.getOrDefault(currency, 0L)
                    when (type) {
                        TransactionType.Income -> innerTotal[currency] =
                            totalAmount + transaction.amount

                        TransactionType.Expense -> innerTotal[currency] =
                            totalAmount - transaction.amount
                    }
                    total[month] = innerTotal
                }

                val byMonthResult = mutableListOf<AnnualTotalReportEntity.ByMonthTotal>()

                for (month in 1..12) {
                    val incomeTotal = income
                        .getOrDefault(month, mutableMapOf())
                        .map { (key, value) ->
                            AnnualTotalReportEntity.Total(
                                currency = key,
                                total = value,
                            )
                        }
                        .toImmutableList()
                    val expenseTotal = expense
                        .getOrDefault(month, mutableMapOf())
                        .map { (key, value) ->
                            AnnualTotalReportEntity.Total(
                                currency = key,
                                total = value,
                            )
                        }
                        .toImmutableList()
                    val totalTotal = total
                        .getOrDefault(month, mutableMapOf())
                        .map { (key, value) ->
                            AnnualTotalReportEntity.Total(
                                currency = key,
                                total = value,
                            )
                        }
                        .toImmutableList()

                    AnnualTotalReportEntity.ByMonthTotal(
                        monthNumber = month,
                        income = incomeTotal,
                        expense = expenseTotal,
                        total = totalTotal,
                    )
                        .let(byMonthResult::add)
                }

                val tmpTotal = mutableMapOf<CurrencyEntity, Long>()
                total.values.forEach { map ->
                    map.forEach { (key, value) ->
                        val tmpAmount = tmpTotal.getOrDefault(key, 0L)
                        tmpTotal[key] = tmpAmount + value
                    }
                }
                val totalResult = tmpTotal.map { (key, value) ->
                    AnnualTotalReportEntity.Total(
                        currency = key,
                        total = value,
                    )
                }

                ReportAnnualTotalResult.Success(
                    AnnualTotalReportEntity(
                        totalMonth = byMonthResult.toImmutableList(),
                        total = totalResult.toImmutableList(),
                        allCurrencies = allCurrencies.toImmutableList(),
                    )
                )
            }.getOrDefault(ReportAnnualTotalResult.Failure)
        }

    private fun getYearStartEndTimestamp(year: Int): Pair<Long, Long> {
        val startMillis = LocalDateTime(year, 1, 1, 0, 0)
            .toInstant(timeZone)
            .toEpochMilliseconds()

        val endMillis = LocalDateTime(year + 1, 1, 1, 0, 0)
            .toInstant(timeZone)
            .minus(1, DateTimeUnit.MILLISECOND)
            .toEpochMilliseconds()

        return startMillis to endMillis
    }

    private fun Long.getMonth(): Int =
        Instant
            .fromEpochMilliseconds(this)
            .toLocalDateTime(timeZone)
            .month
            .number
}
