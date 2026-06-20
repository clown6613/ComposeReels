package dev.composereels.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Example Compose UI test. Copy this pattern to test feature screens with a fake ViewModel.
 */
class StateViewsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun errorState_showsMessage_and_retryInvokesCallback() {
        var retried = false
        composeRule.setContent {
            ErrorState(message = "Something went wrong", onRetry = { retried = true })
        }

        composeRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeRule.onNodeWithText("Retry").performClick()

        assertTrue(retried)
    }
}
