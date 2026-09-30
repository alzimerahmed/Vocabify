/*
 *     Vocabify/Vocabify.common.main
 *     WordOfTheDayWords.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WordOfTheDayWords.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/Vocabify.common.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.domain.model

/**
 * Curated candidate words for the Word-of-the-Day feature (Feature 1).
 *
 * A bundled constant (not a network fetch or user-data-derived list) so
 * that the daily word is identical on every device and in the home-screen
 * widget, works fully offline on a fresh install, and is stable across
 * app updates within a day. All entries are distinct common English words
 * that dictionaryapi.dev / Wiktionary can define.
 */
object WordOfTheDayWords {
    val words: List<String> = listOf(
        "serendipity", "ephemeral", "eloquent", "resilience", "luminous",
        "solitude", "wanderlust", "petrichor", "halcyon", "sonorous",
        "ethereal", "luminosity", "reverie", "nostalgia", "aurora",
        "zenith", "tranquil", "cascade", "labyrinth", "mellifluous",
        "quintessential", "susurrus", "effervescent", "ineffable",
        "surreptitious", "ebullient", "idyllic", "epiphany",
        "serene", "vivacious", "wistful", "zephyr",
        "brevity", "celerity", "demure", "ebullience", "felicity",
        "gossamer", "incandescent", "jubilant", "kaleidoscope",
        "lissome", "murmuration", "nebulous", "opalescent", "panacea",
        "quiescent", "radiant", "scintilla", "tessellate", "umbra",
        "verdant", "whimsical", "yearn", "ambrosial",
        "beatitude", "cadence", "dulcet", "elysian", "fathom",
        "glimmer", "hallowed", "jocund", "kindle",
        "lustrous", "meander", "nocturne", "opulent", "pellucid",
        "quaff", "resonant", "sonata", "twilight", "unfurl",
        "venerate", "wondrous", "zestful",
        "brisk", "candor", "dapple", "eclipse", "flourish",
        "haven", "immaculate", "jubilance", "keen",
        "lucid", "nimble", "orchard", "placid",
        "quaint", "ripple", "shimmer", "tender",
        "velvet", "whisper", "yonder", "amber",
        "balm", "crisp", "dawn", "ember", "frost",
        "glow", "hush", "iris", "jewel", "kith",
        "lull", "mist", "nestle", "opal", "pearl",
        "quill", "revel", "sage", "twine", "utopia",
        "vow", "willow", "zinnia", "arcane",
        "buoyant", "candid", "diligent", "earnest", "fervent",
        "graceful", "humble", "intrepid", "jovial",
        "mirthful", "noble", "optimistic", "prudent",
        "quiet", "resolute", "sturdy", "tenacious", "unwavering",
        "valiant", "zealous", "astute", "bold",
        "curious", "devoted", "empathy", "fortitude", "generous",
        "harmony", "integrity", "justice", "kindness", "loyalty",
        "mercy", "nurture", "patience", "quest", "resolve",
        "sincerity", "trust", "unity", "vigor", "wisdom",
        "aplomb", "brio", "chutzpah", "decorum", "equanimity",
        "gravitas", "hubris", "innocence", "jubilee",
        "kudos", "luster", "moxie", "nuance", "overture",
        "panache", "quandary", "rapport", "temperance",
        "umbrage", "verve", "welfare", "yen",
        "bramble", "clover", "dune", "estuary",
        "fern", "glade", "heather", "isle", "juniper",
        "kelp", "lichen", "meadow", "nettle", "oasis",
        "prairie", "quarry", "reef", "sedge", "thicket",
        "upland", "vale", "woodland", "yarrow",
        "lambent", "evanescent", "opalescence", "halcyonic",
        "luminiferous", "vesperal", "zenithal", "amethyst",
        "brindled", "clement", "diaphanous", "effulgent",
        "fledgling", "kindled", "lambency", "mellowness",
        "pastoral", "quiescence", "resonance",
        "tranquility", "verdancy", "whimsy", "yearning"
    )
}
