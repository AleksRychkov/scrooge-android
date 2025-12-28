package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.ReportByCategoryEntity

fun interface ReportByCategoryUseCase {
    suspend operator fun invoke(filter: FilterEntity): ReportByCategoryResult
}

sealed interface ReportByCategoryResult {
    data class Success(val result: ReportByCategoryEntity) : ReportByCategoryResult
    data object Failure : ReportByCategoryResult
}
