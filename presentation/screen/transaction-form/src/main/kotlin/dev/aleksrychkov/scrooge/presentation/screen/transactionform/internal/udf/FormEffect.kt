package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf

internal sealed interface FormEffect {
    data class ShowErrorMessage(val message: String) : FormEffect
}
