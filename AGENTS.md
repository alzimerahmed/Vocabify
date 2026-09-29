# Vocabify — Rules for AI Agents

## Project

Vocabify = fork of yamin8000/freeDictionaryApp (upstream: https://github.com/yamin8000/freeDictionaryApp, app formerly "Owl"). Offline-capable English dictionary app for [freeDictionaryAPI](https://dictionaryapi.dev/) (dictionaryapi.dev) plus multi-language lookup via Wiktionary. Features: definitions, examples, synonyms/antonyms, IPA + TTS pronunciation, search history, favourites, random word, persistent overlay (bubble) search, Material 3 / Material You with dark/light/OLED themes. Fully independent from upstream (history detached 2026-09-29; no code sync). Distribution: GitHub Releases (signed APKs); F-Droid listing is upstream's — not ours.

**License:** GPL-3.0 (inherited). The original copyright notice (Yamin Siahmargooei, 2024) MUST stay in LICENSE and in source-file headers — stripping it violates GPL-3.0. Our maintainer line is added alongside. See `docs/research.md` ADR-001.

**Scope:** Native Android app only. No backend of our own — the app talks to dictionaryapi.dev and Wiktionary directly. No web frontend.

## Tech Stack & Conventions (inherited from upstream — do not fight it)

### Language & Tooling
- **Language**: Kotlin 2.4.20, JVM target 21, coroutines + Flow.
- **UI**: Jetpack Compose (BOM 2026.08.00), Material 3 + Material You (dynamic color), window-size classes. Navigation Compose. No XML layouts.
- **DI**: Hilt 2.60.1 with KSP (no kapt anywhere).
- **Persistence**: Room 2.8.4 (search history/favourites cache), Jetpack DataStore Preferences (`:datastore` module).
- **Networking**: Retrofit 3 + Moshi (KSP codegen), Jsoup for Wiktionary HTML parsing, Coil for images, Lottie for animations.
- **Immutability**: kotlinx-collections-immutable in Compose state.
- **Build**: Gradle (Kotlin DSL), AGP 9.4.0, version catalog = `gradle/libs.versions.toml` — add ALL new deps via catalog aliases. compileSdk/targetSdk 37, minSdk 24. Core library desugaring ON. R8 minify + resource shrink on release.
- **Tests**: JUnit + datafaker present; Robolectric/Turbine when added.

### Architecture & Conventions
- **Modules**: `app` (activity/nav/DI), `strings` (shared string resources), `common` (theme/shared composables/utils), `datastore` (preferences repository), `feature_home` (search), `feature_settings`, `feature_history`, `feature_favourites`, `feature_overlay` (bubble search), `feature_about`, `search` (network/domain layer for dictionary APIs).
- **Package namespace is KEPT as `io.github.yamin8000.owl`** (ADR-002 in `docs/research.md`) — do not rename applicationId/namespace without a dedicated planned phase.
- **Clean Architecture**: feature modules split data/domain/presentation; MVI/MVVM with ViewModels; state down / events up; StateFlow + `collectAsStateWithLifecycle`.
- **Network discipline**: all API calls behind Retrofit interfaces in `:search`; parse Wiktionary HTML only via Jsoup wrappers; never block main thread; handle offline/error states explicitly.

### Build Inputs (secrets/config — never commit)
- Release signing: CI decodes `ANDROID_KEYSTORE_BASE64` + `ANDROID_KEY_ALIAS`/`ANDROID_KEYSTORE_PASSWORD`/`ANDROID_KEY_PASSWORD` secrets into a keystore at build time. Never commit keystore material.
- No API keys required — dictionaryapi.dev and Wiktionary are keyless.

## Build / Verify

```bash
./gradlew build              # full build + unit tests (CI)
./gradlew assembleRelease    # signed release APK (CI, tag-triggered)
```

**Remote-first verification (mandatory):** we do NOT build or test locally — all builds/tests run in GitHub Actions (`.github/workflows/android.yml` on push/PR to master; `.github/workflows/release.yml` on tag push). Local work is edit-only: IDE typecheck / targeted static review while iterating. Push a branch and let CI verify. A CI green check counts as the gate; never re-run it locally for ceremony. Local builds only when debugging the build system itself (requires JDK 21 + Android SDK).

**IMPORTANT:** `./gradlew` first run downloads Gradle + all deps — slow; if ever run locally, background + poll.

## Gotchas

- **Kotlin 2.4.20 + Compose compiler are tied** via the `org.jetbrains.kotlin.plugin.compose` plugin — never pin a separate compose-compiler version.
- **KSP (not kapt)** for Hilt, Room, and Moshi codegen — mismatched KSP/Kotlin versions fail the build; keep `ksp` version ref aligned with Kotlin.
- **Material 3 is an alpha artifact** (`1.5.0-alpha27`) — expect API churn on upgrades; check breaking changes before bumping.
- **Release build is minified** (R8 + shrinkResources) — debug green does NOT guarantee release green; validate with `assembleRelease` before tagging.
- **versionName vs tag alignment**: `app/build.gradle.kts` `versionName` must match the git tag before pushing a release tag.
- **GPL attribution** — keep upstream copyright in LICENSE and file headers (see License above).
- **Wiktionary search is case-sensitive** and language-dependent — preserve upstream behavior unless a phase explicitly changes it.

## Agent Guidelines & Constraints

### Do's
- **Follow the module boundaries**; new features get their own `feature_*` module (or package) following the existing feature-module pattern.
- **Route UI through the design system** (`docs/design/design-system.md`): Material 3 tokens, dark/light/OLED parity, empty/loading/error states, a11y minimums.
- **Write tests** for new use cases/repositories (behavior, not implementation).
- **Keep network usage behind the `:search` layer** — no Retrofit calls from composables.
- **Conventional Commits**: `type(scope): description`.

### Don'ts
- **NO local builds or test runs** — verification is CI-only (user directive).
- **NO package/applicationId renames** without a dedicated planned phase (ADR-002).
- **NO new heavy dependencies** without a decision record in `docs/research.md` (license must stay GPL-3.0-compatible).
- **NO secrets in code** — keystore material via GitHub secrets only.
- **NO deleting resources** without grepping all reference types (manifest, `R.*`, `@drawable/...`) — Android R-reference rule (`.devin/prompt/phase.md` §19).

## Agent Guidelines & Workflow (this repo's .devin system)

### Resource Discipline (mandatory, non-trivial tasks)
Before any non-trivial task:
1. Read `docs/toolset.md` intent-map (task type → resources)
2. Invoke every skill + sub-agent in that row
3. Read every rule for that task type (`.devin/rules/`)
4. At task end: `code-reviewer` sub-agent on final diff (non-negotiable)
5. Append learnings via `/ce-compound` if durable lesson

Phase implementations (task completes a docs/plan.md row): follow `.devin/prompt/phase.md`.

Skip all this for single-line edits, pure Q&A, reading files.

### Project-Type Filter (Android dictionary app)
Per `docs/toolset.md` intent-map:
- **Skip web-only:** pwa-engineer, seo-specialist, css-architect, playwright-design-clone, payment-integrator, email-engineer, monorepo-manager, realtime-engineer, backend-architect, database-engineer (server-side), web-scraper.
- **Keep universal:** code-reviewer, debugger, test-engineer, security-auditor, performance-engineer (startup/jank), git-master, migration-specialist (AGP/Kotlin upgrades), docs-writer, i18n-specialist (multi-language Wiktionary/RTL), build-optimizer (Gradle), caveman-compressor, vibe-coding-auditor, type-safety-engineer, state-manager (MVI/Flow), search-architect (word search UX), database-engineer (Room), media-optimizer (Coil/Lottie/audio), animation-engineer, frontend-designer (Compose taste), content-writer.
- **Quality gates:** CI-only — `./gradlew build` on GitHub Actions. No local builds.

## Communication Style

Default **caveman-lite** (lightly compressed, readable, technically accurate). `/caveman` skill for full/ultra/wenyan modes.

## Quick Task Flow

Quick tasks: `.devin/prompt/quick.md` (commandments) + `.devin/prompt/rules.md` (scoping, verification, escalation). Phased work: `.devin/prompt/phase.md`.

## Key References

> Note: `docs/*.md` files below are the private knowledge layer (gitignored) — they exist only in the local workspace, not for external cloners.

- `docs/toolset.md` — intent map (task type → skills, sub-agents, rules)
- `docs/plan.md` — phased plan + status
- `docs/project.md` — project state/structure
- `docs/tools-log.md` — .devin resources invoked per session
- `docs/CONCEPTS.md` — project vocabulary
- `docs/research.md` — research, ADRs, gotchas, open questions
- `docs/idea.md` — competitive analysis + Feature Gap List
- `docs/design/design-system.md` — UI tokens + rules
- Upstream reference — https://github.com/yamin8000/freeDictionaryApp (no code sync)
