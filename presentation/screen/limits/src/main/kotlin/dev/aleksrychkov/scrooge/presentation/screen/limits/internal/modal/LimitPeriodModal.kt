package dev.aleksrychkov.scrooge.presentation.screen.limits.internal.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsWheel
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LimitPeriodModal(
    current: String,
    onPeriodSelected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topEnd = Normal, topStart = Normal),
        ) {
            val periodDaily = stringResource(Resources.string.limits_daily)
            val periodWeekly = stringResource(Resources.string.limits_weekly)
            val periodMonthly = stringResource(Resources.string.limits_monthly)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Indicator()
                DsWheel(
                    modifier = Modifier.fillMaxWidth(),
                    data = listOf(periodDaily, periodWeekly, periodMonthly),
                    selectedItem = current,
                    onItemSelected = onPeriodSelected,
                )
            }
        }
    }
}

@Suppress("MagicNumber")
@Composable
private fun BoxScope.Indicator() {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val wheelLabelFontSize = MaterialTheme.typography.titleLarge.fontSize
    val itemHeight = remember {
        val result = textMeasurer.measure(
            text = AnnotatedString("Sample"),
            style = TextStyle(fontSize = wheelLabelFontSize)
        )
        with(density) {
            result.size.height.toDp() + Normal2X
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Large)
            .height(itemHeight)
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50),
            )
            .align(Alignment.Center),
    )
}
