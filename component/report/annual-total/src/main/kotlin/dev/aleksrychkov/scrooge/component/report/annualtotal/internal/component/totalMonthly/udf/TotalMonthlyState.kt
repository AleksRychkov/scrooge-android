package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.totalMonthly.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountMonthlyEntity
import dev.aleksrychkov.scrooge.core.entity.amountToStringFormatted
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Month
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Immutable
internal data class TotalMonthlyState(
    val isLoading: Boolean = false,
    val byMonth: ImmutableList<ByMonth> = persistentListOf()
) {
    @Immutable
    data class ByMonth(
        val month: String,
        val byType: ByType,
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
    resourceManager: ResourceManager
): ImmutableList<TotalMonthlyState.ByMonth> {
    val months = resourceManager.getStringArray(Resources.array.reports_month_names)
    val res = mutableListOf<TotalMonthlyState.ByMonth>()
    this.january?.mapToByMonthState(months[Month.JANUARY.ordinal])?.let(res::add)
    this.february?.mapToByMonthState(months[Month.FEBRUARY.ordinal])?.let(res::add)
    this.march?.mapToByMonthState(months[Month.MARCH.ordinal])?.let(res::add)
    this.april?.mapToByMonthState(months[Month.APRIL.ordinal])?.let(res::add)
    this.may?.mapToByMonthState(months[Month.MAY.ordinal])?.let(res::add)
    this.june?.mapToByMonthState(months[Month.JUNE.ordinal])?.let(res::add)
    this.july?.mapToByMonthState(months[Month.JULY.ordinal])?.let(res::add)
    this.august?.mapToByMonthState(months[Month.AUGUST.ordinal])?.let(res::add)
    this.september?.mapToByMonthState(months[Month.SEPTEMBER.ordinal])?.let(res::add)
    this.october?.mapToByMonthState(months[Month.OCTOBER.ordinal])?.let(res::add)
    this.november?.mapToByMonthState(months[Month.NOVEMBER.ordinal])?.let(res::add)
    this.december?.mapToByMonthState(months[Month.DECEMBER.ordinal])?.let(res::add)
    return res.reversed().toImmutableList()
}

private fun ReportTotalAmountEntity.mapToByMonthState(
    month: String,
): TotalMonthlyState.ByMonth {
    return TotalMonthlyState.ByMonth(
        month = month,
        byType = this.mapToStateValue(),
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
