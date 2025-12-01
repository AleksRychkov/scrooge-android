package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.DeleteCategoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultDeleteCategoryUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val categoryDao = mockk<CategoryDao>(relaxed = true)
    private val useCase = DefaultDeleteCategoryUseCase(lazy { categoryDao }, testDispatcher)

    @Test
    fun `When category is user made Then Success is returned`() = runTest(testDispatcher) {
        // Given
        val category = CategoryEntity(
            id = 1L,
            name = "Food",
            type = TransactionType.Expense,
            iconId = "",
        )

        // When
        val result: DeleteCategoryResult = useCase(category)

        // Then
        assertEquals(DeleteCategoryResult.Success, result)
        coVerify(exactly = 1) { categoryDao.delete(category.id) }
    }

    @Test
    fun `When dao throws exception Then failure result is returned`() = runTest(testDispatcher) {
        // Given
        val category = CategoryEntity(
            id = 3L,
            name = "Transport",
            type = TransactionType.Expense,
            iconId = "",
        )
        coEvery { categoryDao.delete(category.id) } throws IllegalStateException("DB error")
        // When
        val result: DeleteCategoryResult = useCase(category)
        // Then
        assertEquals(
            DeleteCategoryResult.Failure,
            result
        )
    }
}
