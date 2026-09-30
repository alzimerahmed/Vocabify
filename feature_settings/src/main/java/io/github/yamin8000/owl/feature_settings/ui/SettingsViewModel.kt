/*
 *     Vocabify/Vocabify.feature.main
 *     SettingsViewModel.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingsViewModel.kt Last modified at 2024/8/19
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yamin8000.owl.common.util.TTS
import io.github.yamin8000.owl.datastore.domain.usecase.backup.BackupUseCases
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCases: SettingUseCases,
    private val backupUseCases: BackupUseCases,
    private val tts: TTS,
) : ViewModel() {
    private val scope = viewModelScope

    private var _state = MutableStateFlow(SettingsState())
    val state = _state.onStart {
        _state.update {
            it.copy(
                theme = useCases.getTheme(),
                ttsLang = useCases.getTTS(),
                isVibrating = useCases.getVibration(),
                isStartingBlank = useCases.getStartingBlank(),
                source = useCases.getSource(),
                isDynamicColor = useCases.getDynamicColor(),
                isWotdNotification = useCases.getWotdNotification(),
                iconVariant = useCases.getIconVariant(),
                languages = tts.languages().toImmutableList(),
                isTtsAvailable = tts.engine != null
            )
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5.seconds),
        SettingsState()
    )

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnStartingBlankChange -> {
                _state.update { it.copy(isStartingBlank = action.value) }
                scope.launch { useCases.setStartingBlank(action.value) }
            }

            is SettingsAction.OnTtsLangChange -> {
                _state.update { it.copy(ttsLang = action.value) }
                scope.launch { useCases.setTTS(action.value) }
            }

            is SettingsAction.OnVibrationChange -> {
                _state.update { it.copy(isVibrating = action.value) }
                scope.launch { useCases.setVibration(action.value) }
            }

            is SettingsAction.OnThemeChange -> {
                _state.update { it.copy(theme = action.newTheme) }
                scope.launch { useCases.setTheme(action.newTheme) }
            }

            is SettingsAction.OnSourceChanged -> {
                _state.update { it.copy(source = action.source) }
                scope.launch { useCases.setSource(action.source) }
            }

            is SettingsAction.OnTabChanged -> {
                _state.update { it.copy(currentTab = action.newTab) }
            }

            SettingsAction.OnExportData -> {
                scope.launch {
                    val json = try {
                        backupUseCases.exportUserData()
                    } catch (ignored: Exception) {
                        null
                    }
                    _state.update {
                        it.copy(
                            backupJson = json,
                            backupStatus = if (json != null) BackupStatus.Exported else BackupStatus.InvalidFile
                        )
                    }
                }
            }

            is SettingsAction.OnImportData -> {
                scope.launch {
                    val imported = try {
                        backupUseCases.importUserData(action.json)
                    } catch (ignored: Exception) {
                        null
                    }
                    _state.update {
                        it.copy(
                            backupStatus = when {
                                imported == null -> BackupStatus.InvalidFile
                                else -> BackupStatus.Imported(imported)
                            }
                        )
                    }
                }
            }

            SettingsAction.OnBackupJsonConsumed -> {
                _state.update { it.copy(backupJson = null) }
            }

            is SettingsAction.OnDynamicColorChange -> {
                _state.update { it.copy(isDynamicColor = action.value) }
                scope.launch { useCases.setDynamicColor(action.value) }
            }

            is SettingsAction.OnWotdNotificationChange -> {
                _state.update { it.copy(isWotdNotification = action.value) }
                scope.launch { useCases.setWotdNotification(action.value) }
            }

            is SettingsAction.OnIconVariantChange -> {
                _state.update { it.copy(iconVariant = action.variant) }
                scope.launch { useCases.setIconVariant(action.variant) }
            }
        }
    }
}