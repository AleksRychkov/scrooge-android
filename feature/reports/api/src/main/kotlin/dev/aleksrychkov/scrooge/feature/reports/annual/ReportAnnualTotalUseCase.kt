package dev.aleksrychkov.scrooge.feature.reports.annual

import dev.aleksrychkov.scrooge.feature.reports.annual.entity.AnnualTotalReportEntity

fun interface ReportAnnualTotalUseCase {
    suspend operator fun invoke(year: Int): ReportAnnualTotalResult
}

sealed interface ReportAnnualTotalResult {
    data class Success(val entity: AnnualTotalReportEntity) : ReportAnnualTotalResult
    data object Empty : ReportAnnualTotalResult
    data object Failure : ReportAnnualTotalResult
}
