package com.javierperron.motoassistant.domain.usecase

import com.javierperron.motoassistant.domain.AudioRepository

class ToggleAudioScoUseCase(private val repository: AudioRepository) {

    operator fun invoke(enable: Boolean) {
        if (enable) {
            repository.startBluetoothSco()
        } else {
            repository.stopBluetoothSco()
        }
        repository.playConfirmationBeep()
    }
}