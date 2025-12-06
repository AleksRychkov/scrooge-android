package dev.aleksrychkov.scrooge.component.report.annualtotal.internal.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large2X
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal

@Composable
internal fun SelectYear(
    modifier: Modifier,
    year: Int,
    onIncrementClicked: () -> Unit,
    onDecrementClicked: () -> Unit,
    onCurrentYearClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = Normal, vertical = Normal),
            onClick = onDecrementClicked,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier
                .clip(CircleShape)
                .clickable {
                    onCurrentYearClicked()
                }
                .padding(vertical = Normal, horizontal = Large2X),
            text = year.toString(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .padding(horizontal = Normal, vertical = Normal),
            onClick = onIncrementClicked,
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = null,
            )
        }
    }
}

@Suppress("MagicNumber", "UnusedPrivateMember")
@Preview
@Composable
private fun SelectYearPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            var selectedYear by remember { mutableIntStateOf(2025) }
            SelectYear(
                modifier = Modifier.fillMaxWidth(),
                year = selectedYear,
                onIncrementClicked = { selectedYear += 1 },
                onDecrementClicked = { selectedYear -= 1 },
                onCurrentYearClicked = { selectedYear = 2025 },
            )
        }
    }
}
