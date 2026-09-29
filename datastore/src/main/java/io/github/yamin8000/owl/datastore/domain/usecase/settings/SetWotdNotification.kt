package io.github.yamin8000.owl.datastore.domain.usecase.settings

import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class SetWotdNotification(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(value: Boolean) {
        repository.setIsWotdNotification(value)
    }
}
