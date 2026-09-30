/*
 *     Vocabify/Vocabify.datastore.main
 *     SettingsRepository.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingsRepository.kt Last modified at 2024/8/19
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

package io.github.yamin8000.owl.datastore.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.github.yamin8000.owl.common.domain.model.DictionarySource
import io.github.yamin8000.owl.datastore.domain.model.IconVariant
import io.github.yamin8000.owl.datastore.domain.model.SettingsKeys
import io.github.yamin8000.owl.datastore.domain.model.ThemeType
import io.github.yamin8000.owl.datastore.domain.repository.BaseDatastoreRepository
import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class SettingsDatastoreRepository(
    private val datastore: DataStore<Preferences>
) : SettingsRepository, BaseDatastoreRepository by BasicDatastoreRepository(datastore) {

    override suspend fun getTheme(): ThemeType {
        return ThemeType.toType(getString(SettingsKeys.THEME))
    }

    override suspend fun setTheme(theme: ThemeType) {
        setString(SettingsKeys.THEME, theme.toString())
    }

    override suspend fun getTtsLang(): String? {
        return getString(SettingsKeys.TTS_LANG)
    }

    override suspend fun setTtsLang(ttsLang: String) {
        setString(SettingsKeys.TTS_LANG, ttsLang)
    }

    override suspend fun getIsVibrating(): Boolean {
        return getBool(SettingsKeys.IS_VIBRATING) != false
    }

    override suspend fun setIsVibrating(value: Boolean) {
        setBool(SettingsKeys.IS_VIBRATING, value)
    }

    override suspend fun getIsStartingBlank(): Boolean {
        return getBool(SettingsKeys.IS_STARTING_BLANK) != false
    }

    override suspend fun setIsStartingBlank(value: Boolean) {
        setBool(SettingsKeys.IS_STARTING_BLANK, value)
    }

    override suspend fun getDictionarySource(): DictionarySource {
        val source = getString(SettingsKeys.DictionarySource.toString())
        return DictionarySource.valueOf(source ?: DictionarySource.Wiktionary.name)
    }

    override suspend fun setDictionarySource(source: DictionarySource) {
        setString(SettingsKeys.DictionarySource.toString(), source.name)
    }

    override suspend fun getIsDynamicColor(): Boolean {
        return getBool(SettingsKeys.IS_DYNAMIC_COLOR) != false
    }

    override suspend fun setIsDynamicColor(value: Boolean) {
        setBool(SettingsKeys.IS_DYNAMIC_COLOR, value)
    }

    override suspend fun getIsWotdNotification(): Boolean {
        return getBool(SettingsKeys.IS_WOTD_NOTIFICATION) == true
    }

    override suspend fun setIsWotdNotification(value: Boolean) {
        setBool(SettingsKeys.IS_WOTD_NOTIFICATION, value)
    }

    override suspend fun getIconVariant(): IconVariant {
        return IconVariant.toVariant(getString(SettingsKeys.ICON_VARIANT))
    }

    override suspend fun setIconVariant(variant: IconVariant) {
        setString(SettingsKeys.ICON_VARIANT, variant.name)
    }

    override suspend fun getWotdWord(): String? {
        return getString(SettingsKeys.WOTD_WORD)
    }

    override suspend fun setWotdWord(word: String) {
        setString(SettingsKeys.WOTD_WORD, word)
    }

    override suspend fun getWotdDate(): String? {
        return getString(SettingsKeys.WOTD_DATE)
    }

    override suspend fun setWotdDate(date: String) {
        setString(SettingsKeys.WOTD_DATE, date)
    }
}