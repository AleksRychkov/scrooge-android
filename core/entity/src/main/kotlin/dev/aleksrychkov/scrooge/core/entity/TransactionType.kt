package dev.aleksrychkov.scrooge.core.entity

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType(val type: Int) {
    Income(0),
    Expense(1);

    companion object {
        fun from(value: Int): TransactionType = when (value) {
            0 -> Income
            1 -> Expense
            else -> error("Undefined TransactionType: $value")
        }
    }
}
