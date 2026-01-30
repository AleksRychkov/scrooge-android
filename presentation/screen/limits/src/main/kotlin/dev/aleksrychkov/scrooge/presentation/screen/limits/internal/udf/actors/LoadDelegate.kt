package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class LoadDelegate {
    suspend operator fun invoke(): Flow<LimitsEvent> {
        return emptyFlow()
    }
}
