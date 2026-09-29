package io.github.yamin8000.owl.datastore.domain.usecase.settings

import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class SetWotdWord(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(word: String) {
        repository.setWotdWord(word)
    }
}
