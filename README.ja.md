# ComposeReels 🎬

[English](README.md) | [日本語]

[![CI](https://github.com/clown6613/ComposeReels/actions/workflows/ci.yml/badge.svg)](https://github.com/clown6613/ComposeReels/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
![minSdk](https://img.shields.io/badge/minSdk-26-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-2.1-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)
![PRs welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

Google が推奨する技術スタックとアーキテクチャだけで作った、モダンでオープンソースな
**縦スワイプ型リール（短尺動画）アプリ** です。読みやすいリファレンス実装で、
[`CLAUDE.md`](CLAUDE.md) にアーキテクチャと規約を明文化し、**AI（コーディングエージェント）が
安心して拡張できる**ようにしています。

> 縦型・全画面の短尺動画フィードをスワイプして閲覧。再生はすべて
> **単一の共有 ExoPlayer インスタンス** が担います。

## ✨ 特長

- 💯 **100% Kotlin + Jetpack Compose**、Material 3、エッジトゥエッジ。
- 🧱 [Now in Android][nia] スタイルの **マルチモジュール構成** — `:app` / `:core:*` / `:feature:*`。
- 🔁 **単方向データフロー（UDF）** — ViewModel は単一の不変な `StateFlow` を公開。
- ▶️ **シングルトンの動画プレイヤー** — アプリ全体で `ExoPlayer` はちょうど 1 つ。Hilt が提供し、
  ページ間で使い回します（[シングルトンプレイヤー](#-シングルトンプレイヤー)参照）。
- 💉 DI に **Hilt**、非同期に **Coroutines + Flow**。
- 🌐 **差し替え可能なデータ層** — 同梱のサンプルフィードで動作。Hilt のバインドを 1 行変えれば
  Retrofit で実バックエンドから取得できます。
- 🧪 **テスト付き** — ViewModel・Repository・mapper のユニットテストと Compose UI テスト。
- 🤖 **AI フレンドリー** — [`CLAUDE.md`](CLAUDE.md) に規約を明文化し、AI エージェント（と人間）が
  安心して拡張できます。

## 🎬 デモ

<p align="center">
  <img src="docs/demo.gif" alt="ComposeReels デモ — リールフィードのスワイプ" width="280" />
</p>

## 🏛️ アーキテクチャ

Google 推奨のレイヤード／モジュラー構成に従います。依存方向は常に `:core:model` に向かい、
feature モジュール同士は依存しません。

```
            ┌─────────────┐
            │     app     │  Application / MainActivity / NavHost / プロセス終了時の release
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

### モジュール

| モジュール | 責務 |
| --- | --- |
| `:app` | シングルアクティビティのホスト、ナビゲーショングラフ、プロセス終了時に共有プレイヤーを release。 |
| `:feature:reels` | リールフィード本体：`ReelsUiState` / `ReelsViewModel` / `ReelsScreen` / `ReelPage`。 |
| `:core:model` | 純 Kotlin のドメインモデル（`Reel` / `ReelSource`）。 |
| `:core:common` | コルーチンディスパッチャ、`Result` ラッパ、DI 修飾子。 |
| `:core:network` | Retrofit サービス、DTO/mapper、同梱の `reels.json` アセットソース。 |
| `:core:data` | `ReelsRepository` — データソース差し替えのためのシーム。 |
| `:core:media` | **シングルトンの `ExoPlayer`** と `ReelPlayerController`。 |
| `:core:designsystem` | Material 3 テーマ、配色、タイポグラフィ、共通コンポーネント。 |
| `:core:ui` | モデルを知っている共通 Composable（サムネ、オーバーレイ、ローディング/エラー）。 |
| `:core:testing` | Fake、フィクスチャ、`MainDispatcherRule`。 |
| `build-logic` | 各モジュールの build ファイルを薄く保つ Gradle convention plugin。 |

## ▶️ シングルトンプレイヤー

リールは一度に 1 本だけ再生するため、アプリはプロセス全体で **1 つだけ** の `ExoPlayer` を
使います。Hilt が `@Singleton` として提供します。

```kotlin
@Provides
@Singleton
fun providesExoPlayer(@ApplicationContext context: Context): ExoPlayer =
    ExoPlayer.Builder(context).build().apply { repeatMode = Player.REPEAT_MODE_ONE }
```

`ReelPlayerController` がそのインスタンスへの唯一の窓口です。スワイプ時はページャの
`settledPage` を唯一の真実（source of truth）とし、アクティブなページだけが `PlayerView`
を持ち、コントローラは新しいプレイヤーを作らずに共有プレイヤーの media item を差し替えます。

**ライフサイクルの鉄則：** プレイヤーは画面のライフサイクルに合わせて pause/resume しますが、
**release は 1 回だけ**、`Application` の `ProcessLifecycleOwner` から行います。シングルトンは
どの画面よりも長生きするため、**`ViewModel` から release してはいけません**。

> **なぜ 1 つ？** メモリとデコーダの負荷が最小で、ライフサイクルが最もシンプルだからです。
> 代償としてスワイプ時に一瞬バッファリングが入りますが、背面のサムネイルで体感を補います。
> プリバッファ用に複数プレイヤーの *プール* を持つのは自然な発展形で、`ReelPlayerController`
> のシームによって導入は容易です。

## 🚀 セットアップ

**必要環境：** JDK 17 と最近の Android Studio。Min SDK 26。

```bash
git clone https://github.com/your-org/ComposeReels.git
cd ComposeReels
./gradlew installDebug   # 起動中のエミュレータ/実機にビルド & インストール
```

Android Studio でプロジェクトを開いて ▶︎ を押すだけでも OK です。

クローン直後から動作します。フィードは
`core/network/src/main/assets/reels.json` に同梱され、動画は公開サンプル／テストストリーム URL
（Blender のオープンムービー、Apple・Mux の HLS テストストリーム）からストリーミングされます。

### よく使うコマンド

```bash
./gradlew lintDebug            # 静的解析
./gradlew testDebugUnitTest    # JVM ユニットテスト
./gradlew assembleDebug        # デバッグ APK のビルド
./gradlew connectedDebugAndroidTest  # 計測テスト/Compose UI テスト（実機・エミュ必要）
```

## 🛠️ 技術スタック

Kotlin · Jetpack Compose · Material 3 · Hilt · Coroutines & Flow · AndroidX Media3 (ExoPlayer) ·
Navigation Compose · Coil · Retrofit + OkHttp + kotlinx.serialization · Gradle version catalog +
convention plugins。

## 🤝 コントリビュート

歓迎します。[CONTRIBUTING.md](CONTRIBUTING.md) と
[行動規範](CODE_OF_CONDUCT.md) をご覧ください。

## 🎞️ クレジット・サードパーティ素材

サンプルリールは**デモ目的で URL 参照しているだけ**で、リポジトリには再配布しておらず、
**本プロジェクトのライセンス対象外**です:

- **Big Buck Bunny** / **Sintel** — © [Blender Foundation](https://www.blender.org)、
  [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/) で提供。
- **BipBop** HLS テストストリーム — © Apple Inc.（公開テスト素材）。
- [Mux](https://test-streams.mux.dev) の HLS テストストリーム（公開テスト素材）。
- サムネのプレースホルダは [Lorem Picsum](https://picsum.photos)（画像は
  [Unsplash](https://unsplash.com/license) 由来）。

サードパーティのライブラリは各自のライセンス（Apache-2.0 / MIT）に従います。[NOTICE](NOTICE) を参照。

各商標は各社に帰属します。ComposeReels は TikTok / Instagram / Google / Apple / Mux /
Blender Foundation と**提携・承認・出資関係はありません**。

## 📄 ライセンス

[Apache License 2.0](LICENSE) のもとで公開しています（[NOTICE](NOTICE) も参照）。

[nia]: https://github.com/android/nowinandroid
