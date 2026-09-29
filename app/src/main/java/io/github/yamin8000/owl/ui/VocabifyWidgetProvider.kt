/*
 *     Vocabify/freeDictionaryApp.app.main
 *     VocabifyWidgetProvider.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     VocabifyWidgetProvider.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.app.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.app.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.app.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.ui

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import dagger.hilt.android.EntryPointAccessors
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.strings.R as StringsR

/**
 * Home-screen widget (Feature 2): shows today's Word of the Day and a
 * search button. Classic RemoteViews AppWidget — deliberately chosen over
 * Glance to avoid a new Compose-in-RemoteViews dependency (see ADR-004).
 *
 * Tapping the word deep links into the app search with the word prefilled;
 * the search button opens the app search.
 */
internal class VocabifyWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val word = readTodaysWord(context)
        for (appWidgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, buildRemoteViews(context, word))
        }
    }

    companion object {

        private fun readTodaysWord(context: Context): String {
            return try {
                val entryPoint = EntryPointAccessors.fromApplication(
                    context,
                    WotdEntryPoint::class.java
                )
                val settings = entryPoint.settings()
                val today = WotdWorker.todayStamp()
                if (settings.getWotdDate() == today) {
                    settings.getWotdWord() ?: selectTodaysWord()
                } else selectTodaysWord()
            } catch (ignored: Exception) {
                selectTodaysWord()
            }
        }

        /**
         * Refreshes every placed widget with the given word. Called from the
         * daily worker so the widget stays in sync without its own schedule.
         */
        fun updateAll(context: Context, word: String) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(
                ComponentName(context, VocabifyWidgetProvider::class.java)
            )
            if (ids.isEmpty()) return
            val views = buildRemoteViews(context, word)
            manager.updateAppWidget(ids, views)
        }

        internal fun buildRemoteViews(context: Context, word: String): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.widget_vocabify)

            views.setTextViewText(R.id.widget_wotd_word, word)
            views.setTextViewText(
                R.id.widget_wotd_label,
                context.getString(StringsR.string.wotd_widget_label)
            )

            val openApp = PendingIntent.getActivity(
                context,
                REQUEST_SEARCH,
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_search_button, openApp)

            val searchWord = PendingIntent.getActivity(
                context,
                REQUEST_WOTD,
                Intent(context, MainActivity::class.java).apply {
                    putExtra("Search", word)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_wotd_word, searchWord)
            return views
        }
    }
}

private const val REQUEST_SEARCH = 2001
private const val REQUEST_WOTD = 2002
