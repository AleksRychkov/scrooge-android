package dev.aleksrychkov.scrooge.core.designsystem.composables

import android.annotation.SuppressLint
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ComposableNaming")
@Composable
fun DsInputTextFieldsColors() = TextFieldDefaults.colors().copy(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    unfocusedTextColor = Color.Unspecified,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
)
