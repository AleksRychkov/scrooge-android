package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf

import dev.aleksrychkov.scrooge.component.report.annualtotal.internal.component.udf.actors.LoadDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Switcher
import kotlinx.coroutines.flow.Flow

internal class TotalMonthlyActor(
    private val loadDelegate: LoadDelegate,
) : Actor<TotalMonthlyCommand, TotalMonthlyEvent> {

    companion object {
        operator fun invoke(): TotalMonthlyActor {
            return TotalMonthlyActor(
                loadDelegate = LoadDelegate(useCase = getLazy()),
            )
        }
    }

    private val loadSwitcher: Switcher by lazy { Switcher() }

    override suspend fun process(command: TotalMonthlyCommand): Flow<TotalMonthlyEvent> {
        return when (command) {
            is TotalMonthlyCommand.Load -> loadSwitcher.switch { loadDelegate(command) }
        }
    }
}
