package dev.aleksrychkov.scrooge.component.transactions.internal.composables

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import dev.aleksrychkov.scrooge.core.designsystem.theme.ExpenseColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.HalfNormal
import dev.aleksrychkov.scrooge.core.designsystem.theme.IncomeColor
import dev.aleksrychkov.scrooge.core.designsystem.theme.Large
import dev.aleksrychkov.scrooge.core.resources.R as Resources

@Composable
internal fun AddTransactionFab(
    onIncomeClicked: () -> Unit,
    onExpenseClicked: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    BackHandler(enabled = expanded) {
        expanded = false
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .let { baseModifier ->
                if (expanded) {
                    baseModifier.pointerInput(Unit) {
                        detectTapGestures {
                            expanded = false
                        }
                    }
                } else {
                    baseModifier
                }
            }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Large),
            horizontalAlignment = Alignment.End,
        ) {
            FabItems(
                expanded = expanded,
                closeItems = { expanded = false },
                onIncomeClicked = onIncomeClicked,
                onExpenseClicked = onExpenseClicked,
            )

            val rotation by animateFloatAsState(targetValue = if (expanded) 45f else 0f)
            FloatingActionButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = if (expanded) {
                        stringResource(Resources.string.close)
                    } else {
                        stringResource(Resources.string.form_add_transaction)
                    },
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
    }
}

@Composable
private fun FabItems(
    expanded: Boolean,
    closeItems: () -> Unit,
    onIncomeClicked: () -> Unit,
    onExpenseClicked: () -> Unit,
) {
    AnimatedVisibility(
        expanded,
        enter = fadeIn() + slideInHorizontally { fullWidth -> fullWidth },
        exit = fadeOut() + slideOutHorizontally { fullWidth -> fullWidth },
    ) {
        Column(
            modifier = Modifier.padding(bottom = HalfNormal),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = {
                    onIncomeClicked()
                    closeItems()
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(Resources.drawable.ic_trending_up_24px),
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = IncomeColor,
                )
                Spacer(Modifier.size(Large))
                Text(stringResource(Resources.string.income))
            }

            Button(
                onClick = {
                    onExpenseClicked()
                    closeItems()
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(Resources.drawable.ic_trending_down_24px),
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = ExpenseColor,
                )
                Spacer(Modifier.size(Large))
                Text(stringResource(Resources.string.expense))
            }
        }
    }
}
