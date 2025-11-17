package dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.actors

import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceCommand
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceEvent
import dev.aleksrychkov.scrooge.component.transactions.internal.component.balance.udf.BalanceItem
import dev.aleksrychkov.scrooge.component.transactions.internal.utils.DateTimeUtils
import dev.aleksrychkov.scrooge.core.entity.TransactionEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsResult
import dev.aleksrychkov.scrooge.feature.transaction.GetTransactionsUseCase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.math.abs

internal class UpdateBalanceDelegate(
    private val useCase: Lazy<GetTransactionsUseCase>,
) {
    private companion object {
        const val AMOUNT_CHUNK_SIZE = 3
        const val CENTS = 100L
    }

    suspend operator fun invoke(cmd: BalanceCommand.UpdateBalance): Flow<BalanceEvent> {
        val pair = DateTimeUtils.getMonthStartEndTimestamp(cmd.instant)
        val res: GetTransactionsResult = useCase.value.invoke(
            GetTransactionsUseCase.Args(
                fromTimestamp = pair.first,
                toTimestamp = pair.second,
            )
        )
        return when (res) {
            GetTransactionsResult.Failure -> flowOf(BalanceEvent.Internal.FailedToUpdateBalance)
            is GetTransactionsResult.Success -> res.result.map {
                val triple = mapTransactionsToBalanceItems(it)
                BalanceEvent.Internal.UpdateBalance(
                    income = triple.first,
                    expense = triple.second,
                    total = triple.third,
                )
            }
        }
    }

    private fun mapTransactionsToBalanceItems(
        transactions: ImmutableList<TransactionEntity>
    ): Triple<ImmutableList<BalanceItem>, ImmutableList<BalanceItem>, ImmutableList<BalanceItem>> {
        val groupedByCurrency = transactions
            .sortedBy { it.currency }
            .groupBy { it.currency }

        val incomeItems = groupedByCurrency.map { (currency, txs) ->
            val incomeSum = txs
                .filter { it.type == TransactionType.Income }
                .sumOf { it.amount }
            BalanceItem(
                currency = currency,
                value = incomeSum.amountToValue()
            )
        }.toImmutableList()

        val expenseItems = groupedByCurrency.map { (currency, txs) ->
            val expenseSum = txs
                .filter { it.type == TransactionType.Expense }
                .sumOf { it.amount }
            BalanceItem(
                currency = currency,
                value = expenseSum.amountToValue()
            )
        }.toImmutableList()

        val totalItems = groupedByCurrency.map { (currency, txs) ->
            val incomeSum = txs.filter { it.type == TransactionType.Income }.sumOf { it.amount }
            val expenseSum = txs.filter { it.type == TransactionType.Expense }.sumOf { it.amount }
            BalanceItem(
                currency = currency,
                value = (incomeSum - expenseSum).amountToValue()
            )
        }.toImmutableList()

        return Triple(incomeItems, expenseItems, totalItems)
    }

    private fun Long.amountToValue(): String {
        val sign = if (this < 0) "-" else ""
        val absValue = abs(this)
        val major = absValue / CENTS
        val minor = absValue % CENTS
        return "$sign$major.${minor.toString().padStart(2, '0')}".formatAmount()
    }

    private fun String.formatAmount(delimiter: Char = '.'): String {
        if (this.isBlank()) return this
        val sign = if (this.contains("-")) "-" else ""

        val parts = this.replace("-", "").split(delimiter)

        val integerPart = parts[0]
            .replace("\\s".toRegex(), "")
            .reversed()
            .chunked(AMOUNT_CHUNK_SIZE)
            .joinToString(" ")
            .reversed()

        return if (parts.size > 1) {
            "$sign $integerPart$delimiter${parts[1]}"
        } else {
            "$sign $integerPart"
        }
    }
}
