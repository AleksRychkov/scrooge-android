package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportIncomeExpenseTimelineEntity

fun interface ReportIncomeExpenseTimelineUseCase {
    suspend operator fun invoke(filter: FilterEntity): ReportIncomeExpenseTimelineResult
}

sealed interface ReportIncomeExpenseTimelineResult {
    data class Success(
        val result: ReportIncomeExpenseTimelineEntity,
    ) : ReportIncomeExpenseTimelineResult

    data object Failure : ReportIncomeExpenseTimelineResult
}
