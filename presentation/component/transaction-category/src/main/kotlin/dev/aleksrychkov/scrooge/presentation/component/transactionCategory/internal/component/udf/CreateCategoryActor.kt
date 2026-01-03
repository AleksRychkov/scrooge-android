package dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf

import dev.aleksrychkov.scrooge.core.udf.Actor
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.actors.LoadDelegate
import dev.aleksrychkov.scrooge.presentation.component.transactionCategory.internal.component.udf.actors.SubmitDelegate
import kotlinx.coroutines.flow.Flow

internal class CreateCategoryActor(
    private val submitDelegate: SubmitDelegate = SubmitDelegate(),
    private val loadDelegate: LoadDelegate = LoadDelegate(),
) : Actor<CreateCategoryCommand, CreateCategoryEvent> {

    override suspend fun process(command: CreateCategoryCommand): Flow<CreateCategoryEvent> {
        return when (command) {
            is CreateCategoryCommand.Submit -> submitDelegate(command)
            is CreateCategoryCommand.Load -> loadDelegate(command)
        }
    }
}
