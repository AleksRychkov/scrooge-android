package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Switcher
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.delegate.ObserveLimitsDelegate
import kotlinx.coroutines.flow.Flow

internal class LimitsActor(
    private val observeLimits: ObserveLimitsDelegate = ObserveLimitsDelegate(),
) : Actor<LimitsCommand, LimitsEvent> {

    private val switcher = Switcher()

    override suspend fun process(command: LimitsCommand): Flow<LimitsEvent> {
        return when (command) {
            is LimitsCommand.ObserveLimits -> switcher.switch { observeLimits.invoke(command) }
        }
    }
}
