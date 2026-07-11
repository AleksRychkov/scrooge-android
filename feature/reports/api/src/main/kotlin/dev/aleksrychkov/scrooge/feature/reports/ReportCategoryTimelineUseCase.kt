package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportCategoryTimelineEntity

fun interface ReportCategoryTimelineUseCase {
    suspend operator fun invoke(filter: FilterEntity): ReportCategoryTimelineResult
}

sealed interface ReportCategoryTimelineResult {
    data class Success(val result: ReportCategoryTimelineEntity) : ReportCategoryTimelineResult
    data object Failure : ReportCategoryTimelineResult
}
