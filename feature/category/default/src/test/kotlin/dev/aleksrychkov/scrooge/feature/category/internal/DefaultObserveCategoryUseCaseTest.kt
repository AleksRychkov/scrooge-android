package dev.aleksrychkov.scrooge.feature.category.internal

import dev.aleksrychkov.scrooge.core.database.CategoryDao
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class DefaultObserveCategoryUseCaseTest {

    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val categoryDao = mockk<CategoryDao>()
    private val useCase =
        DefaultObserveCategoryUseCase(lazy { categoryDao }, testDispatcher, mockk(relaxed = true))

    @Test
    fun `When dao returns flow Then result is success`() = runTest(testDispatcher) {
        // Given
        val categories: ImmutableList<CategoryEntity> = persistentListOf(
            CategoryEntity(
                id = 1L,
                name = "Food",
                type = TransactionType.Expense,
                isUserMade = true,
            )
        )
        val flow: Flow<ImmutableList<CategoryEntity>> = flowOf(categories)
        coEvery { categoryDao.get(TransactionType.Expense) } returns flow
        // When
        val result: ObserveCategoryResult = useCase(TransactionType.Expense)
        // Then
        when (result) {
            is ObserveCategoryResult.Success -> {
                val actual = result.categories.first()
                assertEquals(categories, actual)
            }

            else -> {
                fail("Expected Success but got $result")
            }
        }

        coVerify(exactly = 1) {
            @Suppress("UnusedFlow")
            categoryDao.get(TransactionType.Expense)
        }
    }

    @Test
    fun `When dao throws exception Then result is failure`() = runTest(testDispatcher) {
        // Given
        coEvery { categoryDao.get(TransactionType.Income) } throws IllegalStateException("DB error")
        // When
        val result: ObserveCategoryResult = useCase(TransactionType.Income)
        // Then
        assertEquals(ObserveCategoryResult.Failure, result)
        coVerify(exactly = 1) {
            @Suppress("UnusedFlow")
            categoryDao.get(TransactionType.Income)
        }
    }
}
