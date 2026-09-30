/*
 *     Vocabify/Vocabify.datastore.main
 *     SettingsKeys.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     SettingsKeys.kt Last modified at 2024/8/19
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

package io.github.yamin8000.owl.datastore.domain.model

object SettingsKeys {
    const val THEME = "theme"
    const val TTS_LANG = "tts_lang"
    const val IS_VIBRATING = "is_vibrating"
    const val IS_STARTING_BLANK = "is_starting_blank"
    const val IS_DYNAMIC_COLOR = "is_dynamic_color"
    const val IS_WOTD_NOTIFICATION = "is_wotd_notification"
    const val ICON_VARIANT = "icon_variant"
    const val WOTD_WORD = "wotd_word"
    const val WOTD_DATE = "wotd_date"

    data object DictionarySource
}