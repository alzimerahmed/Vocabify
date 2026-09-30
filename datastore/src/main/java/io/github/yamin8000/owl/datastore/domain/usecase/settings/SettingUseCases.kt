/*
 *     Vocabify/Vocabify.datastore.main
 *     SettingUseCases.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingUseCases.kt Last modified at 2024/8/19
 *     This file is part of Vocabify/Vocabify.datastore.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.datastore.domain.usecase.settings

data class SettingUseCases(
    val getTTS: GetTTS,
    val setTTS: SetTTS,
    val getTheme: GetTheme,
    val setTheme: SetTheme,
    val getVibration: GetVibration,
    val setVibration: SetVibration,
    val getStartingBlank: GetStartingBlank,
    val setStartingBlank: SetStartingBlank,
    val getSource: GetDictionarySource,
    val setSource: SetDictionarySource,
    val getDynamicColor: GetDynamicColor,
    val setDynamicColor: SetDynamicColor,
    val getWotdNotification: GetWotdNotification,
    val setWotdNotification: SetWotdNotification,
    val getIconVariant: GetIconVariant,
    val setIconVariant: SetIconVariant,
    val getWotdWord: GetWotdWord,
    val setWotdWord: SetWotdWord,
    val getWotdDate: GetWotdDate,
    val setWotdDate: SetWotdDate
)
