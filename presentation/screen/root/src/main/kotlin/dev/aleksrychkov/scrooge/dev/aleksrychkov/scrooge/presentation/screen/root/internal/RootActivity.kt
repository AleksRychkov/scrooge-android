package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.defaultComponentContext
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.feature.theme.ObserveThemeUseCase

internal class RootActivity : ComponentActivity() {
    private val theme: ObserveThemeUseCase = get()

    val controller by lazy(mode = LazyThreadSafetyMode.NONE) {
        WindowInsetsControllerCompat(
            window,
            window.decorView
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val componentContext = remember { defaultComponentContext() }
            val theme by theme().collectAsStateWithLifecycle(null)
            val useDarkTheme = when (theme?.type) {
                ThemeEntity.Type.Light -> false
                ThemeEntity.Type.Dark -> true
                ThemeEntity.Type.System -> isSystemInDarkTheme()
                ThemeEntity.Type.Undefined -> isSystemInDarkTheme()
                else -> null
            }
            if (useDarkTheme != null) {
                controller.isAppearanceLightStatusBars = !useDarkTheme
            }
            AnimatedVisibility(
                visible = useDarkTheme != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AppTheme(useDarkTheme = useDarkTheme ?: isSystemInDarkTheme()) {
                    RootContent(
                        componentContext = componentContext
                    )
                }
            }
        }
    }
}
