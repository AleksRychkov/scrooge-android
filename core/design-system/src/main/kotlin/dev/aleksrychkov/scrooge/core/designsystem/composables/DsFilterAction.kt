package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
fun DsFilterAction(
    name: String,
    showTagIcon: Boolean,
    openFiltersModal: () -> Unit,
) {
    TextButton(
        onClick = openFiltersModal,
    ) {
        Text(text = name)

        if (showTagIcon) {
            Text(
                modifier = Modifier.padding(horizontal = Small),
                text = "+"
            )
            Icon(
                modifier = Modifier.size(Normal),
                imageVector = ImageVector.vectorResource(Resources.drawable.ic_tag_24px),
                contentDescription = null,
            )
        }
    }
}
