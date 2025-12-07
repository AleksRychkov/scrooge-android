package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.AmountForPeriodByTypeAndCode
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.ReportAmountForPeriodByTypeAndCodeEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.toImmutableList

internal object ReportMapper {
    fun amountForPeriodByTypeAndCodeToEntity(
        list: List<AmountForPeriodByTypeAndCode>,
    ): ReportAmountForPeriodByTypeAndCodeEntity {
        val income = mutableListOf<ReportAmountForPeriodByTypeAndCodeEntity.Value>()
        val expense = mutableListOf<ReportAmountForPeriodByTypeAndCodeEntity.Value>()

        list.forEach { obj ->
            val type = TransactionType.from(obj.type.toInt())
            val currency = CurrencyEntity.fromCurrencyCode(obj.currencyCode)!!
            val amount = obj.sum ?: 0

            val value = ReportAmountForPeriodByTypeAndCodeEntity.Value(
                currency = currency,
                amount = amount,
            )
            when (type) {
                TransactionType.Income -> income
                TransactionType.Expense -> expense
            }.add(value)
        }

        return ReportAmountForPeriodByTypeAndCodeEntity(
            income = income.toImmutableList(),
            expense = expense.toImmutableList(),
        )
    }
}
