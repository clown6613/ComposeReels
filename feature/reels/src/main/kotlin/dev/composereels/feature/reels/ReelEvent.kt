package dev.composereels.feature.reels

/** User/lifecycle intents the reels screen sends to the ViewModel (the single input channel). */
sealed interface ReelEvent {
    /** The pager settled on [page]; the shared player should bind to that reel. */
    data class PageSettled(val page: Int) : ReelEvent

    /** The user tapped the video to toggle play/pause. */
    data object TogglePlayPause : ReelEvent

    /** The screen became visible again; resume the active reel. */
    data object ScreenResumed : ReelEvent

    /** The screen is no longer visible; pause playback. */
    data object ScreenPaused : ReelEvent

    /** Retry loading after an error. */
    data object Retry : ReelEvent
}
