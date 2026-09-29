/*
 *     Vocabify/freeDictionaryApp.app.main
 *     OverlaySearchTileService.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     OverlaySearchTileService.kt Last modified at 2026/2/14
 *     This file is part of Vocabify/freeDictionaryApp.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import io.github.yamin8000.owl.common.util.log

/**
 * Quick Settings tile that launches the overlay (bubble) search (Feature 12).
 *
 * Background activity launch restrictions on API 29+ are handled by starting
 * the overlay through a [PendingIntent]; on API 34+ the pending intent is sent
 * with [ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED] so the launch
 * is permitted from a tile while the device is unlocked.
 */
internal class OverlaySearchTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()
        if (isLocked) {
            unlockAndRun { launchOverlay() }
        } else {
            launchOverlay()
        }
    }

    private fun launchOverlay() {
        val intent = Intent(this, OverlayActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        try {
            when {
                Build.VERSION.SDK_INT >= 34 -> {
                    val options = ActivityOptions.makeBasic()
                    options.setPendingIntentBackgroundActivityStartMode(
                        ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                    )
                    pendingIntent.send(
                        this,
                        0,
                        null,
                        null,
                        null,
                        null,
                        options.toBundle()
                    )
                }

                Build.VERSION.SDK_INT >= 29 ->
                    @Suppress("DEPRECATION")
                    startActivityAndCollapse(pendingIntent)

                else ->
                    @Suppress("DEPRECATION")
                    startActivityAndCollapse(intent)
            }
        } catch (e: PendingIntent.CanceledException) {
            log(e.stackTraceToString())
        }
        updateTile()
    }

    private fun updateTile() {
        val tile = qsTile ?: return
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }
}
