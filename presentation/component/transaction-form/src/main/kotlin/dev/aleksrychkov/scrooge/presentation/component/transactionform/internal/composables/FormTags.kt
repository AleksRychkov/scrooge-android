package dev.aleksrychkov.scrooge.presentation.component.transactionform.internal.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.composables.debounceClickable
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.resources.R as Resources

private const val TAGS_COUNT_OVERFLOW = 100
private const val TAGS_COUNT_FONT_OVERFLOW = 10

@Composable
internal fun FormTags(
    modifier: Modifier,
    openTagModal: () -> Unit,
    tagsCount: Int,
) {
    DsSecondaryCard(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .debounceClickable(onClick = openTagModal),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(Resources.drawable.ic_tag_24px),
                contentDescription = null
            )

            if (tagsCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Small)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                        .size(18.dp),
                    contentAlignment = Alignment.Center // keeps content centered
                ) {
                    val txt = if (tagsCount < TAGS_COUNT_OVERFLOW) tagsCount.toString() else "99+"
                    val fontSize = with(LocalDensity.current) {
                        (if (tagsCount < TAGS_COUNT_FONT_OVERFLOW) 10.dp else 8.dp).toSp()
                    }
                    Text(
                        text = txt,
                        color = Color.White,
                        fontSize = fontSize,
                        textAlign = TextAlign.Center,
                        lineHeight = fontSize,
                        style = LocalTextStyle.current.copy(
                            platformStyle = PlatformTextStyle(includeFontPadding = false)
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormFormTags() {
    AppTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            FormTags(
                modifier = Modifier,
                tagsCount = 101,
                openTagModal = {}
            )
        }
    }
}
