package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class ThemeEntity(val type: Type = Type.System) {
    enum class Type {
        Light, Dark, System, Undefined,
    }
}
