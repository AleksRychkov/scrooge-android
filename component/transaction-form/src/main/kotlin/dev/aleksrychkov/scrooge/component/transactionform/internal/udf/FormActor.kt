package dev.aleksrychkov.scrooge.component.transactionform.internal.udf

import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors.LastUsedCurrencyDelegate
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors.LoadTransactionDelegate
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors.SubmitDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.router.Router
import dev.aleksrychkov.scrooge.core.udf.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext

internal class FormActor(
    private val router: Router,
    private val lastUsedCurrency: LastUsedCurrencyDelegate,
    private val loadTransaction: LoadTransactionDelegate,
    private val submitDelegate: SubmitDelegate,
) : Actor<FormCommand, FormEvent> {

    companion object {
        operator fun invoke(router: Router): FormActor {
            return FormActor(
                router = router,
                lastUsedCurrency = LastUsedCurrencyDelegate(
                    getLastUsedCurrency = getLazy(),
                    setLastUsedCurrency = getLazy(),
                ),
                loadTransaction = LoadTransactionDelegate(
                    router = router,
                    useCase = getLazy(),
                ),
                submitDelegate = SubmitDelegate(
                    createUseCase = getLazy(),
                    editUseCase = getLazy(),
                ),
            )
        }
    }

    override suspend fun process(command: FormCommand): Flow<FormEvent> {
        return when (command) {
            FormCommand.GetLastUsedCurrency -> lastUsedCurrency.get()
            is FormCommand.SetLastUsedCurrency -> lastUsedCurrency.set(command)
            is FormCommand.LoadTransaction -> loadTransaction(command)
            is FormCommand.Submit -> submitDelegate(command)
            FormCommand.Exit -> exit()
        }
    }

    private suspend fun exit(): Flow<FormEvent> {
        withContext(Dispatchers.Main) {
            router.close()
        }
        return emptyFlow()
    }
}
