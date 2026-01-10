package dev.aleksrychkov.scrooge.core.designsystem.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small

@Composable
fun DsFilterAction(
    name: String,
    showFilterIcon: Boolean,
    openFiltersModal: () -> Unit,
) {
    TextButton(
        onClick = openFiltersModal,
    ) {
        Text(text = name)

        if (showFilterIcon) {
            Text(
                modifier = Modifier.padding(horizontal = Small),
                text = "+"
            )
            Icon(
                modifier = Modifier.size(Large),
                imageVector = Icons.Rounded.Category,
                contentDescription = null,
            )
        }
    }
}
