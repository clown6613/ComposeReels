package dev.composereels.feature.reels

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.composereels.core.ui.ErrorState
import dev.composereels.core.ui.LoadingState
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Reels feed route. Collects [ReelsUiState] and renders the swipeable pager, forwarding all
 * user/lifecycle intents back to [ReelsViewModel] as [ReelEvent]s.
 */
@Composable
fun ReelsScreen(
    modifier: Modifier = Modifier,
    viewModel: ReelsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Pause/resume playback with the screen lifecycle so audio never plays in the background.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffectLifecycle(
        onResume = { viewModel.onEvent(ReelEvent.ScreenResumed) },
        onPause = { viewModel.onEvent(ReelEvent.ScreenPaused) },
        lifecycleOwner = lifecycleOwner,
    )

    when (val state = uiState) {
        ReelsUiState.Loading -> LoadingState(modifier)
        is ReelsUiState.Error -> ErrorState(
            message = state.message,
            onRetry = { viewModel.onEvent(ReelEvent.Retry) },
            modifier = modifier,
        )

        is ReelsUiState.Success -> ReelsPager(
            state = state,
            player = viewModel.player(),
            onEvent = viewModel::onEvent,
            modifier = modifier,
        )
    }
}

@Composable
private fun ReelsPager(
    state: ReelsUiState.Success,
    player: androidx.media3.exoplayer.ExoPlayer,
    onEvent: (ReelEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { state.reels.size },
    )

    // The settled page is the single source of truth for which reel owns the player.
    LaunchedEffect(pagerState) {
        snapshotFlowSettledPage(pagerState)
            .distinctUntilChanged()
            .collect { page -> onEvent(ReelEvent.PageSettled(page)) }
    }

    VerticalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        key = { index -> state.reels[index].id },
    ) { page ->
        ReelPage(
            reel = state.reels[page],
            isActive = page == pagerState.settledPage,
            player = player,
            onTogglePlayPause = { onEvent(ReelEvent.TogglePlayPause) },
            onLike = { /* Wire to a real like action in your backend. */ },
            onShare = { /* Wire to a share sheet. */ },
        )
    }
}

/** Emits the pager's settled page whenever it changes. */
private fun snapshotFlowSettledPage(
    pagerState: androidx.compose.foundation.pager.PagerState,
) = androidx.compose.runtime.snapshotFlow { pagerState.settledPage }

/**
 * Registers a lifecycle observer that pauses/resumes playback and cleans itself up on dispose.
 */
@Composable
private fun DisposableEffectLifecycle(
    onResume: () -> Unit,
    onPause: () -> Unit,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
) {
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> onResume()
                Lifecycle.Event.ON_PAUSE -> onPause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
