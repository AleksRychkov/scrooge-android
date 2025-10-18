package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DefaultCreateCategoryUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private val categoryDao = mockk<CategoryDao>(relaxed = true)
    private val useCase = DefaultCreateCategoryUseCase(
        categoryDao = lazy { categoryDao },
        ioDispatcher = testDispatcher,
        defaultCategories = mockk(relaxed = true)
    )

    @Test
    fun `When category does not exist Then Success is returned`() = runTest(testDispatcher) {
        // Given
        val category = CategoryEntity(
            id = 0L,
            name = "Food",
            type = TransactionType.Expense,
            isUserMade = true,
        )
        coEvery { categoryDao.getByName(category.name, category.type) } returns null

        // When
        val result = useCase(category)

        // Then
        assertEquals(CreateCategoryResult.Success, result)
        coVerify(exactly = 1) { categoryDao.create(category.name, category.type) }
    }

    @Test
    fun `When category with same name and type exists Then DuplicateViolation is returned`() =
        runTest(testDispatcher) {
            // Given
            val existing = CategoryEntity(
                id = 1L,
                name = "Food",
                type = TransactionType.Expense,
                isUserMade = true,
            )
            val newCategory = CategoryEntity(
                id = 2L,
                name = "Food",
                type = TransactionType.Expense,
                isUserMade = true,
            )
            coEvery {
                categoryDao.getByName(
                    newCategory.name,
                    newCategory.type
                )
            } returns existing

            // When
            val result = useCase(newCategory)

            // Then
            assertEquals(CreateCategoryResult.DuplicateViolation(existing), result)
            coVerify(exactly = 0) { categoryDao.create(any(), any()) }
        }

    @Test
    fun `When dao throws exception Then Failure is returned`() = runTest(testDispatcher) {
        // Given
        val category = CategoryEntity(
            id = 0L,
            name = "Food",
            type = TransactionType.Expense,
            isUserMade = true,
        )
        coEvery {
            categoryDao.getByName(category.name, category.type)
        } throws IllegalStateException("DB error")

        // When
        val result = useCase(category)

        // Then
        assertEquals(CreateCategoryResult.Failure, result)
    }
}
