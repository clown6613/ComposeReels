# ComposeReels 🎬

[English] | [日本語](README.ja.md)

A modern, open-source **vertical reels (short-video) app** for Android, built entirely with the
tech stack and architecture Google recommends. It's a clean, readable reference implementation,
made **AI-friendly** by a [`CLAUDE.md`](CLAUDE.md) that spells out the architecture and conventions
so coding agents (and people) can extend it confidently.

> Swipe through a TikTok-style feed of full-screen videos, all driven by a **single, shared
> ExoPlayer instance**.

## ✨ Highlights

- 💯 **100% Kotlin + Jetpack Compose**, Material 3, edge-to-edge.
- 🧱 **Multi-module architecture** in the style of [Now in Android][nia] — `:app`, `:core:*`,
  `:feature:*`.
- 🔁 **Unidirectional Data Flow (UDF)** — ViewModels expose a single immutable `StateFlow` state.
- ▶️ **Singleton media player** — exactly one `ExoPlayer` for the whole app, provided by Hilt and
  reused across pages (see [The singleton player](#-the-singleton-player)).
- 💉 **Hilt** for dependency injection, **Coroutines + Flow** for async.
- 🌐 **Swappable data layer** — ships with a bundled sample feed; switch one Hilt binding to fetch
  from a real backend with Retrofit.
- 🧪 **Tested** — unit tests for the ViewModel, repository and mappers, plus a Compose UI test.
- 🤖 **AI-ready** — a [`CLAUDE.md`](CLAUDE.md) documents the conventions so AI agents (and humans)
  can extend the project confidently.

## 📸 Screenshots

<!-- TODO: add a screen recording / GIF of the reels feed here. -->
_Add a demo GIF here._

## 🏛️ Architecture

The project follows Google's recommended layered, modular architecture. Dependencies always point
inward toward `:core:model`; feature modules never depend on each other.

```
            ┌─────────────┐
            │     app     │  Application, MainActivity, NavHost, process-level player release
            └──────┬──────┘
                   │
          ┌────────▼────────┐
          │  feature:reels  │  UiState · ViewModel · Screen (VerticalPager) · ReelPage
          └───┬────┬────┬───┘
              │    │    │
   ┌──────────▼─┐ ┌▼────▼──────┐ ┌──────────────┐
   │ core:data  │ │ core:media │ │   core:ui    │
   │ repository │ │ SINGLETON  │ │  composables │
   └─────┬──────┘ │  player    │ └──────┬───────┘
         │        └─────┬──────┘        │
   ┌─────▼──────┐       │        ┌──────▼─────────┐
   │core:network│       │        │core:designsystem│
   └─────┬──────┘       │        └────────────────┘
         │              │
   ┌─────▼──────────────▼───┐
   │   core:model · common  │
   └────────────────────────┘
```

### Modules

| Module | Responsibility |
| --- | --- |
| `:app` | Single-activity host, navigation graph, releases the shared player at process shutdown. |
| `:feature:reels` | The reels feed: `ReelsUiState`, `ReelsViewModel`, `ReelsScreen`, `ReelPage`. |
| `:core:model` | Pure-Kotlin domain models (`Reel`, `ReelSource`). |
| `:core:common` | Coroutine dispatchers, `Result` wrapper and DI qualifiers. |
| `:core:network` | Retrofit service, DTOs/mappers and the bundled `reels.json` asset source. |
| `:core:data` | `ReelsRepository` — the seam for swapping data sources. |
| `:core:media` | **The singleton `ExoPlayer`** and `ReelPlayerController`. |
| `:core:designsystem` | Material 3 theme, colors, type and shared components. |
| `:core:ui` | Model-aware composables (thumbnail, overlay, loading/error states). |
| `:core:testing` | Fakes, fixtures and the `MainDispatcherRule`. |
| `build-logic` | Gradle convention plugins that keep module build files tiny. |

## ▶️ The singleton player

Reels play one video at a time, so the app uses **one** `ExoPlayer` for the entire process —
provided by Hilt as a `@Singleton`:

```kotlin
@Provides
@Singleton
fun providesExoPlayer(@ApplicationContext context: Context): ExoPlayer =
    ExoPlayer.Builder(context).build().apply { repeatMode = Player.REPEAT_MODE_ONE }
```

`ReelPlayerController` is the single window onto that instance. As the user swipes, the pager's
`settledPage` is the source of truth: only the active page hosts a `PlayerView`, and the controller
swaps the media item on the shared player instead of creating a new one.

**Lifecycle rule:** the player is paused/resumed with the screen lifecycle, but it is **released
only once**, from `ProcessLifecycleOwner` in the `Application` — never from a `ViewModel`, since the
singleton outlives any screen.

> **Why one player?** Lowest memory and decoder pressure, and the simplest lifecycle. The trade-off
> is a brief buffer on swipe (mitigated by the thumbnail behind the video). A small player *pool*
> for pre-buffering is a natural future enhancement — the `ReelPlayerController` seam makes it easy.

## 🚀 Getting started

**Requirements:** JDK 17 and a recent Android Studio. Min SDK 26.

```bash
git clone https://github.com/your-org/ComposeReels.git
cd ComposeReels
./gradlew installDebug   # build & install on a running emulator/device
```

Or open the project in Android Studio and hit ▶︎.

The app works out of the box: the feed is bundled in
`core/network/src/main/assets/reels.json`, and the videos stream from public sample / test-stream
URLs (Blender open movies, plus Apple and Mux HLS test streams).

### Useful commands

```bash
./gradlew lintDebug            # static analysis
./gradlew testDebugUnitTest    # JVM unit tests
./gradlew assembleDebug        # build the debug APK
./gradlew connectedDebugAndroidTest  # instrumented/Compose UI tests (needs a device)
```

## 🛠️ Tech stack

Kotlin · Jetpack Compose · Material 3 · Hilt · Coroutines & Flow · AndroidX Media3 (ExoPlayer) ·
Navigation Compose · Coil · Retrofit + OkHttp + kotlinx.serialization · Gradle version catalog +
convention plugins.

## 🤝 Contributing

Contributions are welcome — see [CONTRIBUTING.md](CONTRIBUTING.md) and our
[Code of Conduct](CODE_OF_CONDUCT.md).

## 📄 License

Licensed under the [Apache License 2.0](LICENSE).

Sample videos © their respective owners (Blender Foundation open movies; Apple and Mux HLS test
streams), used here for demonstration purposes only.

[nia]: https://github.com/android/nowinandroid
