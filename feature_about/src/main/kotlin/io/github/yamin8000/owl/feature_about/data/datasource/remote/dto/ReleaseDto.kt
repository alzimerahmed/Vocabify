/*
 *     Vocabify/Vocabify.feature.main
 *     ReleaseDto.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     ReleaseDto.kt Last modified at 2025/11/16
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

package io.github.yamin8000.owl.feature_about.data.datasource.remote.dto

import com.squareup.moshi.JsonClass
import io.github.yamin8000.owl.feature_about.domain.Release

@JsonClass(generateAdapter = true)
data class ReleaseDto(
    val id: Long,
    val name: String
) {
    fun domain() = Release(
        id = id,
        name = name
    )
}
