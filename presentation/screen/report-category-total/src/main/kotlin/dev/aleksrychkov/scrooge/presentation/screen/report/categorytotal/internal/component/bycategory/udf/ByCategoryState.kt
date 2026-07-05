package dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.component.bycategory.udf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.toArgb
import dev.aleksrychkov.scrooge.core.designsystem.theme.primaryBlue
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.entity.amountToStringFormatted
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.resources.categoryIconFromId
import dev.aleksrychkov.scrooge.presentation.screen.report.categorytotal.internal.composables.PieChartSegment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.round
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val ROUND_FRACTION = 10f
private const val SHARE_MAX = 100f
private val roundedFloat = { number: Float -> round(number * ROUND_FRACTION) / ROUND_FRACTION }

@Immutable
internal data class ByCategoryState(
    val isLoading: Boolean = false,
    val filter: FilterEntity,
    val currentType: TransactionType = TransactionType.Expense,
    val byCurrencyIncome: ImmutableList<ByCurrency> = persistentListOf(),
    val byCurrencyExpense: ImmutableList<ByCurrency> = persistentListOf(),
    val bottomSheetOffset: Float = -1f,
) {

    @Immutable
    data class ByCurrency(
        val currencySymbol: String,
        val chartData: ImmutableList<PieChartSegment>,
        val rowData: ImmutableList<Row>,
        val totalData: Row,
    ) {

        @Immutable
        data class Row(
            val categoryName: String,
            val categoryIcon: CategoryIcon,
            val categoryColor: Int,
            val currencySymbol: String,
            val currencyCode: String,
            val amount: String,
            val reference: CategoryEntity,
            val share: Float = 0f,
        )
    }
}

internal fun List<ReportByCategoryEntity.ByCurrency>.toByCurrencyStateList(
    resourceManager: ResourceManager,
): ImmutableList<ByCategoryState.ByCurrency> {
    return this
        .map { byCurrency ->
            val total = byCurrency.data.sumOf { it.amount }
            ByCategoryState.ByCurrency(
                currencySymbol = byCurrency.currency.currencySymbol,
                chartData = byCurrency.data.toByCurrencyChartDataStateList(
                    total = total,
                ),
                rowData = byCurrency.data.toByCurrencyValueStateList(
                    total = total,
                    currencySymbol = byCurrency.currency.currencySymbol,
                    currencyCode = byCurrency.currency.currencyCode,
                ),
                totalData = ByCategoryState.ByCurrency.Row(
                    categoryName = resourceManager.getString(Resources.string.total),
                    categoryIcon = CategoryIcon("Balance", Icons.Rounded.Balance),
                    categoryColor = primaryBlue.toArgb(),
                    currencySymbol = byCurrency.currency.currencySymbol,
                    currencyCode = byCurrency.currency.currencyCode,
                    amount = total.amountToStringFormatted(""),
                    reference = CategoryEntity.from(
                        name = "",
                        type = TransactionType.Expense,
                    )
                )
            )
        }
        .toImmutableList()
}

private fun List<ReportByCategoryEntity.ByCurrency.Value>.toByCurrencyValueStateList(
    total: Long,
    currencySymbol: String,
    currencyCode: String,
): ImmutableList<ByCategoryState.ByCurrency.Row> {
    return this
        .sortedBy { -it.amount }
        .map { value ->
            ByCategoryState.ByCurrency.Row(
                categoryColor = value.category.color,
                categoryName = value.category.name,
                categoryIcon = categoryIconFromId(value.category.iconId),
                currencySymbol = currencySymbol,
                currencyCode = currencyCode,
                amount = value.amount.amountToStringFormatted(""),
                reference = value.category,
                share = if (total == 0L) {
                    0f
                } else {
                    roundedFloat(value.amount * SHARE_MAX / total)
                },
            )
        }
        .toImmutableList()
}

private fun List<ReportByCategoryEntity.ByCurrency.Value>.toByCurrencyChartDataStateList(
    total: Long,
): ImmutableList<PieChartSegment> {
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
