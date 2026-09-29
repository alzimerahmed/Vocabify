# Vocabify — offline-friendly dictionary for Android

<div align="center">

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=android&logoColor=white)](https://developer.android.com/compose)
[![Release](https://img.shields.io/github/v/release/alzimerahmed/Vocabify?include_prereleases)](../../releases)
[![CI](https://img.shields.io/github/actions/workflow/status/alzimerahmed/Vocabify/android.yml?label=CI)](../../actions/workflows/android.yml)
[![License](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](./LICENSE)

*A clean, ad-free dictionary built on freeDictionaryAPI and Wiktionary — definitions, pronunciation, learning mode, and a persistent overlay search, fully offline-capable.*

[Download](#download) • [Features](#features) • [Tech Stack](#tech-stack) • [Building](#building)

</div>

---

## Features

- English-to-English dictionary via [freeDictionaryAPI](https://dictionaryapi.dev/)
- Multi-language to English lookup via the [Wiktionary](https://wiktionary.org) API (case-sensitive)
- Definitions, usage examples, synonyms/antonyms, and etymology where available
- Pronunciation: IPA text plus audio via text-to-speech and speech-to-text search input
- Search history and favourites, cached locally for offline reuse with an offline banner
- Flashcards learning mode built on your favourites
- Word of the day: daily notification plus home-screen widget with search
- Share-to-search: send text from any app straight into Vocabify
- Quick Settings tile for the persistent overlay (bubble) search
- Backup and restore: export and import your history and favourites
- Random word discovery
- Material 3 / Material You with dynamic color, dark, light, and OLED themes
- Adaptive layouts for phones, tablets, and landscape

## Screenshots

See the [Fastlane metadata images](./fastlane/metadata/android/en-US/images) for current captures.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.4 (JVM 21) |
| UI | Jetpack Compose, Material 3 / Material You, Navigation Compose |
| Architecture | Clean Architecture, MVI/MVVM, feature modules |
| DI | Hilt (KSP) |
| Networking | Retrofit 3 + Moshi (KSP codegen), Jsoup |
| Persistence | Room, DataStore Preferences, WorkManager |
| Media | Coil, Lottie, TTS |
| Build | Gradle (Kotlin DSL), AGP 9.4, version catalog |

## Project Structure

```
app/                 activity, navigation, DI wiring
strings/             shared string resources
common/              theme, shared composables, utilities
datastore/           DataStore preferences repository
search/              dictionary API layer (Retrofit/Moshi/Jsoup)
feature_home/        word search screen
feature_history/     search history
feature_favourites/  saved words
feature_learning/    flashcards learning mode
feature_overlay/     bubble overlay search
feature_settings/    app settings
feature_about/       about screen
```

## Download

Grab the latest signed APK from the [Releases](../../releases) page. Every release is built and signed by CI from a version tag.

## Building

```bash
git clone https://github.com/alzimerahmed/Vocabify
./gradlew build          # requires JDK 21 + Android SDK
```

<details>
<summary>Signing a release build</summary>

The release workflow (`.github/workflows/release.yml`) builds a signed APK on tag push. It expects four repository secrets: `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`. Never commit keystore material.

</details>

## Usage

Type a word into the search input. Results show definitions, examples, synonyms/antonyms, etymology, and IPA; tap the pronunciation control to hear the word via TTS. Long-press or use the menu to favourite a word, search a random one, or start flashcards from your favourites. Share text from any app to look it up instantly, or enable the overlay bubble to search without leaving your current app.

## Contributing

Fork the repo, create a feature branch, and open a pull request against `master`. CI runs the full Gradle build on every PR.

## Roadmap

- [ ] Additional source languages for Wiktionary lookup
- [ ] More learning-mode drills (spaced repetition)
- [ ] F-Droid / IzzyOnDroid distribution

## Changelog

See [GitHub Releases](../../releases) for version history.

## License

[GPL-3.0](./LICENSE) — original code Copyright (C) 2024 Yamin Siahmargooei; fork maintained by Alzimer Ahmed. Copyright and license notices are preserved as the license requires.
