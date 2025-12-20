package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.datetime.Month
import kotlinx.serialization.Serializable

@Serializable
data class ReportTotalAmountMonthlyEntity(
    val result: ImmutableMap<Month, ReportTotalAmountEntity> = persistentMapOf(),
)
