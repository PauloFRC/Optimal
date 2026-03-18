package dev.optimal.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.ui.OptimalApp

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
//    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OptimalTheme {
                OptimalApp()
            }
        }
    }
}
