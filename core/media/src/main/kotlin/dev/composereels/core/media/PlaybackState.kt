package dev.composereels.core.media

/**
 * Immutable snapshot of the shared player's state, exposed to the UI so it can render
 * play/pause and buffering affordances without touching ExoPlayer directly.
 *
 * @property currentMediaId Media id of the reel the player is bound to, or null if idle.
 * @property isPlaying Whether playback is currently progressing.
 * @property isBuffering Whether the player is buffering the current item.
 */
data class PlaybackState(
    val currentMediaId: String? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
)
