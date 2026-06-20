package dev.composereels

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dev.composereels.core.media.ReelPlayerController
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application entry point.
 *
 * Owns the lifecycle of the singleton player: it observes the *process* lifecycle and releases
 * the player exactly once, when the whole app is destroyed. Releasing it anywhere shorter-lived
 * (such as a ViewModel) would tear down a player that other screens may still need.
 */
@HiltAndroidApp
class ComposeReelsApplication : Application() {

    @Inject
    lateinit var playerController: ReelPlayerController

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    playerController.release()
                }
            },
        )
    }
}
