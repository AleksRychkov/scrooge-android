package dev.aleksrychkov.scrooge.component.transactionform.internal.udf

import dev.aleksrychkov.scrooge.component.transactionform.internal.toDateString
import dev.aleksrychkov.scrooge.component.transactionform.internal.udf.FormCommand.LoadTransaction
import dev.aleksrychkov.scrooge.core.udf.Reducer
import dev.aleksrychkov.scrooge.core.udf.ReducerResult
import dev.aleksrychkov.scrooge.core.udf.reduceWith
import kotlin.time.Instant

internal class FormReducer : Reducer<FormState, FormEvent, FormCommand, FormEffect> {
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
                        listOf(FormCommand.GetLastUsedCurrency)
                    }
                }
            }

            is FormEvent.External.AddTag -> TODO()
            is FormEvent.External.RemoveTag -> TODO()
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
        }
    }
}
