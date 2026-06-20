package dev.composereels.core.media.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides the app's playback dependencies.
 *
 * Per the project's hard requirement, exactly one [ExoPlayer] instance exists for the whole
 * process. Reels play one video at a time, so reusing a single decoder pipeline keeps memory
 * and battery low; pages swap the media item on the shared player instead of creating new ones.
 */
@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    @Provides
    @Singleton
    fun providesExoPlayer(
        @ApplicationContext context: Context,
    ): ExoPlayer = ExoPlayer.Builder(context)
        // Pause automatically when headphones are unplugged.
        .setHandleAudioBecomingNoisy(true)
        // Request audio focus and duck/pause around other apps.
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build(),
            /* handleAudioFocus = */ true,
        )
        .build()
        .apply {
            // Reels loop until the user swipes away.
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = false
        }
}
