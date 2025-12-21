package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity

fun interface ReportByCategoryUseCase {
    suspend operator fun invoke(period: PeriodTimestampEntity): ReportByCategoryResult
}

sealed interface ReportByCategoryResult {
    data class Success(val result: ReportByCategoryEntity) : ReportByCategoryResult
    data object Failure : ReportByCategoryResult
}
