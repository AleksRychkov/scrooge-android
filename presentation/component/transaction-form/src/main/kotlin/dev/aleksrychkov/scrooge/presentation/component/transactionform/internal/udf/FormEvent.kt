package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity

internal sealed interface FormEvent {
    sealed interface External : FormEvent {
        data class Init(val transactionId: Long?) : External
        data class SetAmount(val amount: String) : External
        data class AppendAmount(val value: String) : External
        data object RemoveLastFromAmount : External
        data class SetComment(val comment: String) : External
        data class SetCategory(val category: CategoryEntity) : External
        data class SetCurrency(val currency: CurrencyEntity) : External
        data class SetDate(val timestamp: Long) : External
        data class SetTags(val tags: Set<TagEntity>) : External
        data object Submit : External
        data object Delete : External
        data object SubmitSuccess : External
    }

    sealed interface Internal : FormEvent {
        data class LastUsedCurrency(val currency: CurrencyEntity) : Internal
        data object EmptyAmount : Internal
        data object EmptyCategory : Internal
        data object SubmitTransactionSuccess : Internal
        data object SubmitTransactionFailure : Internal
        data object DeleteTransactionSuccess : Internal
        data object DeleteTransactionFailure : Internal
        data class SuccessLoadTransaction(val entity: TransactionEntity) : Internal
        data object FailedLoadTransaction : Internal
    }
}
