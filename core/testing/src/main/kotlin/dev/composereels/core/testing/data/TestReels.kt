package dev.composereels.core.testing.data

import dev.composereels.core.model.Reel
import dev.composereels.core.model.ReelSource

/** Deterministic sample reels for use across unit and UI tests. */
val testReels: List<Reel> = listOf(
    Reel(
        id = "reel-1",
        title = "First Reel",
        author = "tester",
        description = "The first sample reel.",
        videoUrl = "https://example.com/1.mp4",
        thumbnailUrl = "https://example.com/1.jpg",
        likeCount = 1_200,
        source = ReelSource.MP4,
    ),
    Reel(
        id = "reel-2",
        title = "Second Reel",
        author = "tester",
        description = "The second sample reel.",
        videoUrl = "https://example.com/2.m3u8",
        thumbnailUrl = "https://example.com/2.jpg",
        likeCount = 3_400,
        source = ReelSource.HLS,
    ),
)
