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

@file:SuppressLint("StartActivityAndCollapseDeprecated") // Intent overload is the only option below API 34

package io.github.yamin8000.owl.ui

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.annotation.SuppressLint
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import io.github.yamin8000.owl.common.util.log

/**
 * Quick Settings tile that launches the overlay (bubble) search (Feature 12).
 *
 * Uses [TileService.startActivityAndCollapse] — the tile context is exempt from
 * background-activity-start restrictions when the panel is collapsed this way.
 * The PendingIntent overload exists only on API 34+; the Intent overload covers 24-33.
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
            if (Build.VERSION.SDK_INT >= 34) {
                startActivityAndCollapse(pendingIntent)
            } else {
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
