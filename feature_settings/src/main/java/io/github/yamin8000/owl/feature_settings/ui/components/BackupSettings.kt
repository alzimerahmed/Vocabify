/*
 *     Vocabify/freeDictionaryApp.feature_settings.main
 *     BackupSettings.kt Copyrighted by Yamin Siahmargooei at 2026/6/25
 *     BackupSettings.kt Last modified at 2026/6/25
 *     This file is part of Vocabify/freeDictionaryApp.feature_settings.main.
 *     Copyright (C) 2026  Yamin Siahmargooei
 *
 *     Vocabify/freeDictionaryApp.feature_settings.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Vocabify/freeDictionaryApp.feature_settings.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_settings.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.AppText
import io.github.yamin8000.owl.common.ui.theme.Sizes
import io.github.yamin8000.owl.feature_settings.ui.BackupStatus
import io.github.yamin8000.owl.strings.R
import java.time.LocalDate

/**
 * Backup/restore card: exports search history + favourites to a JSON
 * file and imports them back, via the Storage Access Framework.
 */
@Composable
internal fun BackupSettings(
    backupStatus: BackupStatus,
    backupJson: String?,
    onExport: () -> Unit,
    onImport: (String) -> Unit,
    onBackupJsonConsumed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var targetUri by remember { mutableStateOf<Uri?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        targetUri = uri
        onExport()
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val json = runCatching {
                context.contentResolver.openInputStream(uri)
                    ?.bufferedReader()
                    ?.use { it.readText() }
            }.getOrNull()
            if (!json.isNullOrBlank()) onImport(json)
        }
    }

    // Write the exported JSON once the ViewModel has produced it.
    LaunchedEffect(backupJson) {
        val uri = targetUri
        if (backupJson != null && uri != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)
                    ?.use { it.write(backupJson.toByteArray()) }
            }
            targetUri = null
            onBackupJsonConsumed()
        }
    }

    SettingsItemCard(
        modifier = modifier,
        title = stringResource(R.string.backup_restore),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    Sizes.Medium,
                    Alignment.CenterHorizontally
                ),
                content = {
                    Button(
                        onClick = { exportLauncher.launch(backupFileName()) },
                        content = {
                            AppText(text = stringResource(R.string.export_data))
                        }
                    )
                    OutlinedButton(
                        onClick = {
                            importLauncher.launch(arrayOf("application/json"))
                        },
                        content = {
                            AppText(text = stringResource(R.string.import_data))
                        }
                    )
                }
            )
            when (backupStatus) {
                BackupStatus.Exported -> AppText(
                    text = stringResource(R.string.backup_export_success),
                    color = MaterialTheme.colorScheme.primary
                )

                is BackupStatus.Imported -> AppText(
                    text = stringResource(R.string.backup_import_success, backupStatus.entryCount),
                    color = MaterialTheme.colorScheme.primary
                )

                BackupStatus.InvalidFile -> AppText(
                    text = stringResource(R.string.backup_invalid_file),
                    color = MaterialTheme.colorScheme.error
                )

                BackupStatus.Idle -> {}
            }
        }
    )
}

private fun backupFileName(): String {
    val date = LocalDate.now()
    return "vocabify-backup-$date.json"
}
