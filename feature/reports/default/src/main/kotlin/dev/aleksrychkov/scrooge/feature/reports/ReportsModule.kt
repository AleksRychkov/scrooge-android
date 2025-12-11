package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.reports.internal.DefaultReportTotalAmountMonthlyUseCase
import dev.aleksrychkov.scrooge.feature.reports.internal.DefaultReportTotalAmountUseCase
import kotlinx.coroutines.Dispatchers

fun buildReportsModule(): NaiveModule {
    return module {
        factory<ReportTotalAmountUseCase> {
            DefaultReportTotalAmountUseCase(
                reportDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<ReportTotalAmountMonthlyUseCase> {
            DefaultReportTotalAmountMonthlyUseCase(
                reportDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
