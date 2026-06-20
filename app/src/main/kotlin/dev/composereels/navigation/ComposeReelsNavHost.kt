package dev.composereels.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.composereels.feature.reels.navigation.ReelsRoute
import dev.composereels.feature.reels.navigation.reelsScreen

/**
 * Root navigation graph. The reels feed is the start destination; add more
 * `featureScreen()` entries here as the app grows.
 */
@Composable
fun ComposeReelsNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ReelsRoute,
    ) {
        reelsScreen()
    }
}
