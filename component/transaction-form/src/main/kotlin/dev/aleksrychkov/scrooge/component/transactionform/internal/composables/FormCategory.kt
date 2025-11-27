package dev.aleksrychkov.scrooge.component.transactionform.internal.composables

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.scrooge.core.designsystem.theme.Normal
import dev.aleksrychkov.scrooge.core.entity.CategoryEntity
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun FormCategory(
    modifier: Modifier,
    category: CategoryEntity?,
    openCategoryModal: () -> Unit,
) {
    TextField(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(Normal)
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
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Unspecified,
        ),
        readOnly = true,
        onValueChange = { },
    )
}
