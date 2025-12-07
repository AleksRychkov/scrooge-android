package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf

import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.udf.actors.LoadAnnualReportDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Switcher
import kotlinx.coroutines.flow.Flow

internal class ReportAnnualTotalActor(
    private val delegate: LoadAnnualReportDelegate,
) : Actor<ReportAnnualTotalCommand, ReportAnnualTotalEvent> {

    companion object {
        operator fun invoke(): ReportAnnualTotalActor {
            return ReportAnnualTotalActor(
                delegate = LoadAnnualReportDelegate(useCase = getLazy())
            )
        }
    }

    private val loadSwitcher: Switcher by lazy {
        Switcher()
    }

    override suspend fun process(command: ReportAnnualTotalCommand): Flow<ReportAnnualTotalEvent> {
        return when (command) {
            is ReportAnnualTotalCommand.Load -> loadSwitcher.switch { delegate(command) }
        }
    }
}
