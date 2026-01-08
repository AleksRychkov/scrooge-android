package dev.aleksrychkov.scrooge.core.database.internal.serializer

import dev.aleksrychkov.scrooge.core.database.Category
import dev.aleksrychkov.scrooge.core.database.TTransaction
import dev.aleksrychkov.scrooge.core.database.Tag
import dev.aleksrychkov.scrooge.core.database.TransactionTag
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

internal class DatabaseSerializerTest {

    @Test
    fun `serialize and deserialize all types`() = runBlocking {
        val serializer = DatabaseSerializer()

        // Prepare test objects
        val category = Category(
            id = 1L,
            name = "Food",
            type = 2L,
            iconId = "icon_food",
            color = 0xFF0000L,
            orderIndex = 0L
        )

        val tag = Tag(
            id = 10L,
            name = "Urgent"
        )

        val transaction = TTransaction(
            id = 100L,
            amount = 5000L,
            datestamp = 1680000000L,
            type = 1L,
            categoryId = category.id,
            currencyCode = "USD"
        )

        val transactionTag = TransactionTag(
            transactionId = transaction.id,
            tagId = tag.id
        )

        // Serialize all objects into a byte array
        val baos = ByteArrayOutputStream()
        val output = DataOutputStream(baos)

        serializer.writeVersion(output) // version
        serializer.serialize(category, output)
        serializer.serialize(tag, output)
        serializer.serialize(transaction, output)
        serializer.serialize(transactionTag, output)

        output.flush()

        // Deserialize from the byte array
        val input = DataInputStream(ByteArrayInputStream(baos.toByteArray()))

        val deserializedCategories = mutableListOf<Category>()
        val deserializedTags = mutableListOf<Tag>()
        val deserializedTransactions = mutableListOf<TTransaction>()
        val deserializedTransactionTags = mutableListOf<TransactionTag>()

        serializer.deserialize(
            input,
            categoryCallback = { deserializedCategories.add(it) },
            tagCallback = { deserializedTags.add(it) },
            transactionCallback = { deserializedTransactions.add(it) },
            transactionTagCallback = { deserializedTransactionTags.add(it) }
        )

        // Assertions
        assertEquals(1, deserializedCategories.size)
        assertEquals(category, deserializedCategories.first())

        assertEquals(1, deserializedTags.size)
        assertEquals(tag, deserializedTags.first())

        assertEquals(1, deserializedTransactions.size)
        assertEquals(transaction, deserializedTransactions.first())

        assertEquals(1, deserializedTransactionTags.size)
        assertEquals(transactionTag, deserializedTransactionTags.first())
    }

    @Test
    fun `deserialize unsupported version throws error`() = runBlocking {
        val serializer = DatabaseSerializer()

        val baos = ByteArrayOutputStream()
        val output = DataOutputStream(baos)

        // Write an unsupported version (e.g., 999L)
        output.writeLong(999L)
        output.flush()

        val input = DataInputStream(ByteArrayInputStream(baos.toByteArray()))

        var thrown: Throwable? = null
        try {
            serializer.deserialize(
                input,
                categoryCallback = {},
                tagCallback = {},
                transactionCallback = {},
                transactionTagCallback = {}
            )
        } catch (e: Throwable) {
            thrown = e
        }

        assert(thrown is IllegalStateException || thrown is RuntimeException) {
            "Expected error for unsupported version, but got $thrown"
        }
    }
}
