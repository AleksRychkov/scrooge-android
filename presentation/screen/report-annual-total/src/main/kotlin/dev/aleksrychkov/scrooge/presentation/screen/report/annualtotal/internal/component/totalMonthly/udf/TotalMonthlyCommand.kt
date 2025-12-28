package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf

import dev.aleksrychkov.scrooge.core.entity.FilterEntity

internal sealed interface TotalMonthlyCommand {
    data class Load(val filter: FilterEntity) : TotalMonthlyCommand
}
