package dev.composereels.core.model

/**
 * A single short-form vertical video shown in the reels feed.
 *
 * This is the domain model the UI and player consume. It is intentionally free of any
 * networking or framework types so it can be reused across layers and unit-tested easily.
 *
 * @property id Stable unique identifier, also used as the player's media id.
 * @property title Short headline shown in the overlay.
 * @property author Display name of the creator.
 * @property description Caption shown under the title.
 * @property videoUrl Direct URL to the playable video stream.
 * @property thumbnailUrl Poster image shown before/behind the video.
 * @property likeCount Number of likes, for the engagement overlay.
 * @property source Streaming format, which drives the player's MIME-type hint.
 */
data class Reel(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val likeCount: Long,
    val source: ReelSource,
)
