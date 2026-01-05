package dev.aleksrychkov.scrooge.feature.transfer.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.transfer.buildTransferModule

internal class TransferInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildTransferModule(context))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
