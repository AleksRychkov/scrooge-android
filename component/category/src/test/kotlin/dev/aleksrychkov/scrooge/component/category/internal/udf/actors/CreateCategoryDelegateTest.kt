package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryResult
import dev.aleksrychkov.scrooge.feature.category.CreateCategoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CreateCategoryDelegateTest {
    private val useCase: CreateCategoryUseCase = mockk()
    private val delegate = CreateCategoryDelegate(lazy { useCase })

    private val cmd = CategoryCommand.CreateNewCategory(
        name = "Food",
        transactionType = TransactionType.Expense,
    )
    private val entity = CategoryEntity(
        id = 1L,
        name = "Food",
        type = TransactionType.Expense,
        isUserMade = true,
    )

    @Test
    fun `When name is blank Then result is FailedToCreateNewCategoryEmptyName`() = runTest {
        // Given
        // When
        val result = delegate(cmd = cmd.copy(name = "")).first()
        // Then
        assertEquals(CategoryEvent.Internal.FailedToCreateNewCategoryEmptyName, result)
    }

    @Test
    fun `When useCase returns DuplicateViolation Then result is FailedToCreateNewCategoryDuplicate`() =
        runTest {
            // Given
            coEvery { useCase(any()) } returns CreateCategoryResult.DuplicateViolation(entity)
            // When
            val result = delegate(cmd = cmd).first()
            // Then
            assertEquals(CategoryEvent.Internal.FailedToCreateNewCategoryDuplicate(entity), result)
        }

    @Test
    fun `When useCase returns Failure Then result is FailedToCreateNewCategory`() = runTest {
        // Given
        coEvery { useCase(any()) } returns CreateCategoryResult.Failure
        // When
        val result = delegate(cmd = cmd).first()
        // Then
        assertEquals(CategoryEvent.Internal.FailedToCreateNewCategory, result)
    }

    @Test
    fun `When useCase returns Success Then result is empty`() = runTest {
        // Given
        coEvery { useCase(any()) } returns CreateCategoryResult.Success
        // When
        val result = delegate(cmd = cmd).firstOrNull()
        // Then
        assertEquals(null, result)
    }
}
