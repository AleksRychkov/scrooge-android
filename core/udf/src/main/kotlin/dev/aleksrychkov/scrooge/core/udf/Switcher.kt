package dev.aleksrychkov.scrooge.core.udf

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// copied from:
// https://github.com/vivid-money/elmslie/blob/publish-elmslie-3.0/elmslie-core/src/commonMain/kotlin/money/vivid/elmslie/core/switcher/Switcher.kt

/**
 * Allows to execute requests for [Actor] implementations in a switching manner. Each request
 * will cancel the previous one.
 *
 * Example:
 * ```
 * private val switcher = Switcher()
 *
 * override fun execute(command: Command): Flow<*> = when (command) {
 *    is MyCommand -> switcher.switch {
 *        flowOf(123)
 *    }
 * }
 * ```
 */
class Switcher {
    private var currentChannel: SendChannel<*>? = null
    private val lock = Mutex()

    /**
     * Collect given flow as a job and cancels all previous ones.
     *
     * @param delayMillis operation delay measured with milliseconds. Can be specified to debounce
     * existing requests.
     * @param action actual event source
     */
    fun <Event : Any> switch(
        delayMillis: Long = 0,
        action: suspend () -> Flow<Event>,
    ): Flow<Event> {
        return callbackFlow {
            lock.withLock {
                currentChannel?.close()
                currentChannel = channel
            }

            delay(delayMillis)

            action.invoke()
                .onEach { send(it) }
                .catch { close(it) }
                .collect { }

            channel.close()
        }
    }

    fun <Event : Any> cancel(
        delayMillis: Long = 0,
    ): Flow<Event> = flow {
        delay(delayMillis)
        lock.withLock {
            currentChannel?.close()
            currentChannel = null
        }
    }
}
