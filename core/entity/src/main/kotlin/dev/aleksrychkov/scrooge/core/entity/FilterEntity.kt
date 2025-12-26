package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable

@Serializable
data class FilterEntity(
    val period: PeriodTimestampEntity = PeriodTimestampEntity(0, 0),
    val tags: ImmutableSet<String> = persistentSetOf(),
)
