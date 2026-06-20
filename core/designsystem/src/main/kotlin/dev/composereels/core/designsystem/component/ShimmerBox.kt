package dev.composereels.core.designsystem.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** A simple animated shimmer placeholder used while media loads. */
@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmer-translate",
    )

    val shimmerColors = listOf(
        Color.DarkGray.copy(alpha = 0.4f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.DarkGray.copy(alpha = 0.4f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translate - 300f, translate - 300f),
        end = Offset(translate, translate),
    )

    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush),
    )
}
