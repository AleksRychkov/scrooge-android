package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf.actors

import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryCommand
import dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.bycategory.udf.ByCategoryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

internal class LoadDelegate {
    operator fun invoke(cmd: ByCategoryCommand.Load): Flow<ByCategoryEvent> {
        return emptyFlow()
    }
}
