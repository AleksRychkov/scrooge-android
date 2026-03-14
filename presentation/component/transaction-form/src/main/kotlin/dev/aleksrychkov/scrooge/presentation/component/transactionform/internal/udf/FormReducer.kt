package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf

import dev.aleksrychkov.scrooge.core.entity.Datestamp
import dev.aleksrychkov.scrooge.core.entity.amountToString
import dev.aleksrychkov.scrooge.core.resources.ResourceManager
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.AmountInputTransformation
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormCommand.Delete
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormCommand.Exit
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormCommand.GetLastUsedCurrency
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormCommand.LoadTransaction
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormCommand.SetLastUsedCurrency
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormCommand.Submit
import dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.udf.FormEffect.ShowErrorMessage
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlin.time.Instant
import dev.aleksrychkov.scrooge.core.resources.R as Resources

internal class FormReducer(
    private val resourceManager: ResourceManager,
) : Reducer<FormState, FormEvent, FormCommand, FormEffect> {

    private val transformation: AmountInputTransformation by lazy {
        AmountInputTransformation()
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun reduce(
        event: FormEvent,
        state: FormState
    ): ReducerResult<FormState, FormCommand, FormEffect> {
        return when (event) {
            is FormEvent.External.Init -> state.reduceWith(event) {
                if (event.transactionId != null) {
                    command {
                        listOf(LoadTransaction(transactionId = event.transactionId))
                    }
                    state {
                        copy(isLoading = true)
                    }
                } else {
                    command {
                        listOf(GetLastUsedCurrency)
                    }
                    state {
                        copy(datestampReadable = datestamp.toReadable())
                    }
                }
            }

            is FormEvent.External.AddTag -> state.reduceWith(event) {
                state {
                    val tmp = tags.toMutableSet()
                    tmp.add(event.tag)
                    copy(tags = tmp.toImmutableSet())
                }
            }

            is FormEvent.External.RemoveTag -> state.reduceWith(event) {
                state {
                    val tmp = tags.toMutableSet()
                    tmp.remove(event.tag)
                    copy(tags = tmp.toImmutableSet())
                }
            }

            is FormEvent.External.SetAmount -> state.reduceWith(event) {
                state {
                    copy(amount = event.amount)
                }
            }

            is FormEvent.External.AppendAmount -> state.reduceWith(event) {
                if (event.value.isBlank() || event.value.length > 1) return@reduceWith
                val newAmount = state.amount + event.value
                state {
                    copy(amount = transformation.transform(newAmount))
                }
            }

            FormEvent.External.RemoveLastFromAmount -> state.reduceWith(event) {
                val newAmount = if (state.amount.isBlank()) "" else state.amount.dropLast(1)
                state {
                    copy(amount = transformation.transform(newAmount))
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
                    val dt = Datestamp.from(instant)
                    copy(
                        datestamp = dt,
                        datestampReadable = dt.toReadable(),
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
                    listOf(Submit(state = state.copy()))
                }
            }

            FormEvent.External.Delete -> state.reduceWith(event) {
                state {
                    copy(isLoading = true)
                }
                command {
                    listOf(Delete(state = state.copy()))
                }
            }

            FormEvent.Internal.DeleteTransactionSuccess -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                command {
                    listOf(Exit)
                }
            }

            FormEvent.Internal.DeleteTransactionFailure -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager.getString(Resources.string.form_failed_delete)
                    listOf(ShowErrorMessage(message = msg))
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
                    listOf(ShowErrorMessage(message = msg))
                }
            }

            FormEvent.Internal.EmptyCategory -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager.getString(Resources.string.form_empty_category)
                    listOf(ShowErrorMessage(message = msg))
                }
            }

            FormEvent.Internal.SubmitTransactionFailure -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                effects {
                    val msg = resourceManager.getString(Resources.string.form_failed_submit)
                    listOf(ShowErrorMessage(message = msg))
                }
            }

            FormEvent.Internal.SubmitTransactionSuccess -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                command {
                    listOf(SetLastUsedCurrency(state.currency), Exit)
                }
            }

            is FormEvent.Internal.SuccessLoadTransaction -> state.reduceWith(event) {
                state {
                    copy(
                        isLoading = false,
                        transactionType = event.entity.type,
                        amount = transformation.transform(event.entity.amount.amountToString()),
                        datestamp = event.entity.datestamp,
                        datestampReadable = event.entity.datestamp.toReadable(),
                        category = event.entity.category,
                        tags = event.entity.tags,
                        currency = event.entity.currency,
                        comment = event.entity.comment,
                    )
                }
            }

            FormEvent.Internal.FailedLoadTransaction -> state.reduceWith(event) {
                state {
                    copy(isLoading = false)
                }
                command {
                    listOf(Exit)
                }
            }

            is FormEvent.External.SetComment -> state.reduceWith(event) {
                state {
                    copy(comment = event.comment)
                }
            }
        }
    }

    private fun Datestamp.toReadable(): String {
        val today = Datestamp.now()
        return when {
            today == this ->
                resourceManager.getString(Resources.string.today)

            this.date == today.date.minus(1, DateTimeUnit.DAY) ->
                resourceManager.getString(Resources.string.yesterday)

            else ->
                this.readableName()
        }
    }
}
