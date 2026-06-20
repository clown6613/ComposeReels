package dev.composereels.core.common.di

import javax.inject.Qualifier

/** Qualifies an injected [kotlinx.coroutines.CoroutineDispatcher]. */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: ReelsDispatcher)

/** The set of dispatchers the app injects, so call sites never hardcode `Dispatchers.IO`. */
enum class ReelsDispatcher {
    Default,
    IO,
}
