package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.limits.LimitsUpdateUseCase
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class UpdateDelegate(
    private val useCase: Lazy<LimitsUpdateUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: LimitsCommand.Update): Flow<LimitsEvent> {
        useCase.value.invoke(entity = cmd.entity)
        return emptyFlow()
    }
}
