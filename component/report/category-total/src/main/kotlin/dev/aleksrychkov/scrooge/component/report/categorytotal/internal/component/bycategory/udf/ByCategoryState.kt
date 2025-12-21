package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.composables.PieChartSegment
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.entity.amountToString
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.resources.categoryIconFromId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class ByCategoryState(
    val isLoading: Boolean = false,
    val period: PeriodTimestampEntity,
    val currentType: TransactionType = TransactionType.Expense,
    val byCurrencyIncome: ImmutableList<ByCurrency> = persistentListOf(),
    val byCurrencyExpense: ImmutableList<ByCurrency> = persistentListOf(),
) {

    @Immutable
    data class ByCurrency(
        val currency: CurrencyEntity,
        val chartData: ImmutableList<PieChartSegment>,
        val valueData: ImmutableList<Value>,
    ) {

        @Immutable
        data class Value(
            val categoryName: String,
            val categoryIcon: CategoryIcon,
            val categoryColor: Int,
            val currencySymbol: String,
            val amount: String,
        )
    }
}

internal fun List<ReportByCategoryEntity.ByCurrency>.toByCurrencyStateList():
    ImmutableList<ByCategoryState.ByCurrency> {
    return this
        .map { byCurrency ->
            ByCategoryState.ByCurrency(
                currency = byCurrency.currency,
                chartData = byCurrency.data.toByCurrencyChartDataStateList(),
                valueData = byCurrency.data.toByCurrencyValueStateList(byCurrency.currency.currencySymbol)
            )
        }
        .toImmutableList()
}

private fun List<ReportByCategoryEntity.ByCurrency.Value>.toByCurrencyValueStateList(
    currencySymbol: String,
): ImmutableList<ByCategoryState.ByCurrency.Value> {
    return this
        .sortedBy { -it.amount }
        .map { value ->
            ByCategoryState.ByCurrency.Value(
                categoryColor = value.category.color,
                categoryName = value.category.name,
                categoryIcon = categoryIconFromId(value.category.iconId),
                currencySymbol = currencySymbol,
                amount = value.amount.amountToString(),
            )
        }
        .toImmutableList()
}

private fun List<ReportByCategoryEntity.ByCurrency.Value>.toByCurrencyChartDataStateList():
    ImmutableList<PieChartSegment> {
    val total = this.sumOf { it.amount }
    return this
        .sortedBy { -it.amount }
        .map { value ->
            PieChartSegment(
                percentage = value.amount / total.toFloat(),
                color = value.category.color,
                icon = categoryIconFromId(value.category.iconId).icon,
            )
        }
        .toImmutableList()
}
