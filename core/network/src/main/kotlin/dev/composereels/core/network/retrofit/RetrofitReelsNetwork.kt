package dev.composereels.core.network.retrofit

import dev.composereels.core.network.ReelsNetworkDataSource
import dev.composereels.core.network.model.NetworkReel
import javax.inject.Inject

/**
 * Remote [ReelsNetworkDataSource] backed by Retrofit.
 *
 * Not bound by default — see `NetworkModule`. Switch the `@Binds` there to this class to
 * fetch the feed over HTTP instead of from the bundled asset.
 */
internal class RetrofitReelsNetwork @Inject constructor(
    private val apiService: ReelsApiService,
) : ReelsNetworkDataSource {

    override suspend fun getReels(): List<NetworkReel> = apiService.getReels()
}
