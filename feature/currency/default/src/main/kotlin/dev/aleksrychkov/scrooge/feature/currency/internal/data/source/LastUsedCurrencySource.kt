package dev.aleksrychkov.scrooge.feature.currency.internal.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal interface LastUsedCurrencySource {
    companion object {
        operator fun invoke(
            storeName: String,
            context: Context,
        ): LastUsedCurrencySource =
            DefaultLastUsedCurrencySource(
                dataStoreName = storeName,
                context = context,
            )
    }

    suspend fun get(): CurrencyEntity?
    suspend fun set(currency: CurrencyEntity)
}

private class DefaultLastUsedCurrencySource(
    dataStoreName: String,
    private val context: Context,
) : LastUsedCurrencySource {

    private val Context._dataStore by preferencesDataStore(dataStoreName)
    private val dataStore: DataStore<Preferences> by lazy {
        context._dataStore
    }
    private val key: Preferences.Key<String> by lazy {
        stringPreferencesKey("last_used_currency_code")
    }

    override suspend fun get(): CurrencyEntity? {
        return dataStore.data
            .map { preferences ->
                preferences[key]
            }
            .firstOrNull()
            ?.let { currencyCode ->
                CurrencyEntity.fromCurrencyCode(currencyCode = currencyCode)
            }
    }

    override suspend fun set(currency: CurrencyEntity): Unit = withContext(NonCancellable) {
        dataStore.edit { preferences ->
            preferences[key] = currency.currencyCode
        }
    }
}
