package dev.aleksrychkov.scrooge.presentation.component.filters.internal

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.presentation.component.category.CategoryComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.FiltersComponent
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersEffect
import dev.aleksrychkov.scrooge.presentation.component.filters.internal.udf.FiltersState
import dev.aleksrychkov.scrooge.presentation.component.tags.TagComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface FiltersComponentInternal : FiltersComponent {
    val state: StateFlow<FiltersState>
    val effects: Flow<FiltersEffect>

    val tagModal: Value<ChildSlot<*, TagComponent>>
    val categoryModal: Value<ChildSlot<*, CategoryComponent>>

    fun onYearClicked(year: Int)
    fun onYearLongClicked(year: Int)
    fun onMonthClicked(month: Int)
    fun onMonthLongClicked(month: Int)
    fun resetFilters()

    fun openTagModal()
    fun closeTagModal()
    fun addTag(tag: TagEntity)
    fun removeTag(tag: TagEntity)

    fun openCategoryModal()
    fun closeCategoryModal()
    fun setCategory(category: CategoryEntity)
    fun removeCategory()

    fun onTransactionTypeSelected(type: TransactionType?)
}
