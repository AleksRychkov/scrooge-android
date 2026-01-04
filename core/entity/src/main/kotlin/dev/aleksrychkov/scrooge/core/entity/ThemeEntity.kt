package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class ThemeEntity(val type: Type) {
    enum class Type {
        Light, Dark, System
    }
}
