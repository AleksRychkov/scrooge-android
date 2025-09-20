@file:Suppress("Filename", "TooGenericExceptionCaught")

package dev.aleksrychkov.scrooge.core.utils

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}

inline fun <T, R> T.runSuspendCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
