package com.javierperron.motoassistant.data

import android.content.Context
import android.media.AudioManager
import android.util.Log

/**
 * Gestor encargado de enrutar la entrada y salida de audio hacia el
 * intercomunicador/casco mediante el perfil Bluetooth SCO.
 */
class BluetoothAudioController(private val context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    /**
     * Activa la conexión de audio SCO con el intercomunicador Bluetooth.
     */
    fun startBluetoothSco() {
        try {
            if (!audioManager.isBluetoothScoOn) {
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.startBluetoothSco()
                audioManager.isBluetoothScoOn = true
                Log.d("BluetoothAudio", "Canal Bluetooth SCO activado")
            }
        } catch (e: Exception) {
            Log.e("BluetoothAudio", "Error al activar Bluetooth SCO: ${e.message}")
        }
    }

    /**
     * Desconecta el canal SCO y regresa el audio al estado normal del teléfono.
     */
    fun stopBluetoothSco() {
        try {
            if (audioManager.isBluetoothScoOn) {
                audioManager.isBluetoothScoOn = false
                audioManager.stopBluetoothSco()
                audioManager.mode = AudioManager.MODE_NORMAL
                Log.d("BluetoothAudio", "Canal Bluetooth SCO desactivado")
            }
        } catch (e: Exception) {
            Log.e("BluetoothAudio", "Error al desactivar Bluetooth SCO: ${e.message}")
        }
    }
}