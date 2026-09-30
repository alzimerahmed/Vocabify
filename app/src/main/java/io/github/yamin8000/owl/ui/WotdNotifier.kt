/*
 *     Vocabify/Vocabify.app.main
 *     Wotd.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     Wotd.kt Last modified at 2026/6/25
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

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import io.github.yamin8000.owl.R
import io.github.yamin8000.owl.strings.R as StringsR
import io.github.yamin8000.owl.ui.MainActivity

/**
 * Word-of-the-Day notification helper (Feature 1).
 *
 * Posts the daily word notification. Callers must have checked
 * POST_NOTIFICATIONS permission (API 33+) — [notify] is a graceful
 * no-op when notifications are disabled.
 */
internal object WotdNotifier {

    const val CHANNEL_ID = "wotd"
    const val NOTIFICATION_ID = 1001

    fun ensureChannel(context: Context) {
        val channel = NotificationChannelCompat.Builder(
            CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT
        )
            .setName(context.getString(StringsR.string.wotd_notification_channel_name))
            .setDescription(context.getString(StringsR.string.wotd_notification_channel_description))
            .build()
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    /**
     * Shows the Word-of-the-Day notification. Tapping it deep links into
     * the app search with the word pre-filled (the existing "Search"
     * extra handled by [MainActivity]).
     */
    fun notify(context: Context, word: String) {
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) return

        ensureChannel(context)

        val deepLink = Intent(context, MainActivity::class.java).apply {
            putExtra("Search", word)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val deepLinkPendingIntent = PendingIntent.getActivity(
            context,
            word.hashCode(),
            deepLink,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = androidx.core.app.NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_wotd_notification)
            .setContentTitle(context.getString(StringsR.string.wotd_notification_title))
            .setContentText(word)
            .setStyle(
                androidx.core.app.NotificationCompat.BigTextStyle()
                    .bigText(context.getString(StringsR.string.wotd_notification_body, word))
            )
            .setContentIntent(deepLinkPendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            manager.notify(NOTIFICATION_ID, notification)
        } catch (securityException: SecurityException) {
            // Permission revoked between check and notify — graceful no-op.
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}
