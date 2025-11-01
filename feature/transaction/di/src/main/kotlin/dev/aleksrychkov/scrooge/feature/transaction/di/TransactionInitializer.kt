package dev.aleksrychkov.scrooge.feature.transaction.di

import android.content.Context
import androidx.startup.Initializer
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.feature.transaction.buildTransactionModule

internal class TransactionInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Naive.add(buildTransactionModule())
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
