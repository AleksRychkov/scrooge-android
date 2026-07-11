package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTotalTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTotalTimelineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportBalanceTotalTimelineUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportBalanceTotalTimelineUseCase {
    override suspend fun invoke(filter: FilterEntity): ReportBalanceTotalTimelineResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                ReportBalanceTotalTimelineResult.Success(
                    reportDao.value.balanceTotalTimeline(filter),
                )
            }.getOrDefault(ReportBalanceTotalTimelineResult.Failure)
        }
}
