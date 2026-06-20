package dev.composereels.core.testing.data

import dev.composereels.core.data.repository.ReelsRepository
import dev.composereels.core.model.Reel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * In-memory [ReelsRepository] for tests. Returns [reels], or throws [error] if it is set, so
 * tests can drive both the success and failure paths.
 */
class FakeReelsRepository(
    private val reels: List<Reel> = testReels,
    private val error: Throwable? = null,
) : ReelsRepository {
    override fun getReels(): Flow<List<Reel>> = flow {
        error?.let { throw it }
        emit(reels)
    }
}
