package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReportTotalByTypeAndCurrencyUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportTotalByTypeAndCurrencyUseCase {

    override suspend fun invoke(
        fromTimestamp: Long,
        toTimestamp: Long
    ): ReportTotalByTypeAndCurrencyResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val resultFlow = reportDao.value
                    .amountForPeriodByTypeAndCodeEntity(
                        fromTimestamp = fromTimestamp,
                        toTimestamp = toTimestamp,
                    )
                    .map(::calculateTotal)
                ReportTotalByTypeAndCurrencyResult.Success(resultFlow)
            }.getOrDefault(ReportTotalByTypeAndCurrencyResult.Failure)
        }

    private fun calculateTotal(
        input: ReportAmountForPeriodByTypeAndCodeEntity,
    ): ReportAmountForPeriodByTypeAndCodeEntity {
        val currencies = mutableSetOf<CurrencyEntity>()
        input.income.map { it.currency }.forEach(currencies::add)
        input.expense.map { it.currency }.forEach(currencies::add)

        val income = mutableListOf<ReportAmountForPeriodByTypeAndCodeEntity.Value>()
        val expense = mutableListOf<ReportAmountForPeriodByTypeAndCodeEntity.Value>()
        val total = mutableListOf<ReportAmountForPeriodByTypeAndCodeEntity.Value>()

        for (cur in currencies) {
            val tmpIncome = input.income.findByCurrencyOrNew(cur)
            val tmpExpense = input.expense.findByCurrencyOrNew(cur)
            val tmpTotal = ReportAmountForPeriodByTypeAndCodeEntity.Value(
                currency = cur,
                amount = tmpIncome.amount - tmpExpense.amount,
            )
            income.add(tmpIncome)
            expense.add(tmpExpense)
            total.add(tmpTotal)
        }

        return ReportAmountForPeriodByTypeAndCodeEntity(
            income = income.toImmutableList(),
            expense = expense.toImmutableList(),
            total = total.toImmutableList(),
        )
    }

    private fun ImmutableList<ReportAmountForPeriodByTypeAndCodeEntity.Value>.findByCurrencyOrNew(
        currency: CurrencyEntity,
    ): ReportAmountForPeriodByTypeAndCodeEntity.Value {
        return this.firstOrNull { it.currency == currency }
            ?: ReportAmountForPeriodByTypeAndCodeEntity.Value(
                currency = currency,
                amount = 0L,
            )
    }
}
