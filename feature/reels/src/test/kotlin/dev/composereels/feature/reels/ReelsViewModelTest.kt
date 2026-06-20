package dev.composereels.feature.reels

import app.cash.turbine.test
import dev.composereels.core.media.ReelPlayerController
import dev.composereels.core.testing.MainDispatcherRule
import dev.composereels.core.testing.data.FakeReelsRepository
import dev.composereels.core.testing.data.testReels
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ReelsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // The controller drives the singleton player; a relaxed mock lets us verify interactions.
    private val playerController = mockk<ReelPlayerController>(relaxed = true)

    @Test
    fun `emits Success with the loaded reels`() = runTest {
        val viewModel = ReelsViewModel(FakeReelsRepository(testReels), playerController)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ReelsUiState.Success)
            assertEquals(testReels, (state as ReelsUiState.Success).reels)
        }
    }

    @Test
    fun `loads the feed as a playlist and plays the first reel`() = runTest {
        ReelsViewModel(FakeReelsRepository(testReels), playerController)

        verify { playerController.setReels(testReels) }
        verify { playerController.playAt(0) }
    }

    @Test
    fun `PageSettled plays the selected reel and updates currentPage`() = runTest {
        val viewModel = ReelsViewModel(FakeReelsRepository(testReels), playerController)

        viewModel.onEvent(ReelEvent.PageSettled(1))

        viewModel.uiState.test {
            val state = awaitItem() as ReelsUiState.Success
            assertEquals(1, state.currentPage)
        }
        verify { playerController.playAt(1) }
    }

    @Test
    fun `emits Error when the repository fails`() = runTest {
        val viewModel = ReelsViewModel(
            FakeReelsRepository(error = IllegalStateException("boom")),
            playerController,
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is ReelsUiState.Error)
            assertEquals("boom", (state as ReelsUiState.Error).message)
        }
    }
}
