package dev.aleksrychkov.scrooge.feature.transfer.internal.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
    private val keyInfo: Preferences.Key<String> = stringPreferencesKey("transfer_state_info")

    override suspend fun observe(): Flow<TransferStateEntity> {
        return context.dataStore.data
            .map { preferences ->
                val ordinal = preferences[key] ?: TransferStateEntity.State.None().ordinal
                val info = preferences[keyInfo]
                ordinal to info
            }
            .map { pair ->
                val state =
                    TransferStateEntity.State.fromOrdinal(ordinal = pair.first, info = pair.second)
                TransferStateEntity(current = state)
            }
    }

    override suspend fun set(state: TransferStateEntity): Unit = withContext(NonCancellable) {
        context.dataStore.edit { preferences ->
            preferences[key] = state.current.ordinal
            state.current.info?.let {
                preferences[keyInfo] = it
            } ?: run {
                preferences.remove(keyInfo)
            }
        }
    }
}
