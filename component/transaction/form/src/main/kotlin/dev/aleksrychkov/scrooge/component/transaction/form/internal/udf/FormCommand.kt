package dev.aleksrychkov.scrooge.component.transaction.form.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

internal sealed interface FormCommand {
    data object GetLastUsedCurrency : FormCommand
    data object Exit : FormCommand
    data class SetLastUsedCurrency(val currency: CurrencyEntity) : FormCommand
    data class LoadTransaction(val transactionId: Long) : FormCommand
    data class Submit(val state: FormState) : FormCommand
    data class Delete(val state: FormState) : FormCommand
}
