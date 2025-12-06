package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf

sealed interface ReportAnnualTotalCommand {
    data class Load(val year: Int) : ReportAnnualTotalCommand
}
