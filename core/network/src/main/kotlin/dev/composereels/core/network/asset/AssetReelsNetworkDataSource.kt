package dev.composereels.core.network.asset

import android.content.Context
import dev.composereels.core.common.di.Dispatcher
import dev.composereels.core.common.di.ReelsDispatcher
import dev.composereels.core.network.ReelsNetworkDataSource
import dev.composereels.core.network.model.NetworkReel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Default [ReelsNetworkDataSource] that loads the feed from a bundled `reels.json` asset.
 * This keeps the sample app fully functional without a server; the reels themselves point
 * at Google's public sample video URLs.
 */
internal class AssetReelsNetworkDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    @Dispatcher(ReelsDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ReelsNetworkDataSource {

    override suspend fun getReels(): List<NetworkReel> = withContext(ioDispatcher) {
        val raw = context.assets.open(ASSET_FILE_NAME).bufferedReader().use { it.readText() }
        json.decodeFromString<List<NetworkReel>>(raw)
    }

    private companion object {
        const val ASSET_FILE_NAME = "reels.json"
    }
}
