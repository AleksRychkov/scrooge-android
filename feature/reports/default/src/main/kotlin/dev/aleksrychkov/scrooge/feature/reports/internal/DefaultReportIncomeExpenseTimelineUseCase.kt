package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportIncomeExpenseTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportIncomeExpenseTimelineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportIncomeExpenseTimelineUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportIncomeExpenseTimelineUseCase {
    override suspend fun invoke(filter: FilterEntity): ReportIncomeExpenseTimelineResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                ReportIncomeExpenseTimelineResult.Success(
                    result = reportDao.value.incomeExpenseTimeline(filter),
                )
            }.getOrDefault(ReportIncomeExpenseTimelineResult.Failure)
        }
}
