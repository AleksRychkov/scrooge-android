package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountMonthlyResult
import dev.aleksrychkov.scrooge.feature.reports.ReportTotalAmountMonthlyUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportTotalAmountMonthlyUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportTotalAmountMonthlyUseCase {
    override suspend fun invoke(
        year: Int,
    ): ReportTotalAmountMonthlyResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val res = reportDao.value.totalAmountMonthly(year)
                val resWithTotal = ReportTotalAmountMonthlyEntity(
                    january = res.january?.let(::calculateTotal),
                    february = res.february?.let(::calculateTotal),
                    march = res.march?.let(::calculateTotal),
                    april = res.april?.let(::calculateTotal),
                    may = res.may?.let(::calculateTotal),
                    june = res.june?.let(::calculateTotal),
                    july = res.july?.let(::calculateTotal),
                    august = res.august?.let(::calculateTotal),
                    september = res.september?.let(::calculateTotal),
                    october = res.october?.let(::calculateTotal),
                    november = res.november?.let(::calculateTotal),
                    december = res.december?.let(::calculateTotal),
                )
                ReportTotalAmountMonthlyResult.Success(resWithTotal)
            }.getOrDefault(ReportTotalAmountMonthlyResult.Failure)
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
