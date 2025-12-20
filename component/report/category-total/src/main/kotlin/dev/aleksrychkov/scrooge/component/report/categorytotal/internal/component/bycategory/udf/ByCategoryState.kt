package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.composables.DonutChartSegment
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
        val chartData: ImmutableList<DonutChartSegment>,
        val valueData: ImmutableList<Value>,
    ) {

        @Immutable
        data class Value(
            val categoryName: String,
            val categoryIcon: CategoryIcon,
            val categoryColor: Color,
            val currencySymbol: String,
            val amount: String,
        )
    }
}
