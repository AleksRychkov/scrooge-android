package dev.aleksrychkov.scrooge.feature.currency.internal.data.repository

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.utils.runSuspendCatching
import dev.aleksrychkov.scrooge.feature.currency.internal.data.source.FavoriteCurrencySource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onSubscription

internal interface FavoriteCurrencyRepository {
    companion object {
        operator fun invoke(
            source: Lazy<FavoriteCurrencySource>
        ): FavoriteCurrencyRepository {
            return DefaultFavoriteCurrencyRepository(lazySource = source)
        }
    }

    suspend fun observeFavoriteCurrencies(): Flow<ImmutableList<CurrencyEntity>>
    suspend fun addFavoriteCurrency(currency: CurrencyEntity)
    suspend fun removeFavoriteCurrency(currency: CurrencyEntity)
}

private class DefaultFavoriteCurrencyRepository(
    private val lazySource: Lazy<FavoriteCurrencySource>,
) : FavoriteCurrencyRepository {

    private val source: FavoriteCurrencySource by lazy {
        lazySource.value
    }

    private val favoriteCurrencies: MutableSharedFlow<ImmutableList<CurrencyEntity>> by lazy {
        MutableSharedFlow(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    }

    override suspend fun observeFavoriteCurrencies(): Flow<ImmutableList<CurrencyEntity>> {
        return favoriteCurrencies
            .asSharedFlow()
            .onSubscription {
                updateRuntime()
            }
    }

    override suspend fun addFavoriteCurrency(currency: CurrencyEntity) {
        source.add(currency)
        updateRuntime()
    }

    override suspend fun removeFavoriteCurrency(currency: CurrencyEntity) {
        source.remove(currency)
        updateRuntime()
    }

    private suspend fun updateRuntime() {
        runSuspendCatching {
            source.get()
        }.also { data ->
            favoriteCurrencies.emit(data.getOrDefault(persistentListOf()))
        }
    }
}
