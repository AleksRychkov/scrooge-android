package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf

internal sealed interface ReportAnnualTotalEvent {
    sealed interface External : ReportAnnualTotalEvent {
        data class Initial(val year: Int) : ReportAnnualTotalEvent
        data object IncrementYear : ReportAnnualTotalEvent
        data object DecrementYear : ReportAnnualTotalEvent
    }

    sealed interface Internal : ReportAnnualTotalEvent
}
