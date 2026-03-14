package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Tinny

@Composable
internal fun FormClose(
    modifier: Modifier = Modifier,
    onCloseClicked: () -> Unit,
) {
    Box(
        modifier = modifier
            .dropShadow(
                shape = CircleShape,
                shadow = Shadow(
                    radius = Normal,
                    spread = Tinny,
                    color = Color.Black.copy(alpha = 0.25f),
                )
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .debounceClickable(onClick = onCloseClicked),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.padding(Normal),
            imageVector = Icons.Default.Close,
            contentDescription = null,
        )
    }
}
