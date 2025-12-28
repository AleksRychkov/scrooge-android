package dev.aleksrychkov.scrooge.feature.reports.internal

import dev.aleksrychkov.scrooge.core.database.ReportDao
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.reports.ReportByCategoryResult
import dev.aleksrychkov.scrooge.feature.reports.ReportByCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultReportByCategoryUseCase(
    private val reportDao: Lazy<ReportDao>,
    private val ioDispatcher: CoroutineDispatcher,
) : ReportByCategoryUseCase {
    override suspend fun invoke(filter: FilterEntity): ReportByCategoryResult =
        withContext(ioDispatcher) {
            runSuspendCatching {
                val result = reportDao.value.byCategory(filter = filter)
                ReportByCategoryResult.Success(result)
            }.getOrDefault(ReportByCategoryResult.Failure)
        }
}
