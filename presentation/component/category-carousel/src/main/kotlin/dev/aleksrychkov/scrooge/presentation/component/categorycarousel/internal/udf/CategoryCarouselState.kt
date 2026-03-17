package dev.aleksrychkov.scrooge.presentation.component.categorycarousel.internal.udf

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.resources.CategoryIcons
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CategoryCarouselState(
    val isLoading: Boolean = false,
    val carousel: ImmutableList<CarouselItem> = persistentListOf(),
    val selectedCategory: CategoryEntity? = null,
)

@Immutable
internal data class CarouselItem(
    val id: Long,
    val ref: CategoryEntity,
    val name: String,
    val color: Color,
    val tint: Color,
    val imageVector: ImageVector,
) {
    companion object {

        fun map(category: CategoryEntity): CarouselItem {
            val color = Color(category.color)
            val imageVector: ImageVector = CategoryIcons
                .find { it.id == category.iconId }?.icon ?: UncategorizedIcon.icon
            return CarouselItem(
                id = category.id,
                ref = category,
                name = category.name,
                color = color,
                tint = color,
                imageVector = imageVector,
            )
        }
    }
}
