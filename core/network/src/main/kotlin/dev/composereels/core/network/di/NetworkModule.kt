package dev.composereels.core.network.di

import dev.composereels.core.network.ReelsNetworkDataSource
import dev.composereels.core.network.asset.AssetReelsNetworkDataSource
import dev.composereels.core.network.retrofit.ReelsApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Wires the networking stack.
 *
 * The active [ReelsNetworkDataSource] binding is [AssetReelsNetworkDataSource] so the app
 * works offline out of the box. To fetch the feed remotely, change the `@Binds` below to
 * `RetrofitReelsNetwork` and point [BASE_URL] at your backend.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    internal abstract fun bindsReelsNetworkDataSource(
        impl: AssetReelsNetworkDataSource,
    ): ReelsNetworkDataSource

    companion object {
        /** Replace with your backend host when switching to the Retrofit data source. */
        private const val BASE_URL = "https://example.com/"

        @Provides
        @Singleton
        fun providesJson(): Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        @Provides
        @Singleton
        fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC },
            )
            .build()

        @Provides
        @Singleton
        fun providesRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        @Provides
        @Singleton
        fun providesReelsApiService(retrofit: Retrofit): ReelsApiService =
            retrofit.create(ReelsApiService::class.java)
    }
}
