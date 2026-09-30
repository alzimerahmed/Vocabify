/*
 *     Vocabify/Vocabify.feature.main
 *     DynamicThemeNotice.kt Copyrighted by Yamin Siahmargooei at 2025/2/7
 *     DynamicThemeNotice.kt Last modified at 2025/2/7
 *     This file is part of Vocabify/Vocabify.feature.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.feature.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.feature.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_settings.ui.components.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.strings.R

@Composable
internal fun DynamicThemeNotice(
    modifier: Modifier = Modifier,
) {
    AppText(
        modifier = modifier,
        text = stringResource(R.string.dynamic_theme_notice),
        textAlign = TextAlign.Justify
    )
}