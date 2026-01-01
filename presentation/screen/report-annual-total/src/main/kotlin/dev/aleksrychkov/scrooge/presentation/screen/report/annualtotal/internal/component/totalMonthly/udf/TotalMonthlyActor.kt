package dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Switcher
import dev.aleksrychkov.scrooge.presentation.screen.report.annualtotal.internal.component.totalMonthly.udf.actors.LoadDelegate
import kotlinx.coroutines.flow.Flow

internal class TotalMonthlyActor(
    private val loadDelegate: LoadDelegate = LoadDelegate(),
) : Actor<TotalMonthlyCommand, TotalMonthlyEvent> {

    private val loadSwitcher: Switcher by lazy { Switcher() }

    override suspend fun process(command: TotalMonthlyCommand): Flow<TotalMonthlyEvent> {
        return when (command) {
            is TotalMonthlyCommand.Load -> loadSwitcher.switch { loadDelegate(command) }
        }
    }
}
