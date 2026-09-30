/*
 *     Vocabify/Vocabify.app.main
 *     App.kt Copyrighted by Yamin Siahmargooei at 2024/8/17
 *     App.kt Last modified at 2026/6/25
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

package io.github.yamin8000.owl.core

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import io.github.yamin8000.owl.datastore.domain.model.IconVariant
import io.github.yamin8000.owl.datastore.domain.usecase.settings.SettingUseCases
import io.github.yamin8000.owl.ui.WotdWorker
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
internal class App : Application() {

    @Inject
    lateinit var settings: SettingUseCases

    override fun onCreate() {
        super.onCreate()

        WotdWorker.schedule(this)
        applyIconVariant()
    }

    /**
     * Feature 11: applies the persisted launcher-icon choice via
     * activity-aliases. Only touches component state when it differs from
     * the current one, so ordinary launches never trigger a launcher refresh.
     */
    private fun applyIconVariant() {
        // The alternate icon is an adaptive icon (anydpi-v26); pre-26 devices
        // keep the default launcher icon.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val variant = try {
            runBlocking { settings.getIconVariant() }
        } catch (ignored: Exception) {
            return
        }
        val main = ComponentName(this, "io.github.yamin8000.owl.ui.MainActivity")
        val monochrome = ComponentName(this, "io.github.yamin8000.owl.ui.MonochromeLauncher")
        val wantMonochrome = variant == IconVariant.Monochrome
        val pm = packageManager
        val monochromeEnabled = pm.getComponentEnabledSetting(monochrome) ==
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        if (monochromeEnabled == wantMonochrome) return
        pm.setComponentEnabledSetting(
            monochrome,
            if (wantMonochrome) PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            main,
            if (wantMonochrome) PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            else PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
