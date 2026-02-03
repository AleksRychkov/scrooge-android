package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors.CreateDelegate
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors.DeleteDelegate
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors.LoadDelegate
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors.LoadLastUsedCurrencyDelegate
import dev.aleksrychkov.scrooge.presentation.screen.limits.internal.udf.actors.UpdateDelegate
import kotlinx.coroutines.flow.Flow

internal class LimitsActor(
    val loadDelegate: Lazy<LoadDelegate> = lazy { LoadDelegate() },
    val loadLastUsedCurrency: Lazy<LoadLastUsedCurrencyDelegate> = lazy { LoadLastUsedCurrencyDelegate() },
    val updateDelegate: Lazy<UpdateDelegate> = lazy { UpdateDelegate() },
    val createDelegate: Lazy<CreateDelegate> = lazy { CreateDelegate() },
    val deleteDelegate: Lazy<DeleteDelegate> = lazy { DeleteDelegate() },
) : Actor<LimitsCommand, LimitsEvent> {

    override suspend fun process(command: LimitsCommand): Flow<LimitsEvent> {
        return when (command) {
            LimitsCommand.LoadLimits -> loadDelegate.value.invoke()
            LimitsCommand.LoadLastUsedCurrency -> loadLastUsedCurrency.value.invoke()
            is LimitsCommand.Create -> createDelegate.value.invoke(command)
            is LimitsCommand.Delete -> deleteDelegate.value.invoke(command)
            is LimitsCommand.Update -> updateDelegate.value.invoke(command)
        }
    }
}
