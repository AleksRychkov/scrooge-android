package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class LimitDataEntity(
    val limit: LimitEntity,
    val spentAmount: Long,
)
