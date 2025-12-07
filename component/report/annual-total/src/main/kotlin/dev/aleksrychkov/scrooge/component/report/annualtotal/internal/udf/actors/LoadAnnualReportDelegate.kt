package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.actors

import android.util.SparseArray
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalCommand
import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.ReportAnnualTotalEvent
import dev.aleksrychkov.scrooge.feature.reports.annual.ReportAnnualTotalUseCase
import dev.aleksrychkov.scrooge.feature.reports.annual.entity.AnnualTotalReportEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Suppress("UnusedPrivateProperty")
internal class LoadAnnualReportDelegate(
    private val useCase: Lazy<ReportAnnualTotalUseCase>
) {
    private val cache = SparseArray<AnnualTotalReportEntity>()

    suspend operator fun invoke(cmd: ReportAnnualTotalCommand.Load): Flow<ReportAnnualTotalEvent> {
        return emptyFlow()
    }
}
