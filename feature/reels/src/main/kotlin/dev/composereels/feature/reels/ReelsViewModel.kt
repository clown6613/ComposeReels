package dev.composereels.feature.reels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import dev.composereels.core.common.result.Result
import dev.composereels.core.common.result.asResult
import dev.composereels.core.data.repository.ReelsRepository
import dev.composereels.core.media.ReelPlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Holds the reels feed state and drives the shared player.
 *
 * State is exposed as a single immutable [ReelsUiState] (UDF). The imperative side — telling
 * the singleton player which reel to play — is owned here too, but it never *releases* the
 * player: the singleton outlives the screen, so release happens at the process level.
 */
@HiltViewModel
class ReelsViewModel @Inject constructor(
    private val reelsRepository: ReelsRepository,
    private val playerController: ReelPlayerController,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReelsUiState>(ReelsUiState.Loading)
    val uiState: StateFlow<ReelsUiState> = _uiState.asStateFlow()

    init {
        loadReels()
    }

    /** The shared player, handed to the active page so it can attach a `PlayerView`. */
    fun player(): ExoPlayer = playerController.player

    fun onEvent(event: ReelEvent) {
        when (event) {
            is ReelEvent.PageSettled -> bindPage(event.page)
            ReelEvent.TogglePlayPause -> playerController.togglePlayPause()
            ReelEvent.ScreenResumed -> playerController.play()
            ReelEvent.ScreenPaused -> playerController.pause()
            ReelEvent.Retry -> loadReels()
        }
    }

    private fun bindPage(page: Int) {
        val state = _uiState.value
        if (state !is ReelsUiState.Success) return
        val reel = state.reels.getOrNull(page) ?: return
        playerController.bind(reel)
        _uiState.update { (it as? ReelsUiState.Success)?.copy(currentPage = page) ?: it }
    }

    private fun loadReels() {
        viewModelScope.launch {
            reelsRepository.getReels().asResult().collect { result ->
                _uiState.update {
                    when (result) {
                        Result.Loading -> ReelsUiState.Loading
                        is Result.Success -> ReelsUiState.Success(reels = result.data)
                            .also { state -> bindFirstReel(state) }
                        is Result.Error -> ReelsUiState.Error(
                            message = result.throwable.message ?: "Failed to load reels.",
                        )
                    }
                }
            }
        }
    }

    /** Auto-play the top reel as soon as the feed arrives. */
    private fun bindFirstReel(state: ReelsUiState.Success) {
        state.reels.firstOrNull()?.let(playerController::bind)
    }
}
