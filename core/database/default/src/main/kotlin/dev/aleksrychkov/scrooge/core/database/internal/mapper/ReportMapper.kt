package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.AmountForPeriodByTypeAndCode
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.toImmutableList

internal object ReportMapper {
    fun amountForPeriodByTypeAndCodeToEntity(
        list: List<AmountForPeriodByTypeAndCode>,
    ): ReportTotalAmountEntity {
        val income = mutableListOf<ReportTotalAmountEntity.Value>()
        val expense = mutableListOf<ReportTotalAmountEntity.Value>()

        list.forEach { obj ->
            val type = TransactionType.from(obj.type.toInt())
            val currency = CurrencyEntity.fromCurrencyCode(obj.currencyCode)!!
            val amount = obj.sum ?: 0

            val value = ReportTotalAmountEntity.Value(
                currency = currency,
                amount = amount,
            )
            when (type) {
                TransactionType.Income -> income
                TransactionType.Expense -> expense
            }.add(value)
        }

        return ReportTotalAmountEntity(
            income = income.toImmutableList(),
            expense = expense.toImmutableList(),
        )
    }
}
