package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal

@Composable
fun DsCardV2(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    OutlinedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            draggedElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary,
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 1.dp,
            brush = SolidColor(MaterialTheme.colorScheme.outline),
        ),
        shape = CardDefaults.shape,
        content = content,
    )
}

@Preview
@Composable
@Suppress("UnusedPrivateMember", "MagicNumber")
private fun DsCardV2PreviewDark() {
    AppTheme(useDarkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            DsCardV2(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(Normal),
                ) {
                    Text(LoremIpsum(20).values.toList().joinToString())
                }
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember", "MagicNumber")
private fun DsCardV2PreviewLight() {
    AppTheme(useDarkTheme = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            DsCardV2(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(Normal),
                ) {
                    Text(LoremIpsum(20).values.toList().joinToString())
                }
            }
        }
    }
}
