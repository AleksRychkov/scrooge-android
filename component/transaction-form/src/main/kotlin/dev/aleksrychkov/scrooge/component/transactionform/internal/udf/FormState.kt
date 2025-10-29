@file:OptIn(ExperimentalTime::class)

package dev.aleksrychkov.scrooge.component.transactionform.internal.udf

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.scrooge.component.transactionform.internal.toDateString
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.CurrencyEntity
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Immutable
internal data class FormState(
    val isLoading: Boolean = false,
    val transactionId: Long? = null,
    val transactionType: TransactionType = TransactionType.Expense,
    val amount: String = "",
    val timestamp: Instant = Clock.System.now(),
    val timestampReadable: String = timestamp.toDateString(),
    val category: CategoryEntity? = null,
    val tags: ImmutableSet<TagEntity> = persistentSetOf(),
    val currency: CurrencyEntity = CurrencyEntity.RUB,
)
