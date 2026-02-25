package dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.delegate

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveTotalResult
import dev.aleksrychkov.scrooge.feature.limits.LimitsObserveTotalUseCase
import dev.aleksrychkov.scrooge.presentation.component.limits.internal.udf.LimitsEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class ObserveLimitsDelegate(
    private val useCase: Lazy<LimitsObserveTotalUseCase> = getLazy(),
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<LimitsEvent> {
        return when (val res = useCase.value.invoke()) {
            LimitsObserveTotalResult.Failure -> emptyFlow()
            is LimitsObserveTotalResult.Success -> res.result.map {
                println(it.joinToString())
                LimitsEvent.Internal.LimitsResult(it)
            }
        }
    }
}
