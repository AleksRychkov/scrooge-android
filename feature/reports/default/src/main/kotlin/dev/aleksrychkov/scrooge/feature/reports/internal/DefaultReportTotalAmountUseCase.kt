package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReportTotalAmountUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportTotalAmountUseCase {

    override suspend fun invoke(period: PeriodTimestampEntity): ReportTotalAmountResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val result = reportDao.value
                    .totalAmount(period = period)
                    .map(::calculateTotal)
                ReportTotalAmountResult.Success(result)
            }.getOrDefault(ReportTotalAmountResult.Failure)
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
