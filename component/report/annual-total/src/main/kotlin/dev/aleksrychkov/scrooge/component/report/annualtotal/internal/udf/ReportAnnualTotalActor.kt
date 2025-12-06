package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class ReportAnnualTotalActor : Actor<ReportAnnualTotalCommand, ReportAnnualTotalEvent> {
    override suspend fun process(command: ReportAnnualTotalCommand): Flow<ReportAnnualTotalEvent> {
        return emptyFlow()
    }
}
