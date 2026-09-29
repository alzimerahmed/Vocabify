package io.github.yamin8000.owl.datastore.domain.usecase.settings

import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class SetDynamicColor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(value: Boolean) {
        repository.setIsDynamicColor(value)
    }
}
