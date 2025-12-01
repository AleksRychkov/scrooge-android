package dev.aleksrychkov.scrooge.core.database.internal.mapper

import dev.aleksrychkov.scrooge.core.database.TTransaction
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.persistentSetOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class TransactionMapperTest {

    @Test
    fun `When transaction mapped to entity Then all fields are converted correctly`() {
        // Given
        val transaction = TTransaction(
            id = 1L,
            amount = 1000L,
            timestamp = 123456789L,
            type = TransactionType.Expense.type.toLong(),
            category = "Food",
            categoryIconId = "Savings",
            tags = "groceries|dinner",
            currencyCode = "USD"
        )
        // When
        val entity: TransactionEntity = TransactionMapper.toEntity(transaction)
        // Then
        assertEquals(1L, entity.id)
        assertEquals(1000L, entity.amount)
        assertEquals(123456789L, entity.timestamp)
        assertEquals(TransactionType.Expense, entity.type)
        assertEquals("Food", entity.category)
        assertEquals(persistentSetOf("groceries", "dinner"), entity.tags)
        assertEquals(CurrencyEntity.USD, entity.currency)
    }

    @Test
    fun `When tags contain separator Then it is replaced with space`() {
        // Given
        val tags = setOf("gro|cery", "food")
        // When
        val dbTags = TransactionMapper.toDatabaseTags(tags)
        // Then
        assertEquals("gro cery|food", dbTags)
    }

    @Test
    fun `When tags are null Then database tags are null`() {
        // Given
        val tags: Set<String>? = null
        // When
        val dbTags = TransactionMapper.toDatabaseTags(tags)
        // Then
        assertNull(dbTags)
    }

    @Test
    fun `When tags are empty Then database tags are null`() {
        // Given
        val tags: Set<String> = emptySet()
        // When
        val dbTags = TransactionMapper.toDatabaseTags(tags)
        // Then
        assertNull(dbTags)
    }

    @Test
    fun `When tags string is null Then entity has empty tags set`() {
        // Given
        val transaction = TTransaction(
            id = 2L,
            amount = 500L,
            timestamp = 999999L,
            type = TransactionType.Income.type.toLong(),
            category = "Salary",
            categoryIconId = "Savings",
            tags = null,
            currencyCode = "EUR"
        )
        // When
        val entity = TransactionMapper.toEntity(transaction)
        // Then
        assertEquals(persistentSetOf<String>(), entity.tags)
    }
}
