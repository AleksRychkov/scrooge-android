package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SearchCategoryDelegate {
    operator fun invoke(cmd: CategoryCommand.Search): Flow<CategoryEvent> {
        val filtered = when {
            cmd.query.isBlank() -> persistentListOf()
            else ->
                cmd.categories
                    .filter {
                        it.name.lowercase().contains(cmd.query.lowercase())
                    }
                    .toImmutableList()
        }
        return flowOf(CategoryEvent.Internal.Filtered(list = filtered.toImmutableList()))
    }
}
