package dev.aleksrychkov.scrooge.presentation.component.category.internal.udf

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcons
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CategoryState(
    val isEditable: Boolean = true,
    val transactionType: TransactionType? = TransactionType.Income,
    val categories: ImmutableList<CategoryItem> = persistentListOf(),
    val categoriesHash: Int = 0,
    val filtered: ImmutableList<CategoryItem> = persistentListOf(),
    val searchQuery: String = "",
)

@Immutable
internal data class CategoryItem(
    val ref: CategoryEntity,
    val color: Color,
    val tint: Color,
    val imageVector: ImageVector,
) {
    companion object {
        internal fun map(category: CategoryEntity): CategoryItem {
            val color = Color(category.color)
            val imageVector: ImageVector = CategoryIcons
                .find { it.id == category.iconId }?.icon ?: UncategorizedIcon.icon
            return CategoryItem(
                ref = category,
                color = color,
                tint = color,
                imageVector = imageVector,
            )
        }
    }
}
