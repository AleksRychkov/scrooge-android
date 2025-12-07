package dev.aleksrychkov.scrooge.component.transaction.form.internal.udf

internal sealed interface FormEffect {
    data class ShowErrorMessage(val message: String) : FormEffect
}
