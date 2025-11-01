package dev.aleksrychkov.scrooge.component.transactionform.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity

internal sealed interface FormCommand {
    data object GetLastUsedCurrency : FormCommand
    data class SetLastUsedCurrency(val currency: CurrencyEntity) : FormCommand
    data class LoadTransaction(val transactionId: Long) : FormCommand
    data class Submit(val state: FormState) : FormCommand
}
