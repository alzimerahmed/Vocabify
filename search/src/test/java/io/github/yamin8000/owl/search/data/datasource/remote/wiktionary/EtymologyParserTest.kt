/*
 *     Vocabify/freeDictionaryApp.search.test
 *     EtymologyParserTest.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     EtymologyParserTest.kt Last modified at 2026/6/25
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

package io.github.yamin8000.owl.search.data.datasource.remote.wiktionary

import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.EtymologyParser
import io.github.yamin8000.owl.search.data.datasource.remote.wiktionary.dto.WikiSectionDto
import org.junit.Assert.assertEquals
import org.junit.Test

class EtymologyParserTest {

    @Test
    fun `extracts plain text from section paragraphs`() {
        val html = "<p>From <i>Old English</i> <b>free</b>.</p><p>Attested since the 9th century.</p>"
        assertEquals(
            "From Old English free.\n\nAttested since the 9th century.",
            EtymologyParser.fromSectionHtml(html)
        )
    }

    @Test
    fun `removes reference superscripts`() {
        val html = "<p>From Latin <sup class=\"reference\">[1]</sup> liber.</p>"
        assertEquals("From Latin liber.", EtymologyParser.fromSectionHtml(html))
    }

    @Test
    fun `returns empty string for blank or paragraph-less html`() {
        assertEquals("", EtymologyParser.fromSectionHtml(""))
        assertEquals("", EtymologyParser.fromSectionHtml("   "))
        assertEquals("", EtymologyParser.fromSectionHtml("<table><tr><td>x</td></tr></table>"))
    }

    @Test
    fun `finds the etymology section case-insensitively`() {
        val sections = listOf(
            WikiSectionDto(index = "1", line = "Pronunciation"),
            WikiSectionDto(index = "2", line = "etymology 1"),
            WikiSectionDto(index = "2", line = "Noun")
        )
        assertEquals("1", EtymologyParser.findEtymologySectionIndex(sections))
    }

    @Test
    fun `returns null when there is no etymology section`() {
        val sections = listOf(
            WikiSectionDto(index = "1", line = "Pronunciation"),
            WikiSectionDto(index = "2", line = "Noun")
        )
        assertEquals(null, EtymologyParser.findEtymologySectionIndex(sections))
    }
}
