/*
 *     Vocabify/Vocabify.search.main
 *     EtymologyParser.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     EtymologyParser.kt Last modified at 2026/6/25
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

import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.dto.WikiSectionDto
import org.jsoup.Jsoup

/**
 * Parses the HTML of a single Wiktionary section (as returned by the
 * MediaWiki parse API) into plain-text etymology paragraphs.
 */
object EtymologyParser {

    /**
     * Extracts readable paragraph text from a section's HTML.
     * Returns an empty string when the section has no paragraph content.
     */
    fun fromSectionHtml(sectionHtml: String): String {
        if (sectionHtml.isBlank()) return ""
        val document = Jsoup.parse(sectionHtml)
        document.select("sup").remove()
        return document.select("p")
            .map { it.wholeText().replace(Regex("\\s+"), " ").trim() }
            .filter { it.isNotEmpty() }
            .distinct()
            .joinToString(separator = "\n\n")
    }

    /**
     * Picks the section index of the etymology section from the list of
     * sections returned by the parse API. Wiktionary names them
     * "Etymology", "Etymology 1", …
     */
    fun findEtymologySectionIndex(sections: List<WikiSectionDto>): String? {
        return sections.firstOrNull { it.line.trim().startsWith("Etymology", ignoreCase = true) }?.index
    }
}
