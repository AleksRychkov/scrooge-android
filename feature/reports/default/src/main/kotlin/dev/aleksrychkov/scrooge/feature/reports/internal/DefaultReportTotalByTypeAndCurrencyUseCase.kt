package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalByTypeAndCurrencyUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
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
                val result = reportDao.value
                    .amountForPeriodByTypeAndCodeEntity(
                        fromTimestamp = fromTimestamp,
                        toTimestamp = toTimestamp,
                    ).let(::calculateTotal)
                ReportTotalByTypeAndCurrencyResult.Success(result)
            }.getOrDefault(ReportTotalByTypeAndCurrencyResult.Failure)
        }

    private fun calculateTotal(
        input: ReportTotalAmountEntity,
    ): ReportTotalAmountEntity {
        val currencies = mutableSetOf<CurrencyEntity>()
        input.income.map { it.currency }.forEach(currencies::add)
        input.expense.map { it.currency }.forEach(currencies::add)

        val income = mutableListOf<ReportTotalAmountEntity.Value>()
        val expense = mutableListOf<ReportTotalAmountEntity.Value>()
        val total = mutableListOf<ReportTotalAmountEntity.Value>()

        for (cur in currencies) {
            val tmpIncome = input.income.findByCurrencyOrNew(cur)
            val tmpExpense = input.expense.findByCurrencyOrNew(cur)
            val tmpTotal = ReportTotalAmountEntity.Value(
                currency = cur,
                amount = tmpIncome.amount - tmpExpense.amount,
            )
            income.add(tmpIncome)
            expense.add(tmpExpense)
            total.add(tmpTotal)
        }

        return ReportTotalAmountEntity(
            income = income.toImmutableList(),
            expense = expense.toImmutableList(),
            total = total.toImmutableList(),
        )
    }

    private fun ImmutableList<ReportTotalAmountEntity.Value>.findByCurrencyOrNew(
        currency: CurrencyEntity,
    ): ReportTotalAmountEntity.Value {
        return this.firstOrNull { it.currency == currency }
            ?: ReportTotalAmountEntity.Value(
                currency = currency,
                amount = 0L,
            )
    }
}
