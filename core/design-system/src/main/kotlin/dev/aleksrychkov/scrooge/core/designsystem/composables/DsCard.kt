package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.designsystem.theme.Medium

@Suppress("MagicNumber")
@Composable
fun DsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = Medium),
        shape = ShapeDefaults.Large,
        content = content,
    )
}
