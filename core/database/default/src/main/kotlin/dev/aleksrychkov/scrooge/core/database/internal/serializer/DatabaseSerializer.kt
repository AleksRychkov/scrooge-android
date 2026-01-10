package dev.aleksrychkov.scrooge.core.database.internal.serializer

import dev.aleksrychkov.scrooge.core.database.Category
import dev.aleksrychkov.scrooge.core.database.TTransaction
import dev.aleksrychkov.scrooge.core.database.Tag
import dev.aleksrychkov.scrooge.core.database.TransactionTag
import java.io.DataInput
import java.io.DataOutput
import java.io.EOFException
import java.io.IOException

internal class DatabaseSerializer {

    private companion object {
        const val SIGNATURE = "dev.aleksrychkov.scrooge"

        const val TYPE_CATEGORY = 1
        const val TYPE_TAG = 2
        const val TYPE_TRANSACTION = 3
        const val TYPE_TRANSACTION_TAG = 4

        // initial
        const val VERSION_1 = 1L

        // added `comment` column to TTransaction
        const val VERSION_2 = 2L

        // current version
        const val VERSION = VERSION_2
    }

    fun writeSignature(output: DataOutput) {
        output.writeUTF(SIGNATURE)
    }

    fun writeVersion(output: DataOutput) {
        output.writeLong(VERSION)
    }

    fun serialize(category: Category, output: DataOutput) {
        output.writeInt(TYPE_CATEGORY)
        output.writeLong(category.id)
        output.writeUTF(category.name)
        output.writeLong(category.type)
        output.writeUTF(category.iconId)
        output.writeLong(category.color)
        output.writeLong(category.orderIndex)
    }

    fun serialize(tag: Tag, output: DataOutput) {
        output.writeInt(TYPE_TAG)
        output.writeLong(tag.id)
        output.writeUTF(tag.name)
    }

    fun serialize(transaction: TTransaction, output: DataOutput) {
        output.writeInt(TYPE_TRANSACTION)
        output.writeLong(transaction.id)
        output.writeLong(transaction.amount)
        output.writeLong(transaction.datestamp)
        output.writeLong(transaction.type)
        output.writeLong(transaction.categoryId)
        output.writeUTF(transaction.currencyCode)
        output.writeUTF(transaction.comment ?: "") // VERSION_2
    }

    fun serialize(transactionTag: TransactionTag, output: DataOutput) {
        output.writeInt(TYPE_TRANSACTION_TAG)
        output.writeLong(transactionTag.transactionId)
        output.writeLong(transactionTag.tagId)
    }

    @Suppress("TooGenericExceptionThrown")
    suspend fun deserialize(
        input: DataInput,
        categoryCallback: suspend (Category) -> Unit,
        tagCallback: suspend (Tag) -> Unit,
        transactionCallback: suspend (TTransaction) -> Unit,
        transactionTagCallback: suspend (TransactionTag) -> Unit,
    ) {
        val signature = input.readUTF()
        if (signature != SIGNATURE) {
            error("Invalid signature: $signature")
        }

        val version = input.readLong()
        if (version !in VERSION_1..VERSION) {
            error("Unsupported version: $VERSION_1")
        }

        try {
            while (true) {
                when (val type = input.readInt()) {
                    TYPE_CATEGORY -> deserializeCategory(input, categoryCallback)
                    TYPE_TAG -> deserializeTag(input, tagCallback)
                    TYPE_TRANSACTION -> deserializeTransaction(input, version, transactionCallback)
                    TYPE_TRANSACTION_TAG -> deserializeTransactionTag(input, transactionTagCallback)
                    else -> error("Unknown type: $type")
                }
            }
        } catch (_: EOFException) {
            // we're done!
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private suspend fun deserializeCategory(
        input: DataInput,
        categoryCallback: suspend (Category) -> Unit
    ) {
        val id = input.readLong()
        val name = input.readUTF()
        val type = input.readLong()
        val iconId = input.readUTF()
        val color = input.readLong()
        val orderIndex = input.readLong()

        categoryCallback(
            Category(
                id = id,
                name = name,
                type = type,
                iconId = iconId,
                color = color,
                orderIndex = orderIndex,
            )
        )
    }

    private suspend fun deserializeTag(input: DataInput, tagCallback: suspend (Tag) -> Unit) {
        val id = input.readLong()
        val name = input.readUTF()

        tagCallback(
            Tag(
                id = id,
                name = name,
            )
        )
    }

    private suspend fun deserializeTransaction(
        input: DataInput,
        version: Long,
        transactionCallback: suspend (TTransaction) -> Unit
    ) {
        val id = input.readLong()
        val amount = input.readLong()
        val datestamp = input.readLong()
        val type = input.readLong()
        val categoryId = input.readLong()
        val currencyCode = input.readUTF()

        val comment: String? = if (version >= VERSION_2) {
            input.readUTF()
        } else {
            null
        }

        transactionCallback(
            TTransaction(
                id = id,
                amount = amount,
                datestamp = datestamp,
                type = type,
                categoryId = categoryId,
                currencyCode = currencyCode,
                comment = comment,
            )
        )
    }

    private suspend fun deserializeTransactionTag(
        input: DataInput,
        transactionTagCallback: suspend (TransactionTag) -> Unit
    ) {
        val transactionId = input.readLong()
        val tagId = input.readLong()

        transactionTagCallback(
            TransactionTag(
                transactionId = transactionId,
                tagId = tagId,
            )
        )
    }
}
