package dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TransferStateEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferActor
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferEvent
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferReducer
import dev.aleksrychkov.scrooge.presentation.component.roottransfer.internal.udf.RootTransferState
import kotlinx.coroutines.flow.StateFlow

internal class DefaultRootTransferComponent(
    componentContext: ComponentContext,
    state: TransferStateEntity
) : RootTransferComponentInternal, ComponentContext by componentContext {

    private val store: Store<RootTransferState, RootTransferEvent, Unit> by lazy {
        instanceKeeper.createStore(
            initialState = RootTransferState(state = state),
            actor = RootTransferActor(),
            reducer = RootTransferReducer(),
            startEvent = RootTransferEvent.External.ObserveState,
        )
    }

    override val state: StateFlow<RootTransferState>
        get() = store.state

    override fun setState(state: TransferStateEntity.State) {
        store.handle(RootTransferEvent.External.SetState(state = state))
    }
}
