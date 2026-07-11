package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity

fun interface ReportBalanceTotalTimelineUseCase {
    suspend operator fun invoke(filter: FilterEntity): ReportBalanceTotalTimelineResult
}

sealed interface ReportBalanceTotalTimelineResult {
    data class Success(val result: ReportBalanceTimelineEntity) : ReportBalanceTotalTimelineResult
    data object Failure : ReportBalanceTotalTimelineResult
}
