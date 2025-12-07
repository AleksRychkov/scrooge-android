package dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf

import dev.aleksrychkov.scrooge.component.transaction.root.internal.component.balance.udf.actors.UpdateBalanceDelegate
import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.core.udf.Switcher
import kotlinx.coroutines.flow.Flow

internal class BalanceActor(
    private val updateDelegate: UpdateBalanceDelegate,
) : Actor<BalanceCommand, BalanceEvent> {

    companion object {
        operator fun invoke(): BalanceActor {
            return BalanceActor(
                updateDelegate = UpdateBalanceDelegate(
                    useCase = getLazy(),
                ),
            )
        }
    }

    private val updateBalanceSwitcher: Switcher by lazy {
        Switcher()
    }

    override suspend fun process(command: BalanceCommand): Flow<BalanceEvent> {
        return when (command) {
            is BalanceCommand.UpdateBalance -> updateBalanceSwitcher.switch {
                updateDelegate(command)
            }
        }
    }
}
