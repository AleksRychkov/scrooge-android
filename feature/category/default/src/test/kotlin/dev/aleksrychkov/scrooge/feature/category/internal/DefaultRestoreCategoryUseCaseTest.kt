package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultRestoreCategoryUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val categoryDao = mockk<CategoryDao>(relaxed = true)
    private val useCase = DefaultRestoreCategoryUseCase(lazy { categoryDao }, testDispatcher)

    @Test
    fun `When dao throws exception Then failure result is returned`() = runTest(testDispatcher) {
        // Given
        val category = CategoryEntity(
            id = 3L,
            name = "Transport",
            type = TransactionType.Expense,
            isUserMade = true,
        )
        coEvery { categoryDao.restore(category.id) } throws IllegalStateException("DB error")
        // When
        val result: RestoreCategoryResult = useCase(category)
        // Then
        assertEquals(
            RestoreCategoryResult.Failure,
            result
        )
    }

    @Test
    fun `When category is restored Then Success is returned`() = runTest(testDispatcher) {
        // Given
        val category = CategoryEntity(
            id = 1L,
            name = "Food",
            type = TransactionType.Expense,
            isUserMade = true,
        )
        // When
        val result: RestoreCategoryResult = useCase(category)
        // Then
        assertEquals(RestoreCategoryResult.Success, result)
        coVerify(exactly = 1) { categoryDao.restore(category.id) }
    }
}
