package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf

internal sealed interface TotalMonthlyCommand {
    data class Load(val year: Int) : TotalMonthlyCommand
}
