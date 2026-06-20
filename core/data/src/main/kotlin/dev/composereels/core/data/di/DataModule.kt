package dev.composereels.core.data.di

import dev.composereels.core.data.repository.DefaultReelsRepository
import dev.composereels.core.data.repository.ReelsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Binds repository interfaces to their default implementations. */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsReelsRepository(impl: DefaultReelsRepository): ReelsRepository
}
