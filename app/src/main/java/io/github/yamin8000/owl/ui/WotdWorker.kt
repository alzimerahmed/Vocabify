/*
 *     Vocabify/Vocabify.app.main
 *     WotdWorker.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     WotdWorker.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/Vocabify.app.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/Vocabify.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/Vocabify.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Vocabify.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import io.github.yamin8000.owl.common.domain.model.WordOfTheDaySelector
import io.github.yamin8000.owl.common.domain.model.WordOfTheDayWords
import io.github.yamin8000.owl.common.util.log
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Daily Word-of-the-Day job (Feature 1).
 *
 * Runs once a day: resolves today's deterministic word, caches it in
 * DataStore (so the widget and the home card can read it without any
 * network call), posts the daily notification when enabled, and refreshes
 * the home-screen widget.
 *
 * Hilt entry point is used instead of @HiltWorker to avoid extra
 * androidx.hilt work-integration dependencies.
 */
internal class WotdWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext,
                WotdEntryPoint::class.java
            )
            val settings = entryPoint.settings()

            val today = todayStamp()
            val word = if (settings.getWotdDate() == today) {
                settings.getWotdWord() ?: selectTodaysWord().also { settings.setWotdWord(it) }
            } else {
                selectTodaysWord().also {
                    settings.setWotdWord(it)
                    settings.setWotdDate(today)
                }
            }

            if (settings.getWotdNotification() && WotdNotifier.hasNotificationPermission(applicationContext)) {
                WotdNotifier.notify(applicationContext, word)
            }

            VocabifyWidgetProvider.updateAll(applicationContext, word)
            Result.success()
        } catch (e: Exception) {
            log(e.stackTraceToString())
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "wotd_daily"

        /** Days since epoch, computed without java.time (lint NewApi on minSdk 24). */
        fun epochDay(): Long {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            val millis = calendar.timeInMillis
            return Math.floorDiv(millis, MILLIS_PER_DAY)
        }

        fun todayStamp(): String {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            return format.format(Date())
        }

        /**
         * Schedules the daily WOTD work around 09:00 local time. Idempotent —
         * safe to call on every app start; KEEP keeps the existing schedule.
         */
        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<WotdWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelayToNextMorning(), TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WotdEntryPoint {
    fun settings(): SettingUseCases
}

internal fun selectTodaysWord(): String {
    return WordOfTheDaySelector.select(WordOfTheDayWords.words, WotdWorker.epochDay())
}

private const val MILLIS_PER_DAY = 24L * 60 * 60 * 1000

/**
 * Milliseconds from now until the next 09:00 local time.
 */
private fun initialDelayToNextMorning(): Long {
    val calendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return (calendar.timeInMillis - System.currentTimeMillis()).coerceAtLeast(0L)
}
