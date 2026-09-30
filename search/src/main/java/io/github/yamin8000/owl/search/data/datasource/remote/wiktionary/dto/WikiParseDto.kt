/*
 *     Vocabify/Vocabify.search.main
 *     WikiParseDto.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WikiParseDto.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/Vocabify.search.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.search.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.search.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.dto

import com.squareup.moshi.JsonClass

/**
 * Response of the MediaWiki `action=parse` API used to fetch
 * Wiktionary etymology content. Depending on the requested `prop`,
 * either [WikiParseContentDto.sections] (prop=sections) or
 * [WikiParseContentDto.text] (prop=text) is populated.
 */
@JsonClass(generateAdapter = true)
data class WikiParseResponseDto(
    val parse: WikiParseContentDto? = null
)

@JsonClass(generateAdapter = true)
data class WikiParseContentDto(
    val title: String? = null,
    val sections: List<WikiSectionDto>? = null,
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class WikiSectionDto(
    val index: String,
    val line: String
)
