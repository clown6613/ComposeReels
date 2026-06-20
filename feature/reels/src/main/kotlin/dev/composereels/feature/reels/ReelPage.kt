package dev.composereels.feature.reels

import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import dev.composereels.core.model.Reel
import dev.composereels.core.ui.ReelOverlay
import dev.composereels.core.ui.ReelThumbnail

/**
 * One full-screen page in the reels feed.
 *
 * Only the active page hosts a [PlayerView]; every other page shows its thumbnail. This is what
 * guarantees the single shared player is attached to exactly one surface at any time. When a page
 * leaves the active slot, `onReset` detaches the surface.
 */
// Media3's PlayerView and resize modes are still annotated @UnstableApi; opt in deliberately.
// Uses androidx.annotation.OptIn (not kotlin.OptIn) so Android lint recognizes the opt-in.
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ReelPage(
    reel: Reel,
    isActive: Boolean,
    player: ExoPlayer,
    onTogglePlayPause: () -> Unit,
    onLike: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        // Poster sits behind the video so the frame is never blank while buffering.
        ReelThumbnail(
            thumbnailUrl = reel.thumbnailUrl,
            contentDescription = reel.title,
            modifier = Modifier.fillMaxSize(),
        )

        if (isActive) {
            AndroidView(
                // Inflated from XML to obtain a TextureView-backed surface (see the layout file).
                factory = { context ->
                    LayoutInflater.from(context)
                        .inflate(R.layout.view_reel_player, null) as PlayerView
                },
                update = { view -> view.player = player },
                onReset = { view -> view.player = null },
                onRelease = { view -> view.player = null },
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Tap anywhere to toggle playback. No ripple — this is a full-screen gesture.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onTogglePlayPause,
                ),
        )

        ReelOverlay(
            reel = reel,
            onLike = onLike,
            onShare = onShare,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
