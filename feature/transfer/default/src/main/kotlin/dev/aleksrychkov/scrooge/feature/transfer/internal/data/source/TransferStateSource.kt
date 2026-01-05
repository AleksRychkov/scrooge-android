package dev.aleksrychkov.scrooge.feature.transfer.internal.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal interface TransferStateSource {
    companion object {
        operator fun invoke(
            storeName: String,
            context: Context,
        ): TransferStateSource =
            DefaultTransferStateSource(
                dataStoreName = storeName,
                context = context,
            )
    }

    suspend fun observe(): Flow<TransferStateEntity>
    suspend fun set(state: TransferStateEntity)
}

private class DefaultTransferStateSource(
    dataStoreName: String,
    private val context: Context,
) : TransferStateSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(dataStoreName)
    private val key: Preferences.Key<Int> = intPreferencesKey("transfer_state_ordinal")

    override suspend fun observe(): Flow<TransferStateEntity> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: TransferStateEntity.State.None.ordinal
            }
            .map { ordinal ->
                TransferStateEntity(current = TransferStateEntity.State.entries[ordinal])
            }
    }

    override suspend fun set(state: TransferStateEntity): Unit = withContext(NonCancellable) {
        context.dataStore.edit { preferences ->
            preferences[key] = state.current.ordinal
        }
    }
}
