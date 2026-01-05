package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class TransferStateEntity(
    val current: State = State.None,
) {
    enum class State {
        None, Importing, Exporting
    }
}
