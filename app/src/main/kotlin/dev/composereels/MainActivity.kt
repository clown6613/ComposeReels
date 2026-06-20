package dev.composereels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dev.composereels.core.designsystem.theme.ComposeReelsTheme
import dev.composereels.navigation.ComposeReelsNavHost
import dagger.hilt.android.AndroidEntryPoint

/** Single activity that hosts the Compose navigation graph. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ComposeReelsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ComposeReelsNavHost()
                }
            }
        }
    }
}
