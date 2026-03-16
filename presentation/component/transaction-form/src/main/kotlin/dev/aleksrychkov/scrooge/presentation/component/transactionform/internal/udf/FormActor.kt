package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf

import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.actors.DeleteDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.actors.LastUsedCurrencyDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.actors.LoadTransactionDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.actors.SubmitDelegate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

internal class FormActor(
    private val router: Router,
    private val lastUsedCurrency: LastUsedCurrencyDelegate = LastUsedCurrencyDelegate(),
    private val loadTransaction: LoadTransactionDelegate = LoadTransactionDelegate(),
    private val submitDelegate: SubmitDelegate = SubmitDelegate(),
    private val deleteDelegate: DeleteDelegate = DeleteDelegate(),
) : Actor<FormCommand, FormEvent> {

    override suspend fun process(command: FormCommand): Flow<FormEvent> {
        return when (command) {
            FormCommand.GetLastUsedCurrency -> lastUsedCurrency.get()
            is FormCommand.SetLastUsedCurrency -> lastUsedCurrency.set(command)
            is FormCommand.LoadTransaction -> loadTransaction(command)
            is FormCommand.Submit -> submitDelegate(command)
            is FormCommand.Delete -> deleteDelegate(command)
            FormCommand.Exit -> exit()
        }
    }

    private suspend fun exit(): Flow<FormEvent> {
        // todo: currently exit implemented with FormState.isDone, fix it
        withContext(Dispatchers.Main) {
            router.close()
        }
        return emptyFlow()
    }
}
