package dev.aleksrychkov.scrooge.core.database.internal.fileadapter

import dev.aleksrychkov.scrooge.core.database.Category
import dev.aleksrychkov.scrooge.core.database.Scrooge
import dev.aleksrychkov.scrooge.core.database.TTransaction
import dev.aleksrychkov.scrooge.core.database.Tag
import dev.aleksrychkov.scrooge.core.database.TransactionTag
import dev.aleksrychkov.scrooge.core.database.fileadapter.DatabaseFileAdapter
import dev.aleksrychkov.scrooge.core.database.internal.database.DatabaseProvider
import dev.aleksrychkov.scrooge.core.database.internal.serializer.DatabaseSerializer
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import java.io.DataInput
import java.io.DataOutput

internal class DefaultDatabaseFileAdapter(
    private val dbProvider: DatabaseProvider,
    private val ioDispatcher: CoroutineDispatcher,
) : DatabaseFileAdapter {

    private val serializer: DatabaseSerializer by lazy { DatabaseSerializer() }

    private val database: Scrooge
        get() = dbProvider.scrooge

    override suspend fun write(output: DataOutput) = withContext(ioDispatcher) {
        val minMax = database.transactionQueries
            .minMaxDatestamp()
            .executeAsOneOrNull() ?: return@withContext

        serializer.writeSignature(output)
        serializer.writeVersion(output)

        database.categoryQueries.selectAll().executeAsList().forEach {
            serializer.serialize(it, output)
        }
        database.tagQueries.selectAllTransactionTag().executeAsList().forEach {
            serializer.serialize(it, output)
        }

        val fromDatestamp = Datestamp(minMax.minDatestamp!!)
        val toDatestamp = Datestamp(minMax.maxDatestamp!!)
        var index: Datestamp = fromDatestamp
        while (index.value <= toDatestamp.value) {
            val fromDatestamp = index.value
            val nextMonth = index.date.plus(1, DateTimeUnit.MONTH)
            val toDatestamp = Datestamp.from(nextMonth).value

            database.transactionQueries
                .selectFromTo(fromDatestamp = fromDatestamp, toDatestamp = toDatestamp)
                .executeAsList()
                .forEach {
                    val t = TTransaction(
                        it.id,
                        it.amount,
                        it.datestamp,
                        it.type,
                        it.categoryId ?: -1,
                        it.currencyCode
                    )
                    serializer.serialize(t, output)
                }
            index = Datestamp.from(nextMonth.plus(1, DateTimeUnit.DAY))
        }
    }

    override suspend fun read(input: DataInput) = withContext(ioDispatcher + NonCancellable) {
        database.transaction {
            val categoryCallback: suspend (Category) -> Unit = { category: Category ->
                database.categoryQueries.add(
                    id = category.id,
                    name = category.name,
                    type = category.type,
                    iconId = category.iconId,
                    color = category.color,
                    orderIndex = category.orderIndex,
                )
            }
            val tagCallback: suspend (Tag) -> Unit = { tag: Tag ->
                database.tagQueries.add(id = tag.id, name = tag.name)
            }
            val transactionCallback: suspend (TTransaction) -> Unit = { transaction: TTransaction ->
                database.transactionQueries.add(
                    id = transaction.id,
                    amount = transaction.amount,
                    datestamp = transaction.datestamp,
                    type = transaction.type,
                    categoryId = transaction.categoryId,
                    currencyCode = transaction.currencyCode,
                )
            }
            val transactionTagCallback: suspend (TransactionTag) -> Unit =
                { transactionTag: TransactionTag ->
                    database.tagQueries.createTransactioTag(
                        transactionId = transactionTag.transactionId,
                        tagId = transactionTag.tagId,
                    )
                }
            serializer.deserialize(
                input,
                categoryCallback = categoryCallback,
                tagCallback = tagCallback,
                transactionCallback = transactionCallback,
                transactionTagCallback = transactionTagCallback,
            )
        }
    }
}
