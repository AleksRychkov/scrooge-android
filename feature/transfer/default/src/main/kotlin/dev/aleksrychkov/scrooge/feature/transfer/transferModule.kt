@file:Suppress("Filename")

package dev.aleksrychkov.scrooge.feature.transfer

import android.content.Context
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.di.singleton
import dev.aleksrychkov.scrooge.feature.transfer.internal.DefaultExportDataUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.DefaultObserveTransferStateUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.DefaultSetTransferStateUseCase
import dev.aleksrychkov.scrooge.feature.transfer.internal.data.repository.TransferStateRepository
import dev.aleksrychkov.scrooge.feature.transfer.internal.data.source.TransferStateSource
import kotlinx.coroutines.Dispatchers

fun buildTransferModule(context: Context): NaiveModule {
    return module {
        singleton<TransferStateSource> {
            TransferStateSource(
                storeName = "transfer_data_store",
                context = context,
            )
        }
        singleton<TransferStateRepository> {
            TransferStateRepository(
                source = get(),
            )
        }
        singleton<ObserveTransferStateUseCase> {
            DefaultObserveTransferStateUseCase(
                repository = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
        factory<ExportDataUseCase> {
            DefaultExportDataUseCase(
                exportUriUseCase = getLazy(),
                context = context,
            )
        }
        factory<SetTransferStateUseCase> {
            DefaultSetTransferStateUseCase(
                repository = getLazy(),
                ioDispatcher = Dispatchers.IO,
            )
        }
    }
}
