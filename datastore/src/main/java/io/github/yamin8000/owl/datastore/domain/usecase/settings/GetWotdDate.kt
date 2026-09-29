package io.github.yamin8000.owl.datastore.domain.usecase.settings

import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class GetWotdDate(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getWotdDate()
    }
}
