package dev.aleksrychkov.scrooge.core.designsystem.theme

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val CategoryIconSize = 48.dp
val ListItemHeight = 60.dp

@Composable
fun Modifier.categoryItemSize(): Modifier = this then Modifier.size(CategoryIconSize)
