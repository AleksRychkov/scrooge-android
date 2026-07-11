package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportBalanceTimelineEntity

fun interface ReportBalanceTimelineUseCase {
    suspend operator fun invoke(filter: FilterEntity): ReportBalanceTimelineResult
}

sealed interface ReportBalanceTimelineResult {
    data class Success(val result: ReportBalanceTimelineEntity) : ReportBalanceTimelineResult
    data object Failure : ReportBalanceTimelineResult
}
