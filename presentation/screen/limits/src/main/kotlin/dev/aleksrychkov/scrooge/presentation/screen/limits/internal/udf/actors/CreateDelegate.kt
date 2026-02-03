package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.limits.LimitsCreateUseCase
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class CreateDelegate(
    private val useCase: Lazy<LimitsCreateUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: LimitsCommand.Create): Flow<LimitsEvent> {
        useCase.value.invoke(entity = cmd.entity)
        return emptyFlow()
    }
}
