package dev.composereels.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.composereels.core.designsystem.component.CRIconButton
import dev.composereels.core.model.Reel

/**
 * Author, caption and engagement controls drawn on top of a playing reel. A bottom scrim
 * keeps the text legible regardless of the underlying video.
 */
@Composable
fun ReelOverlay(
    reel: Reel,
    onLike: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0.6f to Color.Transparent,
                    1f to Color.Black.copy(alpha = 0.6f),
                ),
            ),
    ) {
        // Caption block, bottom-start. systemBarsPadding keeps it clear of the gesture/nav bar
        // and status bar now that the video draws edge-to-edge.
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .systemBarsPadding()
                .padding(start = 16.dp, end = 96.dp, bottom = 24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "@${reel.author}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = reel.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
            )
            Text(
                text = reel.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        // Action rail, bottom-end.
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .systemBarsPadding()
                .padding(end = 8.dp, bottom = 24.dp)
                .width(72.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CRIconButton(
                icon = Icons.Filled.Favorite,
                contentDescription = "Like",
                label = reel.likeCount.toCompactString(),
                onClick = onLike,
            )
            CRIconButton(
                icon = Icons.Filled.Share,
                contentDescription = "Share",
                label = "Share",
                onClick = onShare,
            )
        }
    }
}

/** Formats large counts compactly, e.g. 128400 -> "128.4K". */
private fun Long.toCompactString(): String = when {
    this >= 1_000_000 -> "%.1fM".format(this / 1_000_000.0)
    this >= 1_000 -> "%.1fK".format(this / 1_000.0)
    else -> toString()
}
