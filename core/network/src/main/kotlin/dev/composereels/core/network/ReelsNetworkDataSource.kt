package dev.composereels.core.network

import dev.composereels.core.network.model.NetworkReel

/**
 * Source of the raw reels feed.
 *
 * The default implementation reads a bundled JSON asset so the app runs offline right
 * after cloning. Swap the Hilt binding to [retrofit.RetrofitReelsNetwork] (or your own
 * implementation) to fetch the feed from a real backend instead.
 */
interface ReelsNetworkDataSource {
    suspend fun getReels(): List<NetworkReel>
}
