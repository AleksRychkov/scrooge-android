package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SearchCategoryDelegateTest {
    private val delegate: SearchCategoryDelegate = SearchCategoryDelegate()

    private val categories = listOf(
        CategoryEntity(
            id = 0L,
            name = "1",
            type = TransactionType.Expense,
            iconId = "",
        ),
        CategoryEntity(
            id = 0L,
            name = "2",
            type = TransactionType.Expense,
            iconId = "",
        ),
    )

    @Test
    fun `When query is blank Then result list is empty`() = runTest {
        // Given
        val cmd = CategoryCommand.Search(
            query = "",
            categories = categories,
        )
        // When
        val result = delegate(cmd).first()
        // Then
        assertTrue(result is CategoryEvent.Internal.Filtered)
        assertTrue((result as CategoryEvent.Internal.Filtered).list.isEmpty())
    }

    @Test
    fun `When query Then result list is not empty`() = runTest {
        // Given
        val query = "1"
        val cmd = CategoryCommand.Search(
            query = query,
            categories = categories,
        )
        // When
        val result = delegate(cmd).first()
        // Then
        assertTrue(result is CategoryEvent.Internal.Filtered)
        assertTrue((result as CategoryEvent.Internal.Filtered).list.size == 1)
        assertEquals(query, result.list.first().name)
    }
}
