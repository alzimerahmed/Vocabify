# Vocabify — offline-friendly English dictionary for Android

<div align="center">

[![Android CI](https://github.com/yamin8000/freeDictionaryApp/actions/workflows/android.yml/badge.svg)](https://github.com/yamin8000/freeDictionaryApp/actions/workflows/android.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=android&logoColor=white)](https://developer.android.com/compose)
[![License](https://img.shields.io/badge/License-GPL--3.0-blue.svg)](./LICENSE)

*A clean, ad-free dictionary app built on freeDictionaryAPI and Wiktionary — definitions, pronunciation, history, favourites, and a persistent overlay search.*

[Download](#download) • [Features](#features) • [Tech Stack](#tech-stack) • [Building](#building)

</div>

---

## Features

- English-to-English dictionary via [freeDictionaryAPI](https://dictionaryapi.dev/)
- Multi-language to English lookup via the [Wiktionary](https://wiktionary.org) API (case-sensitive)
- Definitions, usage examples, synonyms/antonyms where available
- Pronunciation: IPA text plus audio via TTS
- Search history and favourites, stored locally for offline reuse
- Random word discovery
- Persistent overlay (bubble) search from any app
- Material 3 / Material You with dynamic color, dark, light, and OLED themes

## Screenshots

See [`screenshots/`](./screenshots) and the Fastlane metadata images for current captures.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.4 (JVM 21) |
| UI | Jetpack Compose, Material 3 / Material You, Navigation Compose |
| Architecture | Clean Architecture, MVI/MVVM, feature modules |
| DI | Hilt (KSP) |
| Networking | Retrofit 3 + Moshi (KSP codegen), Jsoup |
| Persistence | Room, DataStore Preferences |
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
feature_overlay/     bubble overlay search
feature_settings/    app settings
feature_about/       about screen
```

## Download

- GitHub Releases: [here](https://github.com/yamin8000/freeDictionaryApp/releases)

## Building

```bash
git clone <this repo>
./gradlew build          # requires JDK 21 + Android SDK
```

<details>
<summary>Signing a release build</summary>

The release workflow (`.github/workflows/release.yml`) builds a signed APK on tag push. It expects four repository secrets: `ANDROID_KEYSTORE_BASE64`, `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, `ANDROID_KEY_PASSWORD`. Never commit keystore material.

</details>

## Usage

Type a word into the search input. Results show definitions, examples, synonyms/antonyms, and IPA; tap the pronunciation control to hear the word via TTS. Long-press or use the menu to favourite a word or search a random one.

## Contributing

Fork the repo, create a feature branch, and open a pull request against `master`. CI runs the full Gradle build on every PR.

## Roadmap

- [ ] Feature pipeline tracked in the internal `docs/plan.md`
- [ ] Additional source languages for Wiktionary lookup
- [ ] Word-of-the-day widget

## License

[GPL-3.0](./LICENSE) — original code Copyright (C) 2024 Yamin Siahmargooei; fork maintained by Alzimer Ahmed. Copyright and license notices are preserved as the license requires.
