package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.di.getLazy
import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.actors.LoadDelegate
import kotlinx.coroutines.flow.Flow

internal class ByCategoryActor(
    private val loadDelegate: LoadDelegate,
) : Actor<ByCategoryCommand, ByCategoryEvent> {
    companion object {
        operator fun invoke(): ByCategoryActor {
            return ByCategoryActor(
                loadDelegate = LoadDelegate(useCase = getLazy()),
            )
        }
    }

    override suspend fun process(command: ByCategoryCommand): Flow<ByCategoryEvent> {
        return when (command) {
            is ByCategoryCommand.Load -> loadDelegate(command)
        }
    }
}
