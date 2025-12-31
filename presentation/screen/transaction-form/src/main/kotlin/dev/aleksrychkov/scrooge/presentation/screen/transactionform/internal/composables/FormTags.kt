package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal2X
import dev.aleksrychkov.scrooge.core.entity.TagEntity
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormTags(
    modifier: Modifier,
    tags: ImmutableSet<TagEntity>,
    openTagModal: () -> Unit,
    removeTag: (TagEntity) -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = Normal),
    ) {
        InputChip(
            selected = false,
            label = {
                Text(
                    modifier = Modifier.padding(start = Normal),
                    text = stringResource(Resources.string.tag_add),
                )
            },
            shape = RoundedCornerShape(Normal2X),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = stringResource(Resources.string.tag_add),
                )
            },
            onClick = openTagModal,
        )

        tags.forEach { tag ->
            InputChip(
                modifier = Modifier,
                selected = false,
                label = {
                    Text(
                        modifier = Modifier.padding(end = Normal),
                        text = tag.name,
                    )
                },
                shape = RoundedCornerShape(Normal2X),
                onClick = {},
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(Resources.string.tag_delete),
                        modifier = Modifier.debounceClickable {
                            removeTag(tag)
                        }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormTags(
                modifier = Modifier.fillMaxWidth(),
                tags = persistentSetOf(
                    TagEntity.from("tag 1"),
                    TagEntity.from("tag 2"),
                    TagEntity.from("tag 3"),
                ),
                openTagModal = {},
                removeTag = {},
            )
        }
    }
}
