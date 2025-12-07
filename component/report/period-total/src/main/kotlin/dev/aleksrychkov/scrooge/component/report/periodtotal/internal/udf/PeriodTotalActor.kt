package dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf

import dev.aleksrychkov.scrooge.component.report.periodtotal.internal.udf.actors.LoadDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Switcher
import kotlinx.coroutines.flow.Flow

internal class PeriodTotalActor(
    private val loadDelegate: LoadDelegate,
) : Actor<PeriodTotalCommand, PeriodTotalEvent> {

    companion object {
        operator fun invoke(): PeriodTotalActor =
            PeriodTotalActor(
                loadDelegate = LoadDelegate(useCase = getLazy())
            )
    }

    private val loadSwitcher: Switcher by lazy { Switcher() }

    override suspend fun process(command: PeriodTotalCommand): Flow<PeriodTotalEvent> {
        return when (command) {
            is PeriodTotalCommand.Load -> loadSwitcher.switch { loadDelegate(command) }
        }
    }
}
