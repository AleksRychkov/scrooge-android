package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf

sealed interface PeriodTotalCommand {
    data class Load(val fromTimestamp: Long, val toTimestamp: Long) : PeriodTotalCommand
}
