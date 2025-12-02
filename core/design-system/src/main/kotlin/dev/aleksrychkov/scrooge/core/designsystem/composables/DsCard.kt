package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large

@Suppress("MagicNumber")
@Composable
fun DsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier
            .dropShadow(
                shape = ShapeDefaults.Large,
                shadow = Shadow(
                    radius = Large,
                    spread = 3.dp,
                    color = Color(0x12000000),
                    offset = DpOffset(x = 4.dp, 4.dp)
                )
            ),
        shape = ShapeDefaults.Large,
        content = content,
    )
}
