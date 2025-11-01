package dev.aleksrychkov.scrooge.component.transactionform.internal.udf

internal sealed interface FormEffect {
    data class ShowErrorMessage(val message: String) : FormEffect
}
