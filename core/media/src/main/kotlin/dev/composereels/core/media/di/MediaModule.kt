package dev.composereels.core.media.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

/**
 * Provides the app's playback dependencies.
 *
 * Per the project's hard requirement, exactly one [ExoPlayer] instance exists for the whole
 * process. Reels are loaded as a single playlist on that one player so ExoPlayer can pre-buffer the
 * next item; a media cache makes already-watched reels start instantly on re-watch.
 */
@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    private const val MAX_CACHE_BYTES = 256L * 1024 * 1024 // 256 MB LRU media cache.

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun providesPlayerCache(@ApplicationContext context: Context): SimpleCache = SimpleCache(
        File(context.cacheDir, "reel-media"),
        LeastRecentlyUsedCacheEvictor(MAX_CACHE_BYTES),
        StandaloneDatabaseProvider(context),
    )

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun providesMediaSourceFactory(
        @ApplicationContext context: Context,
        cache: SimpleCache,
    ): MediaSource.Factory {
        // Read through the cache, falling back to the network; ignore cache on read errors.
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        return DefaultMediaSourceFactory(cacheDataSourceFactory)
    }

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    fun providesExoPlayer(
        @ApplicationContext context: Context,
        mediaSourceFactory: MediaSource.Factory,
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(mediaSourceFactory)
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
            // Loop the current reel; the user swipes to advance.
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = false
        }
}
