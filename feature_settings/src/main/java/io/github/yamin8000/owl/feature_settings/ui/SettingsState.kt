/*
 *     Vocabify/Vocabify.feature.main
 *     SettingsState.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingsState.kt Last modified at 2024/8/19
 *     This file is part of Vocabify/Vocabify.feature.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.feature_settings.ui

import io.github.yamin8000.owl.common.domain.model.DictionarySource
import io.github.yamin8000.owl.datastore.domain.model.IconVariant
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

data class SettingsState(
    val theme: ThemeType = ThemeType.System,
    val ttsLang: String = "en-US",
    val isVibrating: Boolean = true,
    val isStartingBlank: Boolean = true,
    val languages: ImmutableList<Locale> = persistentListOf(),
    val source: DictionarySource = DictionarySource.FreeDictionary,
    val currentTab: SettingsTab = SettingsTab.General,
    val isTtsAvailable: Boolean = false,
    val backupJson: String? = null,
    val backupStatus: BackupStatus = BackupStatus.Idle,
    val isDynamicColor: Boolean = true,
    val isWotdNotification: Boolean = false,
    val iconVariant: IconVariant = IconVariant.Default
)

sealed interface BackupStatus {
    data object Idle : BackupStatus
    data object Exported : BackupStatus
    data class Imported(val entryCount: Int) : BackupStatus
    data object InvalidFile : BackupStatus
}
