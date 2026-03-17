package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal

import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.screen.transactionform.TransactionFormComponent
import dev.aleksrychkov.scrooge.presentation.component.transactionform.TransactionFormComponent as FormComponent

@Suppress("TooManyFunctions")
internal interface TransactionFormComponentInternal : TransactionFormComponent {
    val transactionId: Long?
    val transactionType: TransactionType
    val formComponent: FormComponent

    fun onBackClicked()
    fun onSaveClicked()
}
