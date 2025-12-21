package dev.aleksrychkov.scrooge.component.transaction.form.internal.udf

import dev.aleksrychkov.scrooge.component.transaction.form.internal.utils.toDateString
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.entity.amountToString
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class FormReducer(
    private val resourceManager: ResourceManager,
) : Reducer<FormState, FormEvent, FormCommand, FormEffect> {

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun reduce(
        event: FormEvent,
        state: FormState
    ): ReducerResult<FormState, FormCommand, FormEffect> {
        return when (event) {
            is FormEvent.External.Init -> state.reduceWith(event) {
                if (event.transactionId != null) {
                    command {
                        listOf(FormCommand.LoadTransaction(transactionId = event.transactionId))
                    }
                    state {
                        copy(isLoading = true)
                    }
                } else {
                    command {
                        listOf(FormCommand.GetLastUsedCurrency)
                    }
                }
            }

            is FormEvent.External.AddTag -> state.reduceWith(event) {
                state {
                    val tmp = tags.toMutableSet()
                    tmp.add(event.tag)
                    copy(tags = tmp.toPersistentList())
                }
            }

            is FormEvent.External.RemoveTag -> state.reduceWith(event) {
                state {
                    val tmp = tags.toMutableSet()
                    tmp.remove(event.tag)
                    copy(tags = tmp.toPersistentList())
                }
            }

            is FormEvent.External.SetAmount -> state.reduceWith(event) {
                state {
                    copy(amount = event.amount)
                }
            }

            is FormEvent.External.SetCategory -> state.reduceWith(event) {
                state {
                    copy(category = event.category)
                }
            }

            is FormEvent.External.SetDate -> state.reduceWith(event) {
                state {
                    val instant = Instant.fromEpochMilliseconds(event.timestamp)
                    copy(
                        timestamp = instant,
                        timestampReadable = instant.toDateString(),
                    )
                }
            }

            is FormEvent.Internal.LastUsedCurrency -> state.reduceWith(event) {
                state {
                    copy(currency = event.currency)
                }
            }

            is FormEvent.External.SetCurrency -> state.reduceWith(event) {
                state {
                    copy(currency = event.currency)
                }
            }

            FormEvent.External.Submit -> state.reduceWith(event) {
                state {
                    copy(isLoading = true)
                }
                command {
                    listOf(FormCommand.Submit(state = state.copy()))
                }
            }

            FormEvent.External.Delete -> state.reduceWith(event) {
                state {
                    copy(isLoading = true)
                }
                command {
                    listOf(FormCommand.Delete(state = state.copy()))
                }
            }

            FormEvent.Internal.DeleteTransactionSuccess -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                command {
                    listOf(FormCommand.Exit)
                }
            }

            FormEvent.Internal.DeleteTransactionFailure -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager.getString(Resources.string.form_failed_delete)
                    listOf(FormEffect.ShowErrorMessage(message = msg))
                }
            }

            FormEvent.External.SubmitSuccess -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
            }

            FormEvent.Internal.EmptyAmount -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager.getString(Resources.string.form_empty_amount)
                    listOf(FormEffect.ShowErrorMessage(message = msg))
                }
            }

            FormEvent.Internal.EmptyCategory -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager.getString(Resources.string.form_empty_category)
                    listOf(FormEffect.ShowErrorMessage(message = msg))
                }
            }

            FormEvent.Internal.SubmitTransactionFailure -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager.getString(Resources.string.form_failed_submit)
                    listOf(FormEffect.ShowErrorMessage(message = msg))
                }
            }

            FormEvent.Internal.SubmitTransactionSuccess -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                command {
                    listOf(FormCommand.SetLastUsedCurrency(state.currency), FormCommand.Exit)
                }
            }

            is FormEvent.Internal.SuccessLoadTransaction -> state.reduceWith(event) {
                val timestamp = Instant.fromEpochMilliseconds(event.entity.timestamp)
                state {
                    copy(
                        isLoading = false,
                        transactionType = event.entity.type,
                        amount = event.entity.amount.amountToString(),
                        timestamp = timestamp,
                        timestampReadable = timestamp.toDateString(),
                        category = event.entity.category,
                        tags = event.entity.tags.map { TagEntity.from(it) }.toImmutableList(),
                        currency = event.entity.currency,
                    )
                }
            }

            FormEvent.Internal.FailedLoadTransaction -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                command {
                    listOf(FormCommand.Exit)
                }
            }
        }
    }
}
