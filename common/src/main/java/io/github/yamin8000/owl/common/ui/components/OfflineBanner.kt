/*
 *     Vocabify/freeDictionaryApp.common.main
 *     OfflineBanner.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     OfflineBanner.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.common.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.yamin8000.owl.common.ui.theme.Sizes

/**
 * Banner shown when the device is offline. Uses only Material 3
 * tokens so it renders correctly in light, dark and OLED themes.
 * The [text] is mandatory so this composable stays free of any
 * module-specific string resources.
 */
@Composable
fun OfflineBanner(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        tonalElevation = Sizes.xSmall,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Sizes.Medium),
                horizontalArrangement = Arrangement.spacedBy(
                    Sizes.Small,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.CloudOff,
                        contentDescription = null
                    )
                    AppText(text = text)
                }
            )
        }
    )
}
