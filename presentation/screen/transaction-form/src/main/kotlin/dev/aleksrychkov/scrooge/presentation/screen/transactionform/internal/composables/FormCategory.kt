package dev.aleksrychkov.scrooge.presentation.screen.transactionform.internal.composables

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsInputTextFieldsColors
import dev.aleksrychkov.scrooge.core.designsystem.composables.DsSecondaryCard
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.Small
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.entity.TransactionType
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormCategory(
    modifier: Modifier,
    category: CategoryEntity?,
    openCategoryModal: () -> Unit,
) {
    DsSecondaryCard(modifier = modifier.height(intrinsicSize = IntrinsicSize.Max)) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Small)
                .pointerInput(category) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            openCategoryModal()
                        }
                    }
                },
            value = category?.name.orEmpty(),
            singleLine = true,
            label = {
                Text(stringResource(Resources.string.category))
            },
            colors = DsInputTextFieldsColors(),
            readOnly = true,
            onValueChange = { },
        )
    }
}

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun FormContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            FormCategory(
                modifier = Modifier.fillMaxWidth(),
                category = CategoryEntity.from("Transport", TransactionType.Income, "Savings"),
                openCategoryModal = {},
            )
        }
    }
}
