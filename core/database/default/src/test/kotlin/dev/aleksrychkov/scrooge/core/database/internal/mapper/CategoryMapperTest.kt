package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.Category
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CategoryMapperTest {

    @Test
    fun `When category mapped to entity Then all fields are converted correctly`() {
        // Given
        val category = Category(
            id = 1L,
            type = TransactionType.Expense.type.toLong(),
            name = "Food",
            isDeleted = 0L,
            iconId = "iconId",
            color = 0L,
        )
        // When
        val entity: CategoryEntity = CategoryMapper.toEntity(category)
        // Then
        assertEquals(1L, entity.id)
        assertEquals(TransactionType.Expense, entity.type)
        assertEquals("Food", entity.name)
        assertEquals("iconId", entity.iconId)
    }
}
