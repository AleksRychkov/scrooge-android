package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
data class PeriodTimestampEntity(val from: Long, val to: Long)
