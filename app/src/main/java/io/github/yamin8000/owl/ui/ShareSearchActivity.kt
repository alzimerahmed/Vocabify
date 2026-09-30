/*
 *     Vocabify/Vocabify.app.main
 *     ShareSearchActivity.kt Copyrighted by Yamin Siahmargooei at 2024/9/5
 *     ShareSearchActivity.kt Last modified at 2026/2/14
 *     This file is part of Vocabify/Vocabify.app.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
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

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * Lightweight trampoline activity for the share-to-search flow (Feature 3).
 *
 * Receives [Intent.ACTION_SEND] text/plain shares from other apps and forwards
 * the shared text to [MainActivity], where it is pre-filled in the search
 * field and looked up. Finishes immediately so it never appears in Recents.
 */
internal class ShareSearchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedText = handleShareIntent()

        if (!sharedText.isNullOrBlank()) {
            val intent = Intent(this, MainActivity::class.java)
                .putExtra("Search", sharedText)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        finish()
    }

    private fun handleShareIntent(): String? {
        val intent = intent ?: return null
        if (intent.action != Intent.ACTION_SEND || intent.type != "text/plain") return null
        return intent.getStringExtra(Intent.EXTRA_TEXT)?.trim()?.takeIf { it.isNotBlank() }
    }
}
