package dev.composereels.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import dev.composereels.core.designsystem.component.ShimmerBox

/**
 * Poster image for a reel, shown behind the video and on inactive pages. Displays a shimmer
 * placeholder while the image loads so the feed never flashes blank.
 */
@Composable
fun ReelThumbnail(
    thumbnailUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = thumbnailUrl,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        loading = { ShimmerBox(Modifier.fillMaxSize()) },
        modifier = modifier,
    )
}
