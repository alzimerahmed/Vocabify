/*
 *     Vocabify/Vocabify.search.main
 *     EntryRoomRepository.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     EntryRoomRepository.kt Last modified at 2024/8/30
 *     This file is part of Vocabify/Vocabify.search.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

package io.github.yamin8000.owl.search.data.repository.local

import io.github.yamin8000.owl.common.util.StringUtils.sanitizeWord
import io.github.yamin8000.owl.common.util.DateTimeUtils.epoch
import io.github.yamin8000.owl.search.data.datasource.local.dao.DAOs
import io.github.yamin8000.owl.search.data.datasource.local.entity.EntryEntity
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.search.domain.repository.local.EntryRepository
import io.github.yamin8000.owl.search.domain.repository.local.MeaningRepository
import io.github.yamin8000.owl.search.domain.repository.local.PhoneticRepository
import kotlinx.collections.immutable.toImmutableList

class EntryRoomRepository(
    private val dao: DAOs.EntryDao,
    private val phoneticRepository: PhoneticRepository,
    private val meaningRepository: MeaningRepository
) : EntryRepository, BaseRoomRepository<Entry, EntryEntity>(dao) {

    override suspend fun add(item: Entry): Long {
        return dao.insert(
            EntryEntity(
                word = item.word,
                createdAt = epoch()
            )
        )
    }

    override suspend fun findByTerm(term: String): List<Entry> {
        return dao.where("word", sanitizeWord(term)).mapNotNull { mapToDomain(it) }
    }

    override suspend fun prune(maxEntries: Int): Int {
        if (maxEntries <= 0) return 0
        val entities = dao.all()
        val toRemove = entities
            .sortedByDescending { it.createdAt }
            .drop(maxEntries)
        if (toRemove.isEmpty()) return 0
        dao.deleteAll(toRemove)
        return toRemove.size
    }

    override suspend fun mapToDomain(item: EntryEntity?): Entry? {
        return if (item != null) {
            Entry(
                id = item.id,
                word = item.word,
                phonetics = phoneticRepository.findAllByEntryId(item.id).toImmutableList(),
                meanings = meaningRepository.findAllByEntryId(item.id).toImmutableList()
            )
        } else null
    }

    override suspend fun findEntity(item: Entry): EntryEntity? {
        return if (item.id != null) dao.find(item.id)
        else null
    }
}