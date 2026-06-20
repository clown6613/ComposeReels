# CLAUDE.md

Guidance for AI coding agents (Claude Code and similar) working in this repository. Humans will
find it useful too. Keep this file up to date when conventions change.

## What this project is

ComposeReels is a vertical short-video ("reels") Android app built as a modern, Google-recommended
reference and an AI-driven-development template. Optimize changes for **readability, correctness and
architectural consistency** — this code is meant to be read and copied.

## Build & test commands

```bash
./gradlew lintDebug            # static analysis
./gradlew testDebugUnitTest    # JVM unit tests (ViewModel, repository, mappers)
./gradlew assembleDebug        # build the debug APK
./gradlew connectedDebugAndroidTest  # Compose UI / instrumented tests (needs a device)
./gradlew build                # everything: compile + unit tests + assemble
```

Requirements: **JDK 17**, min SDK 26. The Gradle wrapper pins the Gradle version.

## Module map

```
app                → Application, MainActivity, NavHost, process-level player release
feature:reels      → reels feed: UiState, ViewModel, Screen (VerticalPager), ReelPage, navigation
core:model         → pure-Kotlin domain models (Reel, ReelSource)
core:common        → coroutine dispatchers, Result wrapper, DI qualifiers
core:network       → Retrofit service, DTOs/mappers, bundled reels.json asset source
core:data          → ReelsRepository (the data-source seam)
core:media         → THE singleton ExoPlayer + ReelPlayerController
core:designsystem  → Material 3 theme + shared components
core:ui            → model-aware composables (thumbnail, overlay, states)
core:testing       → fakes, fixtures, MainDispatcherRule
build-logic        → Gradle convention plugins
```

## Architecture rules (do not violate)

1. **Dependency direction**: everything points inward toward `:core:model`. Feature modules depend
   only on `:core:*` modules — **never on another feature**.
2. **UDF**: a ViewModel exposes exactly one immutable state object via `StateFlow`. UI is a pure
   function of that state; user/lifecycle intents flow back as a single sealed `*Event` type. Do not
   expose mutable state, multiple unrelated flows, or `LiveData`.
3. **The player is a singleton**. There is one `ExoPlayer` in the whole app.
   - Get it only through `ReelPlayerController` (in `:core:media`). Never call `ExoPlayer.Builder`
     anywhere else.
   - **Never release the player from a `ViewModel`.** It outlives screens. Release happens once, in
     `ComposeReelsApplication` via `ProcessLifecycleOwner`. Pausing/resuming per screen is fine.
   - Exactly one `PlayerView` is attached at a time — driven by the pager's `settledPage`.
4. **Swap data sources at the binding, not the call site**. The default `ReelsNetworkDataSource`
   reads a bundled asset; to go remote, change the `@Binds` in `NetworkModule` to
   `RetrofitReelsNetwork` and set the base URL. Don't add networking logic into the repository.
5. **Dispatchers are injected** via `@Dispatcher(...)`. Never hardcode `Dispatchers.IO` in app code.

## Conventions

- **Language**: all code, comments, KDoc and string resources are in **English**. (User-facing docs
  are bilingual: `README.md` + `README.ja.md`.)
- **Gradle**: dependencies live in `gradle/libs.versions.toml`. Module build files apply a
  `composereels.*` convention plugin and stay minimal. Add new shared config to `build-logic`.
- **DI**: Hilt. New repositories/data sources get an `@Binds`/`@Provides` in the owning module's
  `di/` package and `@InstallIn(SingletonComponent::class)`.
- **Tests**: prefer fast JVM unit tests with fakes from `:core:testing` (`FakeReelsRepository`,
  `testReels`, `MainDispatcherRule`). Use Turbine for `StateFlow` assertions and MockK for the
  player controller.

## How to add a new feature module (recipe)

1. Create `feature/<name>/` with a `build.gradle.kts` applying
   `alias(libs.plugins.composereels.android.feature)`.
2. Add `include(":feature:<name>")` to `settings.gradle.kts`.
3. Set `namespace = "dev.composereels.feature.<name>"`.
4. Add `<Name>UiState`, `<Name>Event`, `<Name>ViewModel` (`@HiltViewModel`), `<Name>Screen`, and a
   `navigation/<Name>Navigation.kt` exposing a `@Serializable` route + `NavGraphBuilder` extension.
5. Register the route in `app`'s `ComposeReelsNavHost`.
6. Add a `<Name>ViewModelTest` using `:core:testing`.

## Common pitfalls

- Releasing the singleton player in `ViewModel.onCleared()` → crashes other screens. Don't.
- Declaring a repository in a module without adding it to the Hilt graph → `MissingBinding`.
- Adding repositories with `mavenCentral()` inside a module → fails because of
  `FAIL_ON_PROJECT_REPOS`. Declare repos only in `settings.gradle.kts`.
- Attaching more than one `PlayerView` to the shared player → use the `isActive`/`settledPage` gate.
