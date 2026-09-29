/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     AdaptiveLayoutTest.kt Copyrighted by Yamin Siahmargooei at 2026/1/1
 *     AdaptiveLayoutTest.kt Last modified at 2026/1/1
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

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdaptiveLayoutTest {

    @Test
    fun `two pane is used only on expanded width`() {
        assertTrue(
            AdaptiveLayout.shouldUseTwoPane(WindowWidthSizeClass.Expanded)
        )
        assertFalse(
            AdaptiveLayout.shouldUseTwoPane(WindowWidthSizeClass.Medium)
        )
        assertFalse(
            AdaptiveLayout.shouldUseTwoPane(WindowWidthSizeClass.Compact)
        )
    }
}
