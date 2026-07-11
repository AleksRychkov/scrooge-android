package dev.aleksrychkov.scrooge.presentation.screen.charts.internal

import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.FilterEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.category.GetRandomCategoryResult
import dev.aleksrychkov.scrooge.feature.category.GetRandomCategoryUseCase
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.currency.GetLastUsedCurrencyUseCase
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCategoryResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCategoryUseCase
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyResult
import dev.aleksrychkov.scrooge.feature.transaction.GetMostUsedCurrencyUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ChartsFilterResolverTest {

    private val mostUsedCurrency = mockk<GetMostUsedCurrencyUseCase>()
    private val lastUsedCurrency = mockk<GetLastUsedCurrencyUseCase>()
    private val mostUsedCategory = mockk<GetMostUsedCategoryUseCase>()
    private val randomCategory = mockk<GetRandomCategoryUseCase>()
    private val resolver = ChartsFilterResolver(
        lazyOf(mostUsedCurrency),
        lazyOf(lastUsedCurrency),
        lazyOf(mostUsedCategory),
        lazyOf(randomCategory),
    )

    @Test
    fun `When period has transactions Then most used currency is selected`() = runTest {
        // Given
        val category = category(1)
        val filter = FilterEntity.currentYear().copy(category = category)
        coEvery { mostUsedCurrency(filter) } returns
            GetMostUsedCurrencyResult.Success(CurrencyEntity.USD)

        // When
        val result = resolver(filter)

        // Then
        assertEquals(CurrencyEntity.USD, result.currency)
        assertEquals(category, result.category)
        coVerify(exactly = 0) { lastUsedCurrency() }
    }

    @Test
    fun `When currency history is empty Then RUB is selected`() = runTest {
        // Given
        val filter = FilterEntity.currentYear().copy(category = category(1))
        coEvery { mostUsedCurrency(filter) } returns GetMostUsedCurrencyResult.Empty
        coEvery { lastUsedCurrency() } returns GetLastUsedCurrencyResult.Empty

        // When
        val result = resolver(filter)

        // Then
        assertEquals(CurrencyEntity.RUB, result.currency)
    }

    @Test
    fun `When period has category transactions Then most used category is selected`() = runTest {
        // Given
        val category = category(2)
        val filter = FilterEntity.currentYear().copy(currency = CurrencyEntity.RUB)
        coEvery { mostUsedCategory(filter) } returns GetMostUsedCategoryResult.Success(category)

        // When
        val result = resolver(filter)

        // Then
        assertEquals(category, result.category)
        coVerify(exactly = 0) { randomCategory(any()) }
    }

    @Test
    fun `When period has no category transactions Then random category is selected`() = runTest {
        // Given
        val category = category(3)
        val filter = FilterEntity.currentYear().copy(
            currency = CurrencyEntity.RUB,
            transactionType = TransactionType.Expense,
        )
        coEvery { mostUsedCategory(filter) } returns GetMostUsedCategoryResult.Empty
        coEvery { randomCategory(TransactionType.Expense) } returns
            GetRandomCategoryResult.Success(category)

        // When
        val result = resolver(filter)

        // Then
        assertEquals(category, result.category)
    }

    private fun category(id: Long) = CategoryEntity(
        id = id,
        name = "Category $id",
        type = TransactionType.Expense,
        iconId = CategoryEntity.DEFAULT_ICON_ID,
        color = CategoryEntity.DEFAULT_COLOR,
    )
}
