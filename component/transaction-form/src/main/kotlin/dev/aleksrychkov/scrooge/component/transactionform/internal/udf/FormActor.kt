package dev.aleksrychkov.scrooge.component.transactionform.internal.udf

import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors.LastUsedCurrencyDelegate
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors.LoadTransactionDelegate
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors.SubmitDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.flow.Flow

internal class FormActor(
    private val lastUsedCurrency: LastUsedCurrencyDelegate,
    private val loadTransaction: LoadTransactionDelegate,
    private val submitDelegate: SubmitDelegate,
) : Actor<FormCommand, FormEvent> {

    companion object {
        operator fun invoke(): FormActor {
            return FormActor(
                lastUsedCurrency = LastUsedCurrencyDelegate(
                    getLastUsedCurrency = getLazy(),
                    setLastUsedCurrency = getLazy(),
                ),
                loadTransaction = LoadTransactionDelegate(),
                submitDelegate = SubmitDelegate(),
            )
        }
    }

    override suspend fun process(command: FormCommand): Flow<FormEvent> {
        return when (command) {
            FormCommand.GetLastUsedCurrency -> lastUsedCurrency.get()
            is FormCommand.SetLastUsedCurrency -> lastUsedCurrency.set(command)
            is FormCommand.LoadTransaction -> loadTransaction(command)
            is FormCommand.Submit -> submitDelegate(command)
        }
    }
}
