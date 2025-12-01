package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.DeleteCategoryResult
import dev.aleksrychkov.scrooge.feature.category.DeleteCategoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DeleteCategoryDelegateTest {
    private val useCase: DeleteCategoryUseCase = mockk()
    private val delegate: DeleteCategoryDelegate = DeleteCategoryDelegate(lazy { useCase })

    private val entity = CategoryEntity(
        id = 1L,
        name = "Food",
        type = TransactionType.Expense,
        iconId = "",
    )
    private val cmd = CategoryCommand.Delete(category = entity)

    @Test
    fun `When useCase returns Success Then result is DeletedCategory`() = runTest {
        // Given
        val expected = CategoryEvent.Internal.DeletedCategory(cmd.category)
        coEvery { useCase(entity) } returns DeleteCategoryResult.Success
        // When
        val result = delegate(cmd).firstOrNull()
        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `When useCase returns Failure Then result is FailedToDeleteCategory`() = runTest {
        // Given
        coEvery { useCase(entity) } returns DeleteCategoryResult.Failure
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(CategoryEvent.Internal.FailedToDeleteCategory, result)
    }
}
