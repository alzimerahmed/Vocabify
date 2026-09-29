package io.github.yamin8000.owl.datastore.domain.usecase.settings

import io.github.yamin8000.owl.datastore.domain.model.IconVariant
import io.github.yamin8000.owl.datastore.domain.repository.SettingsRepository

class SetIconVariant(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(variant: IconVariant) {
        repository.setIconVariant(variant)
    }
}
