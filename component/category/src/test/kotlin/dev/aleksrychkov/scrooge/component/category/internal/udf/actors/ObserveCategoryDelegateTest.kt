package dev.aleksrychkov.scrooge.component.category.internal.udf.actors

import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryCommand
import dev.aleksrychkov.scrooge.component.category.internal.udf.CategoryEvent
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryResult
import dev.aleksrychkov.scrooge.feature.category.ObserveCategoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class ObserveCategoryDelegateTest {
    private val useCase: ObserveCategoryUseCase = mockk()
    private val delegate: ObserveCategoryDelegate = ObserveCategoryDelegate(lazy { useCase })

    private val cmd = CategoryCommand.ObserveCategories(
        transactionType = TransactionType.Expense,
    )

    @Test
    fun `When useCase returns Success Then result is Categories`() = runTest {
        // Given
        coEvery {
            useCase(transactionType = cmd.transactionType)
        } returns ObserveCategoryResult.Success(categories = flowOf(persistentListOf()))
        // When
        val result = delegate(cmd).first()
        // Then
        assertTrue(result is CategoryEvent.Internal.Categories)
    }

    @Test
    fun `When useCase returns Failure Then result is FailedToObserveCategories`() = runTest {
        // Given
        coEvery { useCase(transactionType = cmd.transactionType) } returns ObserveCategoryResult.Failure
        // When
        val result = delegate(cmd).first()
        // Then
        assertEquals(CategoryEvent.Internal.FailedToObserveCategories, result)
    }
}
