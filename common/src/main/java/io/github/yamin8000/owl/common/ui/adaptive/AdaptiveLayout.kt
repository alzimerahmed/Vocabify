/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     AdaptiveLayout.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     AdaptiveLayout.kt Last modified at 2026/1/1
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Pure layout-selection logic for adaptive (window-size-class aware) screens.
 * Kept free of Compose state so it can be unit tested on the JVM.
 */
object AdaptiveLayout {

    /** Readability cap for text-heavy content on expanded widths. */
    val MaxContentWidth: Dp = 720.dp

    /**
     * Two-pane (list-detail) layout is used for word detail on expanded width.
     */
    fun shouldUseTwoPane(widthSizeClass: WindowWidthSizeClass): Boolean =
        widthSizeClass == WindowWidthSizeClass.Expanded
}

/**
 * Width size class of the current window, provided by [MainActivity] via
 * [androidx.compose.material3.windowsizeclass.calculateWindowSizeClass].
 * Defaults to Compact so standalone windows (e.g. the overlay bubble) keep
 * their compact behavior.
 */
val LocalWindowWidthSizeClass = staticCompositionLocalOf { WindowWidthSizeClass.Compact }

/**
 * Centers [content] horizontally and caps its width to [maxWidth] on expanded
 * windows for readability. [Dp.Unspecified] (or compact/medium windows) leaves
 * the layout untouched.
 */
@Composable
fun AdaptiveMaxWidthContent(
    modifier: Modifier = Modifier,
    maxWidth: Dp = AdaptiveLayout.MaxContentWidth,
    content: @Composable () -> Unit
) {
    val widthSizeClass = LocalWindowWidthSizeClass.current
    val capped = maxWidth != Dp.Unspecified &&
        widthSizeClass == WindowWidthSizeClass.Expanded
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
        content = {
            Box(
                modifier = if (capped) Modifier.widthIn(max = maxWidth) else Modifier,
                content = { content() }
            )
        }
    )
}
