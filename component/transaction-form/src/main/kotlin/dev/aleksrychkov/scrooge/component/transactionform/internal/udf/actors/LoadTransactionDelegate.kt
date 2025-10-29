package dev.aleksrychkov.scrooge.component.transactionform.internal.udf.actors

import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormCommand
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class LoadTransactionDelegate {
    suspend operator fun invoke(cmd: FormCommand.LoadTransaction): Flow<FormEvent> {
        return emptyFlow()
    }
}
