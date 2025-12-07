package dev.aleksrychkov.scrooge.feature.reports.annual.entity

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList

data class AnnualTotalReportEntity(
    val allCurrencies: ImmutableList<CurrencyEntity>,
    val totalMonth: ImmutableList<ByMonthTotal>,
    val total: ImmutableList<Total>,
) {
    data class ByMonthTotal(
        val monthNumber: Int, // 1 -> 12
        val income: ImmutableList<Total>,
        val expense: ImmutableList<Total>,
        val total: ImmutableList<Total>,
    )

    data class Total(
        val currency: CurrencyEntity,
        val total: Long,
    )
}
