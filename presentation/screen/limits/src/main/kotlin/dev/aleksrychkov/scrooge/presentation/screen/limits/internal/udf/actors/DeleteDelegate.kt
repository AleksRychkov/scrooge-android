package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.limits.LimitsDeleteUseCase
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsCommand
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class DeleteDelegate(
    private val useCase: Lazy<LimitsDeleteUseCase> = getLazy(),
) {
    suspend operator fun invoke(cmd: LimitsCommand.Delete): Flow<LimitsEvent> {
        useCase.value.invoke(id = cmd.id)
        return emptyFlow()
    }
}
