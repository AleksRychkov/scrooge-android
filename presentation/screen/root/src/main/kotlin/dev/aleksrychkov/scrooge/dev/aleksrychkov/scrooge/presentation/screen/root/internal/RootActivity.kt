package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import android.animation.Animator
import android.os.Bundle
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCaller
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.defaultComponentContext
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme
import dev.aleksrychkov.scrooge.core.di.Naive
import dev.aleksrychkov.scrooge.core.di.NaiveModule
import dev.aleksrychkov.scrooge.core.di.factory
import dev.aleksrychkov.scrooge.core.di.get
import dev.aleksrychkov.scrooge.core.di.module
import dev.aleksrychkov.scrooge.core.entity.ThemeEntity
import dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal.transfer.DefaultGetExportUriUseCase
import dev.aleksrychkov.scrooge.feature.theme.ObserveThemeUseCase
import dev.aleksrychkov.scrooge.feature.transfer.GetExportUriUseCase

private const val SPLASH_SCREEN_EXIT_ANIM_DURATION = 500L

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
        setSplashScreenAnimation(splashScreen)

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
            AnimatedVisibility(
                visible = useDarkTheme != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AppTheme(useDarkTheme = useDarkTheme ?: isSystemInDarkTheme()) {
                    RootContent(
                        componentContext = componentContext,
                        readyCallback = hideSplashScreen,
                    )
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
        module = module { factory<GetExportUriUseCase> { exportUri } }.also(Naive::add)
    }

    private fun setSplashScreenAnimation(splashScreen: SplashScreen) {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view
                .animate()
                .alpha(0f)
                .setDuration(SPLASH_SCREEN_EXIT_ANIM_DURATION)
                .setInterpolator(AnticipateInterpolator())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationCancel(animation: Animator) {
                        splashScreenView.remove()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        splashScreenView.remove()
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        // no-op
                    }

                    override fun onAnimationStart(animation: Animator) {
                        // no-op
                    }
                })
                .start()
        }
    }
}
