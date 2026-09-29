/*
 *     Vocabify/freeDictionaryApp.feature_settings.main
 *     PersonalizationSettings.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     PersonalizationSettings.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.feature_settings.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.feature_settings.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_settings.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Style
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.core.content.ContextCompat
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.DefaultCutShape
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.datastore.domain.model.IconVariant
import io.github.yamin8000.owl.strings.R

/**
 * Feature 11: Material You dynamic-color toggle. Only meaningful on
 * Android 12+; hidden below.
 */
@Composable
internal fun DynamicColorSetting(
    isDynamicColor: Boolean,
    onDynamicColorChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
    SwitchItem(
        modifier = modifier,
        imageVector = Icons.TwoTone.Style,
        caption = stringResource(R.string.dynamic_color),
        checked = isDynamicColor,
        onCheckedChange = onDynamicColorChange
    )
}

/**
 * Feature 1: daily Word-of-the-Day notification toggle. Requests the
 * POST_NOTIFICATIONS runtime permission on API 33+; stays off when the
 * user denies it.
 */
@Composable
internal fun WotdNotificationSetting(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        onEnabledChange(granted)
    }

    SwitchItem(
        modifier = modifier,
        imageVector = Icons.TwoTone.CalendarMonth,
        caption = stringResource(R.string.wotd_notification),
        checked = isEnabled,
        onCheckedChange = { enabled ->
            when {
                !enabled -> onEnabledChange(false)

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED ->
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

                else -> onEnabledChange(true)
            }
        }
    )
}

/**
 * Feature 11: launcher icon chooser (default vs. monochrome alternate
 * icon applied via activity-alias).
 */
@Composable
internal fun IconVariantSetting(
    currentVariant: IconVariant,
    onVariantChange: (IconVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    var isShowingDialog by remember { mutableStateOf(false) }

    SettingsItemCard(
        modifier = modifier,
        title = stringResource(R.string.app_icon),
        content = {
            if (isShowingDialog) {
                IconVariantDialog(
                    currentVariant = currentVariant,
                    onVariantChange = {
                        onVariantChange(it)
                        isShowingDialog = false
                    },
                    onDismiss = { isShowingDialog = false }
                )
            }
            SettingsItem(
                onClick = { isShowingDialog = true },
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Sizes.Small, Alignment.Start)
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.Style,
                            contentDescription = null
                        )
                        AppText(
                            text = when (currentVariant) {
                                IconVariant.Default -> stringResource(R.string.app_icon_default)
                                IconVariant.Monochrome -> stringResource(R.string.app_icon_monochrome)
                            },
                            maxLines = 2
                        )
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IconVariantDialog(
    currentVariant: IconVariant,
    onVariantChange: (IconVariant) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        content = {
            Surface(
                shape = DefaultCutShape,
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(
                            Sizes.Small,
                            Alignment.CenterVertically
                        ),
                        modifier = Modifier
                            .padding(Sizes.Large)
                            .selectableGroup()
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    Sizes.Small,
                                    Alignment.CenterHorizontally
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.TwoTone.Style,
                                    contentDescription = null
                                )
                                AppText(text = stringResource(R.string.app_icon))
                            }
                            IconVariant.entries().forEach { variant ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(
                                        Sizes.xSmall,
                                        Alignment.Start
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = variant == currentVariant,
                                            role = Role.RadioButton,
                                            onClick = {
                                                onVariantChange(variant)
                                                onDismiss()
                                            }
                                        )
                                ) {
                                    RadioButton(
                                        modifier = Modifier.padding(start = Sizes.Medium),
                                        selected = variant == currentVariant,
                                        onClick = null
                                    )
                                    AppText(
                                        modifier = Modifier.padding(vertical = Sizes.Large),
                                        text = when (variant) {
                                            IconVariant.Default -> stringResource(R.string.app_icon_default)
                                            IconVariant.Monochrome -> stringResource(R.string.app_icon_monochrome)
                                        }
                                    )
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}
