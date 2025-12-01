package dev.aleksrychkov.scrooge.feature.category.internal.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal interface CategoryKeyValueSource {
    companion object Companion {
        operator fun invoke(
            storeName: String,
            context: Context,
        ): CategoryKeyValueSource =
            DefaultDefaultCategoriesSource(
                dataStoreName = storeName,
                context = context,
            )
    }

    suspend fun isPreloadDone(): Boolean
    suspend fun setPreloadDone()
}

private class DefaultDefaultCategoriesSource(
    dataStoreName: String,
    private val context: Context,
) : CategoryKeyValueSource {
    private val Context._dataStore by preferencesDataStore(dataStoreName)
    private val dataStore: DataStore<Preferences> by lazy {
        context._dataStore
    }
    private val key: Preferences.Key<Boolean> by lazy {
        booleanPreferencesKey("is_default_categories_preload_done")
    }

    override suspend fun isPreloadDone(): Boolean {
        return dataStore.data
            .map { preferences ->
                preferences[key]
            }
            .firstOrNull()
            ?: false
    }

    override suspend fun setPreloadDone(): Unit = withContext(NonCancellable) {
        dataStore.edit { preferences ->
            preferences[key] = true
        }
    }
}
