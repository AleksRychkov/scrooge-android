package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.delegate

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveActualResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveActualUseCase
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class ObserveLimitsDelegate(
    private val useCase: Lazy<LimitsObserveActualUseCase> = getLazy(),
) {
    suspend operator fun invoke(): Flow<LimitsEvent> {
        return when (val res = useCase.value.invoke()) {
            LimitsObserveActualResult.Failure -> emptyFlow()
            is LimitsObserveActualResult.Success ->
                res.limits.map { limits ->
                    LimitsEvent.Internal.LimitsResult(limits)
                }
        }
    }
}
