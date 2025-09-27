package dev.aleksrychkov.scrooge.core.router.context

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ComponentContextFactory
import dev.aleksrychkov.scrooge.core.router.Router

interface RouterComponentContext : ComponentContext {
    companion object {
        operator fun invoke(
            componentContext: ComponentContext,
            router: Router
        ): RouterComponentContext =
            DefaultRouterComponentContext(componentContext = componentContext, router = router)
    }

    val router: Router
}

private class DefaultRouterComponentContext(
    componentContext: ComponentContext,
    override val router: Router,
) : RouterComponentContext, ComponentContext by componentContext {

    override val componentContextFactory: ComponentContextFactory<RouterComponentContext> =
        ComponentContextFactory { lifecycle, stateKeeper, instanceKeeper, backHandler ->
            val ctx = componentContext.componentContextFactory(
                lifecycle = lifecycle,
                stateKeeper = stateKeeper,
                instanceKeeper = instanceKeeper,
                backHandler = backHandler,
            )
            DefaultRouterComponentContext(ctx, router)
        }
}
