/*
 *     Vocabify/Vocabify.search.main
 *     WiktionaryAPI.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WiktionaryAPI.kt Last modified at 2026/6/25
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

package io.github.yamin8000.owl.search.data.datasource.remote.wiktionary

import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.dto.WikiMeaningDto
import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.dto.WikiParseResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface WiktionaryAPI {

    @GET("page/definition/{term}")
    suspend fun search(
        @Path("term") term: String
    ): Map<String, List<WikiMeaningDto>>

    /**
     * Calls the MediaWiki API (`action=parse`) on Wiktionary. The [url]
     * must be absolute so it overrides the REST base URL of this Retrofit
     * instance.
     */
    @GET
    suspend fun parse(
        @Url url: String
    ): WikiParseResponseDto
}