# Contributing to ComposeReels

Thanks for your interest in improving ComposeReels! This project doubles as a reference
template, so contributions that keep the architecture clean and well-documented are especially
welcome.

## Getting started

1. **Prerequisites**: JDK 17 and the latest stable Android Studio. The Gradle wrapper pins the
   Gradle version, so no separate install is needed.
2. Clone the repo and open it in Android Studio, or build from the command line.
3. Run the app on an emulator or device (API 26+):

   ```bash
   ./gradlew installDebug
   ```

## Before opening a pull request

Run the same checks CI runs:

```bash
./gradlew lintDebug testDebugUnitTest assembleDebug
```

## Code style & conventions

- **Language**: all code, comments, KDoc and string resources are written in **English**.
- **Architecture**: follow the existing multi-module layout (see `README.md`). Feature modules
  depend only on `:core:*` modules, never on each other.
- **UDF**: ViewModels expose a single immutable UI state via `StateFlow`; UI sends intents back
  as events. Don't expose mutable state or `LiveData`.
- **The player is a singleton**: never construct an `ExoPlayer` yourself and never release the
  shared one from a `ViewModel`. Go through `ReelPlayerController`.
- Keep formatting consistent with `.editorconfig` (120-column lines, 4-space indent).

## Commit & PR guidelines

- Write focused commits with clear messages.
- Describe the motivation and the change in the PR template.
- Add or update tests for behavior changes.

## Reporting bugs & requesting features

Use the issue templates under **New Issue**. Please include reproduction steps for bugs and a
clear use case for feature requests.
