/*
 *     Vocabify/Vocabify.datastore.main
 *     IconVariant.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     IconVariant.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/Vocabify.datastore.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.domain.model

import androidx.compose.runtime.Stable

/**
 * Launcher icon options (Feature 11). Each entry maps to an
 * activity-alias in the app manifest that is enabled/disabled via
 * [android.content.pm.PackageManager.setComponentEnabledSetting].
 */
@Stable
enum class IconVariant {
    Default,
    Monochrome;

    companion object {
        fun toVariant(value: String?): IconVariant {
            return entries().firstOrNull { it.name == value } ?: Default
        }

        fun entries() = listOf(Default, Monochrome)
    }
}
