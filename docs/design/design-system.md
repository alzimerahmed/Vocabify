# Vocabify Design System

> Concrete rules for every UI-facing change. Source of truth in code: `common/src/main/java/io/github/yamin8000/owl/common/ui/theme/` (Color.kt, Shape.kt, Sizes.kt, Theme.kt) and `common/ui/components/`.

## Tokens

### Color
- Full Material 3 scheme defined in `Color.kt` (`md_theme_light_*` / `md_theme_dark_*`) and wired in `Theme.kt` via `lightColorScheme(...)` / `darkColorScheme(...)`.
- **Dynamic color (Material You)** is applied on Android 12+ via `dynamicLightColorScheme`/`dynamicDarkColorScheme` — never bypass it.
- Three theme modes supported: Light, Dark, OLED (pure-black dark variant).
- **Rule**: no hardcoded `Color(0x...)` in feature code. Always `MaterialTheme.colorScheme.*`. Extend the palette only by adding new `md_theme_*` tokens, never inline values.

### Shape
- Token set in `Shape.kt`. Use `MaterialTheme.shapes.*`; do not invent ad-hoc `RoundedCornerShape(...)` where a token exists.

### Spacing & Sizing
- `Sizes` object provides the dp scale (xxSmall = 1.dp upward). Use `Sizes.*` for padding/gaps/sizes; no magic numbers.

### Typography & Motion
- Material 3 defaults via `MaterialTheme.typography.*`; Material 3 **Expressive** APIs and `MotionScheme` are already opted into (`Theme.kt`) — use the motion scheme for transitions instead of raw `tween`/`spring` values.
- Respect reduced-motion: avoid non-essential looping animations (Lottie) when the system requests reduced motion.

## Components
- Shared composables live in `common/ui/components/` (AppCard, AppText, ClickableIcon, EmptyList, HighlightText, MySnackbar, CrudContent family, ScaffoldWithTitle...). **Reuse before rewriting**; promote a pattern into `common` only after a second usage.
- State handling: every screen defines polished **empty** (`EmptyList`), **loading**, and **error** states — never a blank screen or raw stack trace. Offline is an explicit state, not a silent failure.

## Theming & Dark Mode
- Every new screen must render correctly in Light, Dark, and OLED. Verify by toggling the app theme setting, not just system dark mode.
- Status bar / system bars handled via `WindowCompat` edge-to-edge in `BaseActivity` — don't fight it with per-screen bar color hacks.

## Accessibility (minimums)
- Touch targets ≥ 48dp (use `ClickableIcon` which wraps minimum-target handling).
- Content descriptions for all icons/images; decorative elements get `null`.
- Text scales with system font scale — no fixed-height text containers.
- Contrast: WCAG AA against the resolved color scheme in both themes.

## Layout & Adaptivity
- Window size classes dependency (`material3-window-size`) is available — use it for tablet/landscape layouts (plan Phase 7).
- Handle long strings (long words, long definitions) with wrapping/ellipsis rules; support RTL mirroring (use start/end, not left/right).

## Anti-slop checks (before any UI diff ships)
- No one-off colors/dp/typography.
- All four states present (empty/loading/error/offline where relevant).
- Light + Dark + OLED verified.
- Icons have content descriptions.
- Motion uses MotionScheme tokens.
