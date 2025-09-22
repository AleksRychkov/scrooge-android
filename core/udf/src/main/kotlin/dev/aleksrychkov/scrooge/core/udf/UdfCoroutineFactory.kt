package dev.aleksrychkov.scrooge.core.udf

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val UdfDispatcher: CoroutineDispatcher = Dispatchers.Default

@Suppress("FunctionName")
fun UdfScope(): CoroutineScope = CoroutineScope(
    UdfDispatcher +
        CoroutineName("StoreScope") +
        SupervisorJob() +
        CoroutineExceptionHandler { _, exception ->
            // todo logging
        }
)
