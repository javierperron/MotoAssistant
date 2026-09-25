package com.javierperron.motoassistant.core

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.sin

/**
 * Generador del tono de activación en ráfaga continua ("Tu-Di-Tu-Di")
 * sin pausas intermedias y sin dependencias innecesarias.
 */
class BeepPlayer {

    fun playBeep() {
        Thread {
            try {
                val sampleRate = 44100
                val noteDurationMs = 70 // Duración de cada nota individual
                val samplesPerNote = (noteDurationMs * sampleRate) / 1000

                // 4 notas seguidas sin pausas (Tu-Di-Tu-Di)
                val totalSamples = samplesPerNote * 4
                val buffer = ShortArray(totalSamples)

                val freq1 = 659.25 // Mi5
                val freq2 = 880.00 // La5

                var currentIndex = 0

                fun appendNote(frequency: Double) {
                    for (i in 0 until samplesPerNote) {
                        val time = i.toDouble() / sampleRate
                        val envelope = sin(Math.PI * i / samplesPerNote)
                        val angle = 2.0 * Math.PI * frequency * time
                        buffer[currentIndex + i] = (sin(angle) * envelope * Short.MAX_VALUE * 0.7).toInt().toShort()
                    }
                    currentIndex += samplesPerNote
                }

                // Ráfaga pegada de 4 tonos
                appendNote(freq1)
                appendNote(freq2)
                appendNote(freq1)
                appendNote(freq2)

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

                Thread.sleep(320)
                audioTrack.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun release() {
        // Manejado automáticamente por AudioTrack
    }
}