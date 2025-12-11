package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.ReportTotalAmountEntity
import dev.aleksrychkov.scrooge.core.entity.amountToStringFormatted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
internal data class PeriodTotalState(
    val isLoading: Boolean = true,
    val data: ByType = ByType(),
) {
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

internal fun ReportTotalAmountEntity.mapToStateValue(): PeriodTotalState.ByType {
    return PeriodTotalState.ByType(
        income = this.income
            .map { value ->
                PeriodTotalState.ByType.Value(
                    currencySymbol = value.currency.currencySymbol,
                    amount = value.amount.amountToStringFormatted(),
                )
            }
            .toImmutableList(),
        expense = this.expense
            .map { value ->
                PeriodTotalState.ByType.Value(
                    currencySymbol = value.currency.currencySymbol,
                    amount = value.amount.amountToStringFormatted(),
                )
            }
            .toImmutableList(),
        total = this.total
            .map { value ->
                PeriodTotalState.ByType.Value(
                    currencySymbol = value.currency.currencySymbol,
                    amount = value.amount.amountToStringFormatted(),
                )
            }
            .toImmutableList(),
    )
}
