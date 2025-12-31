package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf

internal sealed interface TagEffect {
    data class ShowInfoMessage(val message: String) : TagEffect
}
