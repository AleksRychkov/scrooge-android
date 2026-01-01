package dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal

import com.arkivanov.decompose.ComponentContext
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import dev.aleksrychkov.scrooge.core.udf.Store
import dev.aleksrychkov.scrooge.core.udfextensions.createStore
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagActor
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEffect
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagEvent
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagReducer
import dev.aleksrychkov.scrooge.presentation.component.transactiontag.internal.udf.TagState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal class DefaultTagComponent(
    private val componentContext: ComponentContext,
) : TagComponentInternal, ComponentContext by componentContext {

    private val store: Store<TagState, TagEvent, TagEffect> by lazy {
        instanceKeeper.createStore(
            initialState = TagState(),
            actor = TagActor(),
            reducer = TagReducer(),
            startEvent = TagEvent.External.Init,
        )
    }

    override val state: StateFlow<TagState>
        get() = store.state

    override val effects: Flow<TagEffect>
        get() = store.effects

    override fun deleteTag(tag: TagEntity) {
        store.handle(TagEvent.External.Delete(tag = tag))
    }

    override fun setSearchQuery(query: String) {
        store.handle(TagEvent.External.Search(query = query))
    }

    override fun addNewTag() {
        store.handle(TagEvent.External.AddNewTag)
    }
}
