package dev.composereels.feature.reels.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.composereels.feature.reels.ReelsScreen
import kotlinx.serialization.Serializable

/** Type-safe route for the reels feed. */
@Serializable
data object ReelsRoute

/** Registers the reels screen in a navigation graph. */
fun NavGraphBuilder.reelsScreen() {
    composable<ReelsRoute> {
        ReelsScreen()
    }
}
