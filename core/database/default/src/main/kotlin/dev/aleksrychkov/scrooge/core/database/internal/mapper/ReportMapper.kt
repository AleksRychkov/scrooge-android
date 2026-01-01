package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.ByCategory
import dev.aleksrychkov.scrooge.core.database.TotalAmount
import dev.aleksrychkov.scrooge.core.database.TotalAmountMothly
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate
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
        fun build(
            month: Int,
            byMonth: Map<Int, List<TotalAmountMothly>>,
        ): ReportTotalAmountEntity? {
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

        return list
            .groupBy { it.year.toInt() }
            .map { (year, list) ->
                val byMonth = list.groupBy { it.month.toInt() }
                Month.entries
                    .map { month -> month to build(month.number, byMonth) }
                    .filter { it.second != null }
                    .associate { (month, entity) -> LocalDate(year, month, 1) to entity!! }
            }
            .flatMap { map -> map.entries }
            .associate { entry -> entry.key to entry.value }
            .toImmutableMap()
            .let(::ReportTotalAmountMonthlyEntity)
    }

    fun byCategoryToEntity(
        list: List<ByCategory>
    ): ReportByCategoryEntity {
        val income = mutableListOf<ReportByCategoryEntity.ByCurrency>()
        val expense = mutableListOf<ReportByCategoryEntity.ByCurrency>()
        fun ReportByCategoryEntity.ByCurrency.addByType(type: Long) {
            when (type.toInt()) {
                TransactionType.Income.type -> {
                    income.add(this)
                }

                TransactionType.Expense.type -> {
                    expense.add(this)
                }
            }
        }
        list
            .groupBy { it.type }
            .forEach { (type, typedList) ->
                typedList
                    .groupBy { it.currencyCode }
                    .forEach { (currencyCode, byCurrencyList) ->
                        byCurrencyList
                            .toReportByCategoryEntityByCurrency(currencyCode)
                            .addByType(type)
                    }
            }

        return ReportByCategoryEntity(
            income = income.toImmutableList(),
            expense = expense.toImmutableList(),
        )
    }

    private fun List<ByCategory>.toReportByCategoryEntityByCurrency(
        currencyCode: String,
    ): ReportByCategoryEntity.ByCurrency {
        return ReportByCategoryEntity.ByCurrency(
            currency = CurrencyEntity.fromCurrencyCode(currencyCode)!!,
            data = this.map { it.toReportByCategoryEntityValue() }.toImmutableList(),
        )
    }

    private fun ByCategory.toReportByCategoryEntityValue(): ReportByCategoryEntity.ByCurrency.Value {
        return ReportByCategoryEntity.ByCurrency.Value(
            category = this.toCategoryEntity(),
            amount = this.total ?: 0L,
        )
    }

    private fun ByCategory.toCategoryEntity(): CategoryEntity {
        return if (this.categoryId != null) {
            CategoryEntity(
                id = this.categoryId,
                name = this.categoryName!!,
                type = TransactionType.from(this.categoryType!!.toInt()),
                iconId = this.categoryIconId ?: CategoryEntity.DEFAULT_ICON_ID,
                color = this.categoryColor?.toInt() ?: CategoryEntity.DEFAULT_COLOR,
            )
        } else {
            CategoryEntity.from(
                name = "Deleted",
                type = TransactionType.from(this.type.toInt()),
            )
        }
    }
}
