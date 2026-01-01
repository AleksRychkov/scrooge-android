package dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.report.categorytotal.internal.component.bycategory.udf.actors.LoadDelegate
import kotlinx.coroutines.flow.Flow

internal class ByCategoryActor(
    private val loadDelegate: LoadDelegate = LoadDelegate(),
) : Actor<ByCategoryCommand, ByCategoryEvent> {
    override suspend fun process(command: ByCategoryCommand): Flow<ByCategoryEvent> {
        return when (command) {
            is ByCategoryCommand.Load -> loadDelegate(command)
        }
    }
}
