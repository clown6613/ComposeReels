// The generated @Serializable code references kotlinx.serialization's internal GeneratedSerializer.
// The build handles this opt-in automatically, but some IDE analyzers flag it; opt in at file level
// to keep the editor clean. Harmless to the build.
@file:OptIn(InternalSerializationApi::class)

package dev.composereels.core.network.model

import dev.composereels.core.model.Reel
import dev.composereels.core.model.ReelSource
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Wire format for the reels feed. Decoupled from the domain [Reel] model. */
@Serializable
data class NetworkReel(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    @SerialName("video_url") val videoUrl: String,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("like_count") val likeCount: Long = 0,
    val source: String = "MP4",
)

/** Maps a [NetworkReel] DTO to the domain [Reel], defaulting unknown formats to MP4. */
fun NetworkReel.asExternalModel(): Reel = Reel(
    id = id,
    title = title,
    author = author,
    description = description,
    videoUrl = videoUrl,
    thumbnailUrl = thumbnailUrl,
    likeCount = likeCount,
    source = runCatching { ReelSource.valueOf(source.uppercase()) }.getOrDefault(ReelSource.MP4),
)
