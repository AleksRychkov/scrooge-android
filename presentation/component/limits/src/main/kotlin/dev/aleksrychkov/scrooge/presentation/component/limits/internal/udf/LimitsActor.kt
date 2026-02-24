package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.delegate.ObserveLimitsDelegate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class LimitsActor(
    private val observeLimits: ObserveLimitsDelegate = ObserveLimitsDelegate(),
) : Actor<LimitsCommand, LimitsEvent> {
    override suspend fun process(command: LimitsCommand): Flow<LimitsEvent> {
        return when (command) {
            LimitsCommand.ObserveLimits -> observeLimits.invoke()
            LimitsCommand.ObserveTotals -> emptyFlow()
        }
    }
}
