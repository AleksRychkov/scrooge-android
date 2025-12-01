package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryResult
import dev.aleksrychkov.scrooge.feature.category.RestoreCategoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

internal class RestoreCategoryDelegateTest {
    private val useCase: RestoreCategoryUseCase = mockk()
    private val delegate: RestoreCategoryDelegate = RestoreCategoryDelegate(lazy { useCase })

    private val entity = CategoryEntity(
        id = 1L,
        name = "Food",
        type = TransactionType.Expense,
        iconId = "",
    )
    private val cmd = CategoryCommand.Restore(category = entity)

    @Test
    fun `When useCase returns Success Then result is empty flow`() = runTest {
        // Given
        coEvery { useCase(entity) } returns RestoreCategoryResult.Success
        // When
        val result = delegate(cmd).firstOrNull()
        // Then
        assertNull(result)
    }

    @Test
    fun `When useCase returns Failure Then result is FailedToRestoreCategory`() = runTest {
        // Given
        coEvery { useCase(entity) } returns RestoreCategoryResult.Failure
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(CategoryEvent.Internal.FailedToRestoreCategory, result)
    }
}
