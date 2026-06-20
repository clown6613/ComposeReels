package dev.composereels.core.network.retrofit

import dev.composereels.core.network.model.NetworkReel
import retrofit2.http.GET

/**
 * Retrofit endpoint for the reels feed. Used by [RetrofitReelsNetwork].
 *
 * Point the Retrofit base URL (see `NetworkModule`) at any host that serves a
 * `reels.json` array to fetch the feed remotely.
 */
interface ReelsApiService {
    @GET("reels.json")
    suspend fun getReels(): List<NetworkReel>
}
