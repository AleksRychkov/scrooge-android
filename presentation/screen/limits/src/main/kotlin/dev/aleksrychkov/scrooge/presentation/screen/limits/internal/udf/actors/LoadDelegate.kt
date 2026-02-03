package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.limits.LimitsGetResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsGetUseCase
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class LoadDelegate(
    private val useCase: Lazy<LimitsGetUseCase> = getLazy(),
) {
    suspend operator fun invoke(): Flow<LimitsEvent> {
        val result = useCase.value.invoke()
        return if (result is LimitsGetResult.Success) {
            flowOf(LimitsEvent.Internal.LimitsResult(data = result.result))
        } else {
            emptyFlow()
        }
    }
}
