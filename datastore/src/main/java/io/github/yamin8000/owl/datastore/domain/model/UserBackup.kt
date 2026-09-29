/*
 *     Vocabify/freeDictionaryApp.data.main
 *     UserBackup.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     UserBackup.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.datastore.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.datastore.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.datastore.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.datastore.domain.model

import com.squareup.moshi.JsonClass

/**
 * User data (search history + favourites) in the backup file format.
 * Versioned so future format changes stay backwards compatible.
 */
@JsonClass(generateAdapter = true)
data class UserBackup(
    val version: Int = CURRENT_VERSION,
    val history: List<String> = emptyList(),
    val favourites: List<String> = emptyList()
) {
    companion object {
        const val CURRENT_VERSION = 1
    }
}
