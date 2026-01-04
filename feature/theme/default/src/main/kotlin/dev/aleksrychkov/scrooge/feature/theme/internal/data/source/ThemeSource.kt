package dev.aleksrychkov.scrooge.feature.theme.internal.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal interface ThemeSource {
    companion object {
        operator fun invoke(
            storeName: String,
            context: Context,
        ): ThemeSource =
            DefaultThemeSource(
                dataStoreName = storeName,
                context = context,
            )
    }

    suspend fun observe(): Flow<ThemeEntity?>
    suspend fun set(theme: ThemeEntity)
}

private class DefaultThemeSource(
    dataStoreName: String,
    private val context: Context,
) : ThemeSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(dataStoreName)
    private val key: Preferences.Key<Int> = intPreferencesKey("theme_type_ordinal")

    override suspend fun observe(): Flow<ThemeEntity?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: ThemeEntity.Type.Undefined.ordinal
            }
            .map { ordinal ->
                ThemeEntity(type = ThemeEntity.Type.entries[ordinal])
            }
    }

    override suspend fun set(theme: ThemeEntity): Unit = withContext(NonCancellable) {
        context.dataStore.edit { preferences ->
            preferences[key] = theme.type.ordinal
        }
    }
}
