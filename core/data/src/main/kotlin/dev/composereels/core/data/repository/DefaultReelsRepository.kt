package dev.composereels.core.data.repository

import dev.composereels.core.common.di.Dispatcher
import dev.composereels.core.common.di.ReelsDispatcher
import dev.composereels.core.model.Reel
import dev.composereels.core.network.ReelsNetworkDataSource
import dev.composereels.core.network.model.asExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Default [ReelsRepository] that maps network DTOs to domain models off the main thread.
 */
internal class DefaultReelsRepository @Inject constructor(
    private val networkDataSource: ReelsNetworkDataSource,
    @Dispatcher(ReelsDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ReelsRepository {

    override fun getReels(): Flow<List<Reel>> = flow {
        emit(networkDataSource.getReels().map { it.asExternalModel() })
    }.flowOn(ioDispatcher)
}
