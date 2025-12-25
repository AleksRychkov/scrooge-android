package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
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
    val currentYear: Int = 0,
) {
    @Immutable
    data class ByMonth(
        val month: String,
        val byType: ByType,
        val periodTimestamp: PeriodTimestampEntity,
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
    year: Int,
): ImmutableList<TotalMonthlyState.ByMonth> {
    val months = resourceManager.getStringArray(Resources.array.reports_month_names)
    return this.result
        .map { (month, value) ->
            value.mapToByMonthState(
                month = month,
                months = months,
                year = year,
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
        month = monthName,
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
