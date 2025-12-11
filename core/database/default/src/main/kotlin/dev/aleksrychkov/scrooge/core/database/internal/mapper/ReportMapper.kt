package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.TotalAmount
import dev.aleksrychkov.scrooge.core.database.TotalAmountMothly
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import kotlinx.datetime.number

internal object ReportMapper {
    fun totalAmountToEntity(
        list: List<TotalAmount>,
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

    fun totalAmountMonthlyToEntity(
        list: List<TotalAmountMothly>
    ): ReportTotalAmountMonthlyEntity {
        val byMonth: Map<Int, List<TotalAmountMothly>> =
            list.groupBy { it.ym.toInt() }

        fun build(month: Int): ReportTotalAmountEntity? {
            val data = byMonth[month] ?: return null
            val mapped = data.map {
                TotalAmount(
                    type = it.type,
                    currencyCode = it.currencyCode,
                    sum = it.total_amount
                )
            }
            return totalAmountToEntity(mapped)
        }

        return ReportTotalAmountMonthlyEntity(
            january = build(Month.JANUARY.number),
            february = build(Month.FEBRUARY.number),
            march = build(Month.MARCH.number),
            april = build(Month.APRIL.number),
            may = build(Month.MAY.number),
            june = build(Month.JUNE.number),
            july = build(Month.JULY.number),
            august = build(Month.AUGUST.number),
            september = build(Month.SEPTEMBER.number),
            october = build(Month.OCTOBER.number),
            november = build(Month.NOVEMBER.number),
            december = build(Month.DECEMBER.number),
        )
    }
}
