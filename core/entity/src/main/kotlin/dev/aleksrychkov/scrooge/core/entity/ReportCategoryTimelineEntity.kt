package dev.aleksrychkov.scrooge.core.entity

import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ReportCategoryTimelineEntity(
    val series: SerializableImmutableList<CategorySeries> = persistentListOf(),
) {
    @Serializable
    data class CategorySeries(
        val category: CategoryEntity,
        val points: SerializableImmutableList<Point> = persistentListOf(),
    )

    @Serializable
    data class Point(
        val month: LocalDate,
        val amount: Long,
    )
}
