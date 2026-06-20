package dev.composereels.core.data.repository

import dev.composereels.core.model.ReelSource
import dev.composereels.core.network.ReelsNetworkDataSource
import dev.composereels.core.network.model.NetworkReel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultReelsRepositoryTest {

    private val networkReels = listOf(
        NetworkReel(
            id = "n1",
            title = "Net Reel",
            author = "creator",
            description = "desc",
            videoUrl = "https://example.com/n1.m3u8",
            thumbnailUrl = "https://example.com/n1.jpg",
            likeCount = 42,
            source = "HLS",
        ),
    )

    private val networkDataSource = ReelsNetworkDataSource { networkReels }

    @Test
    fun `maps network reels to domain reels`() = runTest {
        val repository = DefaultReelsRepository(networkDataSource, UnconfinedTestDispatcher())

        val reels = repository.getReels().first()

        assertEquals(1, reels.size)
        val reel = reels.first()
        assertEquals("n1", reel.id)
        assertEquals("Net Reel", reel.title)
        assertEquals(42, reel.likeCount)
        assertEquals(ReelSource.HLS, reel.source)
    }
}

/** SAM-style helper so the test can supply a one-line fake data source. */
private fun ReelsNetworkDataSource(block: suspend () -> List<NetworkReel>) =
    object : ReelsNetworkDataSource {
        override suspend fun getReels(): List<NetworkReel> = block()
    }
