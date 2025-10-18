package dev.aleksrychkov.scrooge.core.designsystem.composables

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

// copied from https://stackoverflow.com/a/79611497
@Composable
fun DialogSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value != SwipeToDismissBoxValue.Settled) {
                snackbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(snackbarHostState.currentSnackbarData) {
        snackbarHostState.currentSnackbarData?.let {
            dismissState.reset()
        }
    }

    snackbarHostState.currentSnackbarData
        ?.takeIf { it.visuals.message.isNotEmpty() }
        ?.let { data ->
            Dialog(
                onDismissRequest = {
                    data.dismiss()
                },
                properties = DialogProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                    usePlatformDefaultWidth = false
                )
            ) {
                (LocalView.current.parent as DialogWindowProvider).window.apply {
                    setGravity(Gravity.BOTTOM)
                    clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    attributes = attributes.apply {
                        width = WindowManager.LayoutParams.MATCH_PARENT
                    }
                }

                val density = LocalDensity.current
                val navigationBarPadding = with(density) {
                    WindowInsets.navigationBars.getBottom(this).toDp()
                }

                SwipeToDismissBox(
                    modifier = modifier.padding(bottom = navigationBarPadding),
                    state = dismissState,
                    backgroundContent = {}
                ) {
                    SnackbarHost(hostState = snackbarHostState) {
                        Snackbar(snackbarData = it)
                    }
                }
            }
        }
}
