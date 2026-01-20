package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCaller
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.defaultComponentContext
import dev.aleksrychkov.scrooge.core.designsystem.locals.AppThemeState
import dev.aleksrychkov.scrooge.core.designsystem.locals.LocalAppTheme
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.transfer.DefaultGetExportUriUseCase
import dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.transfer.DefaultGetImportUriUseCase
import dev.aleksrychkov.scrooge.feature.theme.ObserveThemeUseCase
import dev.aleksrychkov.scrooge.feature.transfer.GetExportUriUseCase
import dev.aleksrychkov.scrooge.feature.transfer.GetImportUriUseCase

internal class RootActivity : ComponentActivity() {
    private val theme: ObserveThemeUseCase = get()

    private val insetController by lazy(mode = LazyThreadSafetyMode.NONE) {
        WindowInsetsControllerCompat(
            window,
            window.decorView
        )
    }

    private var module: NaiveModule? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }
        val hideSplashScreen: () -> Unit = {
            splashScreen.setKeepOnScreenCondition { false }
        }

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
                insetController.isAppearanceLightStatusBars = !useDarkTheme
            }
            val appTheme = AppThemeState(useDarkTheme = useDarkTheme ?: isSystemInDarkTheme())
            AnimatedVisibility(
                visible = useDarkTheme != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AppTheme(useDarkTheme = useDarkTheme ?: isSystemInDarkTheme()) {
                    CompositionLocalProvider(LocalAppTheme provides appTheme) {
                        RootContent(
                            componentContext = componentContext,
                            readyCallback = hideSplashScreen,
                        )
                    }
                }
            }
        }
        setupRootModule()
    }

    override fun onDestroy() {
        super.onDestroy()
        module?.let(Naive::remove)
    }

    private fun setupRootModule() {
        val exportUri = DefaultGetExportUriUseCase(resultCaller = this as ActivityResultCaller)
        val importUri = DefaultGetImportUriUseCase(resultCaller = this as ActivityResultCaller)
        module = module {
            factory<GetExportUriUseCase> { exportUri }
            factory<GetImportUriUseCase> { importUri }
        }.also(Naive::add)
    }
}
