package dev.composereels.core.network.model

import dev.composereels.core.model.ReelSource
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkReelMapperTest {

    private fun networkReel(source: String) = NetworkReel(
        id = "id",
        title = "title",
        author = "author",
        description = "description",
        videoUrl = "https://example.com/v.mp4",
        thumbnailUrl = "https://example.com/t.jpg",
        likeCount = 7,
        source = source,
    )

    @Test
    fun `maps all fields to the domain model`() {
        val reel = networkReel("MP4").asExternalModel()

        assertEquals("id", reel.id)
        assertEquals("title", reel.title)
        assertEquals("author", reel.author)
        assertEquals("description", reel.description)
        assertEquals("https://example.com/v.mp4", reel.videoUrl)
        assertEquals(7, reel.likeCount)
        assertEquals(ReelSource.MP4, reel.source)
    }

    @Test
    fun `parses HLS source case-insensitively`() {
        assertEquals(ReelSource.HLS, networkReel("hls").asExternalModel().source)
    }

    @Test
    fun `falls back to MP4 for unknown source values`() {
        assertEquals(ReelSource.MP4, networkReel("webm").asExternalModel().source)
    }
}
