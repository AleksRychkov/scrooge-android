package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveUseCase
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class LoadDelegate(
    private val useCase: Lazy<LimitsObserveUseCase> = getLazy(),
) {
    suspend operator fun invoke(): Flow<LimitsEvent> {
        val result = useCase.value.invoke()
        return if (result is LimitsObserveResult.Success) {
            result.result.map {
                LimitsEvent.Internal.LimitsResult(data = it)
            }
        } else {
            emptyFlow()
        }
    }
}
