package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors.LoadDelegate
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors.SaveDelegate
import kotlinx.coroutines.flow.Flow

internal class LimitsActor : Actor<LimitsCommand, LimitsEvent> {
    private val loadDelegate: LoadDelegate by lazy { LoadDelegate() }
    private val saveDelegate: SaveDelegate by lazy { SaveDelegate() }

    override suspend fun process(command: LimitsCommand): Flow<LimitsEvent> {
        return when (command) {
            LimitsCommand.LoadLimits -> loadDelegate()
            is LimitsCommand.Save -> saveDelegate(command)
        }
    }
}
