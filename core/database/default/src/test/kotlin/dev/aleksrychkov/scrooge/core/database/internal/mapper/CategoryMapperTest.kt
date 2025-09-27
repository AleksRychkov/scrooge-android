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
            isUserMade = 1L
        )
        // When
        val entity: CategoryEntity = CategoryMapper.toEntity(category)
        // Then
        assertEquals(1L, entity.id)
        assertEquals(TransactionType.Expense, entity.type)
        assertEquals("Food", entity.name)
        assertEquals(true, entity.isUserMade)
    }

    @Test
    fun `When isUserMade is 0 Then entity has isUserMade false`() {
        // Given
        val category = Category(
            id = 2L,
            type = TransactionType.Income.type.toLong(),
            name = "Salary",
            isUserMade = 0L
        )
        // When
        val entity = CategoryMapper.toEntity(category)
        // Then
        assertEquals(false, entity.isUserMade)
    }
}
