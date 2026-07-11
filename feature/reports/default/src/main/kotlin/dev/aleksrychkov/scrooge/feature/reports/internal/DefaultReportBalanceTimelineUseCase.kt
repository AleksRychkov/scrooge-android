package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTimelineResult
import dev.aleksrychkov.scrooge.feature.reports.ReportBalanceTimelineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportBalanceTimelineUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportBalanceTimelineUseCase {
    override suspend fun invoke(filter: FilterEntity): ReportBalanceTimelineResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                ReportBalanceTimelineResult.Success(
                    result = reportDao.value.balanceTimeline(filter = filter),
                )
            }.getOrDefault(ReportBalanceTimelineResult.Failure)
        }
}
