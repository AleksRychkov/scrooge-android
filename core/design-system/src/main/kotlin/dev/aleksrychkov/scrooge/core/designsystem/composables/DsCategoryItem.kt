package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny
import dev.aleksrychkov.scrooge.core.designsystem.theme.categoryItemSize
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity

@Composable
fun DsCategoryItem(
    modifier: Modifier = Modifier,
    category: CategoryEntity? = null,
    color: Color,
    tint: Color,
    imageVector: ImageVector,
    borderEnabled: Boolean = false,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderWidth: Dp = Tinny,
    onItemClicked: (CategoryEntity) -> Unit = {},
) {
    Box(
        modifier = modifier
            .categoryItemSize()
            .clip(RoundedCornerShape(Normal))
            .borderModifier(
                borderEnabled = borderEnabled,
                borderColor = borderColor,
                borderWidth = borderWidth,
            )
            .clickModifier(
                entity = category,
                onItemClicked = onItemClicked,
            ),
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .background(color.copy(alpha = 0.15f))
                .padding(Normal),
            tint = tint,
            imageVector = imageVector,
            contentDescription = null,
        )
    }
}

@Composable
private fun Modifier.clickModifier(
    entity: CategoryEntity?,
    onItemClicked: (CategoryEntity) -> Unit,
): Modifier = if (entity != null) {
    this then Modifier.debounceClickable(
        onClick =
        { onItemClicked(entity) }
    )
} else {
    this
}

@Composable
private fun Modifier.borderModifier(
    borderEnabled: Boolean,
    borderColor: Color,
    borderWidth: Dp,
): Modifier = if (borderEnabled) {
    this then Modifier.border(
        width = borderWidth,
        color = borderColor,
        shape = RoundedCornerShape(Normal)
    )
} else {
    this
}
