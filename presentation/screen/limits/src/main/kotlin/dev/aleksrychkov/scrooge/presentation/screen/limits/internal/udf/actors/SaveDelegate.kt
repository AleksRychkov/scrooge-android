package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class SaveDelegate {
    suspend operator fun invoke(cmd: LimitsCommand.Save): Flow<LimitsEvent> {
        return emptyFlow()
    }
}
