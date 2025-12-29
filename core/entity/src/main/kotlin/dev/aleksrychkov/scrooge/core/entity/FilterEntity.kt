package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable

@Serializable
data class FilterEntity(
    val readableName: String = "",
    val period: PeriodDatestampEntity = PeriodDatestampEntity(Datestamp.ZERO, Datestamp.ZERO),
    val tags: ImmutableSet<String> = persistentSetOf(),
)
