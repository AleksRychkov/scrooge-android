package dev.aleksrychkov.scrooge.feature.reports

import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.feature.reports.internal.DefaultReportTotalByTypeAndCurrencyUseCase
import kotlinx.coroutines.Dispatchers

fun buildReportsModule(): NaiveModule {
    return module {
        factory<ReportTotalByTypeAndCurrencyUseCase> {
            DefaultReportTotalByTypeAndCurrencyUseCase(
                reportDao = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
