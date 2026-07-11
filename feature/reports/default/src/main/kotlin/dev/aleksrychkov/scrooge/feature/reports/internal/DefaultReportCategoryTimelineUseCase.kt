package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportCategoryTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportCategoryTimelineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportCategoryTimelineUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportCategoryTimelineUseCase {
    override suspend fun invoke(filter: FilterEntity): ReportCategoryTimelineResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                ReportCategoryTimelineResult.Success(
                    result = reportDao.value.categoryTimeline(filter = filter),
                )
            }.getOrDefault(ReportCategoryTimelineResult.Failure)
        }
}
