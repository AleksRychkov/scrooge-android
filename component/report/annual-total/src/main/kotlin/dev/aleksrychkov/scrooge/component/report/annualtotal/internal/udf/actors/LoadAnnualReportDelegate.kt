package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.actors

import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalCommand
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Suppress("UnusedPrivateProperty")
internal class LoadAnnualReportDelegate {
    suspend operator fun invoke(cmd: ReportAnnualTotalCommand.Load): Flow<ReportAnnualTotalEvent> {
        return emptyFlow()
    }
}
