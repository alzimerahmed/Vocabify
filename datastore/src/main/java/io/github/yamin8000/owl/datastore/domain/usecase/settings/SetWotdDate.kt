package io.github.yamin8000.owl.datastore.domain.usecase.settings

import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class SetWotdDate(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(date: String) {
        repository.setWotdDate(date)
    }
}
