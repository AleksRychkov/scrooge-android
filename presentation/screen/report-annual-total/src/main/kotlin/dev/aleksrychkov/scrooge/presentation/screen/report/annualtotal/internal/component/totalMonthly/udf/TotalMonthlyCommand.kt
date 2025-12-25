package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf

internal sealed interface TotalMonthlyCommand {
    data class Load(val year: Int) : TotalMonthlyCommand
}
