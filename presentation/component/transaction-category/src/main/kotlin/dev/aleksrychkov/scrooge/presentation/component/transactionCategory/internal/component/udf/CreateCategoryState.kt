package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.CategoryIcon
import dev.aleksrychkov.scrooge.core.resources.UncategorizedIcon
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class CreateCategoryState(
    val isLoading: Boolean = false,
    val isDone: Boolean = false,
    val name: String = "",
    val transactionType: TransactionType = TransactionType.Expense,
    val selectedCategoryIcon: CategoryIcon = UncategorizedIcon,
    val selectedCategoryColor: Int = 0xFF212121.toInt(),
    val availableIcons: ImmutableList<CategoryIcon> = persistentListOf(),
    val availableColors: ImmutableList<ColorWrapper> = persistentListOf(),
) {
    @Immutable
    class ColorWrapper(val color: Color, val argb: Int)
}
