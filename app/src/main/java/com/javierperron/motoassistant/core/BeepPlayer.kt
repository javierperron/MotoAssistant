package com.javierperron.motoassistant.core

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.sin

/**
 * Generador del tono de activación tipo app de reparto (DiDi/Uber).
 * Produce un doble tono ascendente suave ("Tu-Di") mediante síntesis de audio.
 */
class BeepPlayer(private val context: Context) {

    /**
     * Reproduce el tono de activación ascendente.
     */
    fun playBeep() {
        Thread {
            try {
                val sampleRate = 44100
                // Duración de cada una de las dos notas (en milisegundos)
                val noteDurationMs = 90
                val samplesPerNote = (noteDurationMs * sampleRate) / 1000
                val totalSamples = samplesPerNote * 2
                val buffer = ShortArray(totalSamples)

                // Frecuencia Nota 1: ~659 Hz (Mi5)
                // Frecuencia Nota 2: ~880 Hz (La5 - Tono ascendente)
                val freq1 = 659.25
                val freq2 = 880.00

                // Generar primera nota ("Tu")
                for (i in 0 until samplesPerNote) {
                    val time = i.toDouble() / sampleRate
                    val envelope = sin(Math.PI * i / samplesPerNote) // Suaviza inicio y fin
                    val angle = 2.0 * Math.PI * freq1 * time
                    buffer[i] = (sin(angle) * envelope * Short.MAX_VALUE * 0.7).toInt().toShort()
                }

                // Generar segunda nota ("Di")
                for (i in 0 until samplesPerNote) {
                    val index = samplesPerNote + i
                    val time = i.toDouble() / sampleRate
                    val envelope = sin(Math.PI * i / samplesPerNote)
                    val angle = 2.0 * Math.PI * freq2 * time
                    buffer[index] = (sin(angle) * envelope * Short.MAX_VALUE * 0.75).toInt().toShort()
                }

                // Configurar canal de salida multimedia
                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()

                // Liberación de recursos al terminar
                Thread.sleep((noteDurationMs * 2).toLong() + 50)
                audioTrack.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun release() {
        // La memoria se libera automáticamente en el hilo secundario
    }
}