package dev.composereels.core.data.repository

import dev.composereels.core.model.Reel
import kotlinx.coroutines.flow.Flow

/**
 * Single entry point for reels data. The UI layer depends on this interface only, so the
 * underlying source (bundled asset, REST backend, local cache) can change without touching
 * features.
 */
interface ReelsRepository {
    /** Streams the reels feed. Emits once for the asset-backed default implementation. */
    fun getReels(): Flow<List<Reel>>
}
