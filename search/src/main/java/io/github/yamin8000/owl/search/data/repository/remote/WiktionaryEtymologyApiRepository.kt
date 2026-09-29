/*
 *     Vocabify/freeDictionaryApp.search.main
 *     WiktionaryEtymologyApiRepository.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WiktionaryEtymologyApiRepository.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.search.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.search.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.search.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.search.data.repository.remote

import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.EtymologyParser
import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.WiktionaryAPI
import io.github.yamin8000.owl.search.domain.repository.remote.WiktionaryEtymologyRepository
import java.net.URLEncoder

class WiktionaryEtymologyApiRepository(
    private val api: WiktionaryAPI
) : WiktionaryEtymologyRepository {

    override suspend fun etymology(word: String): String? {
        val term = word.trim()
        if (term.isEmpty()) return null

        val sectionsUrl = buildParseUrl(term, prop = "sections")
        val sectionIndex = EtymologyParser.findEtymologySectionIndex(
            api.parse(sectionsUrl).parse?.sections.orEmpty()
        ) ?: return null

        val sectionUrl = buildParseUrl(term, prop = "text", section = sectionIndex)
        val sectionHtml = api.parse(sectionUrl).parse?.text ?: return null
        return EtymologyParser.fromSectionHtml(sectionHtml).takeIf { it.isNotEmpty() }
    }

    private fun buildParseUrl(
        term: String,
        prop: String,
        section: String? = null
    ): String = buildString {
        append("https://en.wiktionary.org/w/api.php")
        append("?action=parse")
        append("&page=").append(URLEncoder.encode(term, "UTF-8"))
        append("&prop=").append(prop)
        if (section != null) append("&section=").append(section)
        append("&redirects=1")
        append("&format=json")
    }
}
