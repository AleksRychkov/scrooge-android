package dev.aleksrychkov.scrooge.feature.currency.internal.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal interface FavoriteCurrencySource {
    companion object {
        operator fun invoke(
            storeName: String,
            context: Context,
            dispatcher: CoroutineDispatcher = Dispatchers.IO
        ): FavoriteCurrencySource =
            DefaultFavoriteCurrencySource(
                storeName = storeName,
                context = context,
                dispatcher = dispatcher
            )
    }

    suspend fun get(): ImmutableList<CurrencyEntity>
    suspend fun add(currency: CurrencyEntity)
    suspend fun remove(currency: CurrencyEntity)
}

private class DefaultFavoriteCurrencySource(
    storeName: String,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : FavoriteCurrencySource {

    private val Context._dataStore by preferencesDataStore(storeName)
    private val dataStore: DataStore<Preferences> by lazy {
        context._dataStore
    }
    private val json: Json by lazy {
        Json { ignoreUnknownKeys = true }
    }

    private val runtimeCacheMutex: Mutex by lazy { Mutex() }
    private var isFirstReadCompleted: Boolean = false
    private val _runtimeCache: MutableSet<CurrencyEntity> by lazy { mutableSetOf() }

    override suspend fun get(): ImmutableList<CurrencyEntity> {
        return runtimeCache { sorted().toImmutableList() }
    }

    override suspend fun add(currency: CurrencyEntity) {
        val json = json.encodeToString(currency)
        dataStore.edit { preferences ->
            preferences[currency.storeKey()] = json
        }
        runtimeCache {
            add(currency)
        }
    }

    override suspend fun remove(currency: CurrencyEntity) {
        dataStore.edit { preferences ->
            preferences.remove(currency.storeKey())
        }
        runtimeCache { remove(currency) }
    }

    private fun CurrencyEntity.storeKey(): Preferences.Key<String> =
        stringPreferencesKey(currencyNumCode)

    private suspend fun <T> runtimeCache(block: MutableSet<CurrencyEntity>.() -> T): T {
        runtimeCacheMutex.withLock {
            makeFirstReadIfNeeded()
            return block(_runtimeCache)
        }
    }

    private suspend fun makeFirstReadIfNeeded(): Unit = withContext(dispatcher) {
        if (isFirstReadCompleted) return@withContext
        isFirstReadCompleted = true
        dataStore.data
            .map { preferences ->
                preferences.asMap().values
                    .mapNotNull { it as? String }
                    .map { json.decodeFromString<CurrencyEntity>(it) }
                    .let(_runtimeCache::addAll)
            }
            .firstOrNull()
    }
}
