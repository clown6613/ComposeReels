package dev.composereels.core.model

/**
 * Streaming format of a [Reel].
 *
 * The media layer maps this to a MIME type so ExoPlayer can pick the right source
 * factory (progressive MP4 vs adaptive HLS/DASH) without inspecting the URL.
 */
enum class ReelSource {
    /** A progressive MP4 file served over HTTP. */
    MP4,

    /** An adaptive HLS playlist (`.m3u8`). */
    HLS,

    /** An adaptive DASH manifest (`.mpd`). Non-DRM only — DRM streams can't show a frame poster. */
    DASH,
}
