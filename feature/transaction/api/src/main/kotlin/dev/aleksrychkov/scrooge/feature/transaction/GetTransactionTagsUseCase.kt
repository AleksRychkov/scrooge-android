package dev.aleksrychkov.scrooge.feature.transaction

import kotlinx.collections.immutable.ImmutableSet

fun interface GetTransactionTagsUseCase {
    suspend operator fun invoke(): ImmutableSet<String>
}
