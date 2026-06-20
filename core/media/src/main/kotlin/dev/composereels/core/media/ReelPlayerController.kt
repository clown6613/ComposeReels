package dev.composereels.core.media

import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dev.composereels.core.model.Reel
import dev.composereels.core.model.ReelSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The single window onto the shared [ExoPlayer]. Features depend on this controller rather
 * than on ExoPlayer directly, which keeps the "exactly one player" rule enforceable in one place.
 *
 * Threading: all methods must be called from the main thread (ExoPlayer's requirement). The
 * controller is created lazily by Hilt and lives as long as the process.
 */
@Singleton
class ReelPlayerController @Inject constructor(
    /** Exposed so a single `PlayerView` can attach to the shared instance. */
    val player: ExoPlayer,
) {

    private val _playbackState = MutableStateFlow(PlaybackState())

    /** Observable playback state for the UI. */
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private var loadedReelIds: List<String> = emptyList()

    init {
        player.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    _playbackState.value = _playbackState.value.copy(
                        isBuffering = playbackState == Player.STATE_BUFFERING,
                    )
                }
            },
        )
    }

    /**
     * Load the whole feed as a single playlist on the shared player. ExoPlayer then pre-buffers the
     * neighbouring items, so swiping to the next reel starts much faster. No-op if the same feed is
     * already loaded, which makes repeated calls on recomposition/config-change cheap.
     */
    fun setReels(reels: List<Reel>) {
        val ids = reels.map { it.id }
        if (ids == loadedReelIds) return
        loadedReelIds = ids
        player.setMediaItems(reels.map { it.toMediaItem() })
        player.prepare()
    }

    /** Make [index] the current reel and start playback. */
    fun playAt(index: Int) {
        if (index !in loadedReelIds.indices) return
        if (player.currentMediaItemIndex != index) {
            player.seekToDefaultPosition(index)
        }
        player.playWhenReady = true
        _playbackState.value = _playbackState.value.copy(currentMediaId = loadedReelIds[index])
    }

    /** Resume playback of the currently bound reel. */
    fun play() {
        player.playWhenReady = true
    }

    /** Pause without releasing, e.g. when the app moves to the background. */
    fun pause() {
        player.playWhenReady = false
    }

    /** Toggle between playing and paused for the current reel. */
    fun togglePlayPause() {
        player.playWhenReady = !player.playWhenReady
    }

    /**
     * Release the shared player. Call this once, when the process is shutting down
     * (see the app's `ProcessLifecycleOwner` observer) — never from a ViewModel, since the
     * singleton outlives any screen.
     */
    fun release() {
        loadedReelIds = emptyList()
        player.release()
    }
}

/** Builds a [MediaItem], tagging HLS streams so ExoPlayer selects the HLS source. */
private fun Reel.toMediaItem(): MediaItem = MediaItem.Builder()
    .setMediaId(id)
    .setUri(videoUrl)
    .apply {
        when (source) {
            ReelSource.HLS -> setMimeType(MimeTypes.APPLICATION_M3U8)
            ReelSource.DASH -> setMimeType(MimeTypes.APPLICATION_MPD)
            ReelSource.MP4 -> Unit // Inferred from the URI.
        }
    }
    .build()
