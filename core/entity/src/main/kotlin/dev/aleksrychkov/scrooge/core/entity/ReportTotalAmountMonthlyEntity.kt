package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ReportTotalAmountMonthlyEntity(
    val result: SerializableImmutableMap<LocalDate, ReportTotalAmountEntity> = persistentMapOf(),
)
