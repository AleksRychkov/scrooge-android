@file:Suppress("EmptyFunctionBlock", "UnusedParameter")

package dev.aleksrychkov.scrooge.component.report.categorytotal.internal.component.period

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.entity.PeriodTimestampEntity

@Composable
internal fun PeriodContent(
    modifier: Modifier,
    component: PeriodComponent,
    callback: (PeriodTimestampEntity) -> Unit,
) {
    val state = remember { component.state }

    PeriodContent(
        modifier = modifier,
        state = state,
    )
}

@Composable
private fun PeriodContent(
    modifier: Modifier,
    state: PeriodState,
) {
}
