package dev.aleksrychkov.scrooge.feature.category

import dev.aleksrychkov.scrooge.core.entity.TransactionType

fun interface SwapOrderIndexCategoryUseCase {
    suspend operator fun invoke(fromIndex: Int, toIndex: Int, type: TransactionType)
}
