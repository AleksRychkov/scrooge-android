package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.PeriodDatestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import dev.aleksrychkov.scrooge.core.entity.amountToStringFormatted
import dev.aleksrychkov.scrooge.core.entity.startEndOfMonth
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Immutable
internal data class TotalMonthlyState(
    val isLoading: Boolean = false,
    val byMonth: ImmutableList<ByMonth> = persistentListOf(),
    val filter: FilterEntity = FilterEntity(),
) {
    @Immutable
    data class ByMonth(
        val title: String,
        val byType: ByType,
        val periodTimestamp: PeriodDatestampEntity,
    )

    @Immutable
    data class ByType(
        val income: ImmutableList<Value> = persistentListOf(),
        val expense: ImmutableList<Value> = persistentListOf(),
        val total: ImmutableList<Value> = persistentListOf(),
    ) {
        @Immutable
        data class Value(
            val currencySymbol: String,
            val amount: String,
        )
    }
}

internal fun ReportTotalAmountMonthlyEntity.mapToState(
    resourceManager: ResourceManager,
): ImmutableList<TotalMonthlyState.ByMonth> {
    val months = resourceManager.getStringArray(Resources.array.month_names)
    return this.result
        .map { (date, value) ->
            value.mapToByMonthState(
                month = date.month,
                months = months,
                year = date.year,
            )
        }
        .reversed()
        .toImmutableList()
}

private fun ReportTotalAmountEntity.mapToByMonthState(
    month: Month,
    months: Array<String>,
    year: Int,
): TotalMonthlyState.ByMonth {
    val monthName = months[month.ordinal]
    return TotalMonthlyState.ByMonth(
        title = "$monthName $year",
        byType = this.mapToStateValue(),
        periodTimestamp = startEndOfMonth(month, year),
    )
}

private fun ReportTotalAmountEntity.mapToStateValue(): TotalMonthlyState.ByType {
    return TotalMonthlyState.ByType(
        income = this.income
            .map { value ->
                TotalMonthlyState.ByType.Value(
                    currencySymbol = value.currency.currencySymbol,
                    amount = value.amount.amountToStringFormatted("+"),
                )
            }
            .toImmutableList(),
        expense = this.expense
            .map { value ->
                TotalMonthlyState.ByType.Value(
                    currencySymbol = value.currency.currencySymbol,
                    amount = value.amount.amountToStringFormatted("-"),
                )
            }
            .toImmutableList(),
        total = this.total
            .map { value ->
                TotalMonthlyState.ByType.Value(
                    currencySymbol = value.currency.currencySymbol,
                    amount = value.amount.amountToStringFormatted(),
                )
            }
            .toImmutableList(),
    )
}
