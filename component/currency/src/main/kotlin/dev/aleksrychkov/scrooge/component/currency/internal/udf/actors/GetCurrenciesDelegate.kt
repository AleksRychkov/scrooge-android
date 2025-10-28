package dev.aleksrychkov.scrooge.component.currency.internal.udf.actors

import dev.aleksrychkov.scrooge.component.currency.internal.udf.CurrencyEvent
import dev.aleksrychkov.scrooge.feature.currency.GetCurrenciesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class GetCurrenciesDelegate(
    private val useCase: Lazy<GetCurrenciesUseCase>,
) {
    suspend operator fun invoke(): Flow<CurrencyEvent> {
        val currencies = useCase.value.invoke()
        return flowOf(CurrencyEvent.Internal.Currencies(currencies = currencies))
    }
}
