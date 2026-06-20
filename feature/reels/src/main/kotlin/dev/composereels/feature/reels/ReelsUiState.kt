package dev.composereels.feature.reels

import dev.composereels.core.model.Reel

/**
 * The complete, immutable state the reels screen renders from. Following UDF, the screen is a
 * pure function of this value; the ViewModel is the only thing that produces new instances.
 */
sealed interface ReelsUiState {
    /** The first page of reels is still loading. */
    data object Loading : ReelsUiState

    /** Loading failed; [message] explains why and the user can retry. */
    data class Error(val message: String) : ReelsUiState

    /**
     * Reels are ready to watch.
     *
     * @property reels The ordered feed.
     * @property currentPage Index of the reel currently bound to the shared player.
     */
    data class Success(
        val reels: List<Reel>,
        val currentPage: Int = 0,
    ) : ReelsUiState
}
