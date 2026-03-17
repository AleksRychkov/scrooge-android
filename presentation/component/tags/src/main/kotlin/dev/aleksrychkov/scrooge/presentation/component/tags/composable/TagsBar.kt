package dev.aleksrychkov.scrooge.presentation.component.tags.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsButton
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSearchTextField
import dev.aleksrychkov.scrooge.core.designsystem.composables.animateElevation
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppBarShadow
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.presentation.component.tags.internal.TagComponentInternal
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun TagsBar(
    modifier: Modifier,
    component: TagComponentInternal,
    contentListState: LazyListState,
) {
    val state by component.state.collectAsStateWithLifecycle()

    val headerElevation by remember {
        derivedStateOf {
            if (contentListState.firstVisibleItemScrollOffset > 0 ||
                contentListState.firstVisibleItemIndex != 0
            ) {
                AppBarShadow
            } else {
                0.dp
            }
        }
    }
    val animatedElevation by headerElevation.animateElevation()
    Surface(
        Modifier.fillMaxWidth(),
        shadowElevation = animatedElevation,
    ) {
        Row(
            modifier = modifier
                .height(IntrinsicSize.Max)
                .padding(Large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(weight = 1f, fill = true),
            ) {
                DsSearchTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.searchQuery,
                    onValueChanged = component::setSearchQuery,
                )
            }

            if (state.isEditable) {
                DsButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = Normal),
                    onClick = component::addNewTag,
                ) {
                    Text(text = stringResource(Resources.string.add))
                }
            }
        }
    }
}
