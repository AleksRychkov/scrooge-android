package dev.aleksrychkov.scrooge.core.udfextensions

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import com.arkivanov.essenty.instancekeeper.retainedInstance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class RetainedCoroutineScope(
    context: CoroutineContext
) : CoroutineScope, InstanceKeeper.Instance {
    override val coroutineContext: CoroutineContext = context

    override fun onDestroy() {
        this.cancel()
    }
}

fun InstanceKeeperOwner.retainedCoroutineScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    job: Job = SupervisorJob()
): CoroutineScope {
    return retainedInstance(key = this::class.java.name) {
        RetainedCoroutineScope(
            context = dispatcher + job
        )
    }
}
