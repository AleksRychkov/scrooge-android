package dev.aleksrychkov.scrooge.component.currency.internal.udf

import dev.aleksrychkov.scrooge.component.currency.internal.udf.actors.GetCurrenciesDelegate
import dev.aleksrychkov.scrooge.component.currency.internal.udf.actors.SearchCurrenciesDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow

internal class CurrencyActor(
    private val getCurrencies: GetCurrenciesDelegate,
    private val searchCurrencies: SearchCurrenciesDelegate,
) : Actor<CurrencyCommand, CurrencyEvent> {

    companion object {
        operator fun invoke(): CurrencyActor {
            return CurrencyActor(
                getCurrencies = GetCurrenciesDelegate(useCase = getLazy()),
                searchCurrencies = SearchCurrenciesDelegate(),
            )
        }
    }

    override suspend fun process(command: CurrencyCommand): Flow<CurrencyEvent> {
        return when (command) {
            CurrencyCommand.GetCurrencies -> getCurrencies()
            is CurrencyCommand.Search -> searchCurrencies(command)
        }
    }
}
