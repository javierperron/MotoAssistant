package com.javierperron.motoassistant.data

import android.content.Context
import com.javierperron.motoassistant.core.BeepPlayer
import com.javierperron.motoassistant.domain.AudioRepository

class AudioRepositoryImpl(
    context: Context,
    private val bluetoothController: BluetoothAudioController = BluetoothAudioController(context),
    private val beepPlayer: BeepPlayer = BeepPlayer()
) : AudioRepository {

    override fun startBluetoothSco() {
        bluetoothController.startBluetoothSco()
    }

    override fun stopBluetoothSco() {
        bluetoothController.stopBluetoothSco()
    }

    override fun playConfirmationBeep() {
        beepPlayer.playBeep()
    }
}