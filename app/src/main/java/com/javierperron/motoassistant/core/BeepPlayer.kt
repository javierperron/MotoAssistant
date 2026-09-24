package com.javierperron.motoassistant.core

import android.media.AudioManager
import android.media.ToneGenerator

/**
 * Utilidad encargada de emitir tonos de confirmación auditiva (Bip)
 * para interactuar con la app sin necesidad de mirar la pantalla.
 */
class BeepPlayer {

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            // Usa el canal de audio del sistema a un volumen del 80%
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Reproduce un "Bip" corto para indicar inicio de escucha o confirmación.
     */
    fun playBeep() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 150) // 150 ms de duración
    }

    /**
     * Libera los recursos del sistema cuando ya no se utilice.
     */
    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}