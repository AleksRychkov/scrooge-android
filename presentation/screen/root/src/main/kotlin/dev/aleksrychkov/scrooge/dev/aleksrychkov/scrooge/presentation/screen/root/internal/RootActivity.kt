package dev.aleksrychkov.scrooge.dev.aleksrychkov.scrooge.presentation.screen.root.internal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import dev.aleksrychkov.scrooge.core.designsystem.theme.AppTheme

internal class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                RootContent(
                    componentContext = defaultComponentContext()
                )
            }
        }
    }
}
