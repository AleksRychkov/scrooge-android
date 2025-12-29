@file:OptIn(ExperimentalTime::class)

package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.ExperimentalTime

@Immutable
internal data class FormState(
    val isLoading: Boolean = false,
    val transactionId: Long? = null,
    val transactionType: TransactionType = TransactionType.Expense,
    val amount: String = "",
    val datestamp: Datestamp = Datestamp.now(),
    val category: CategoryEntity? = null,
    val tags: ImmutableList<TagEntity> = persistentListOf(),
    val currency: CurrencyEntity = CurrencyEntity.RUB,
)
