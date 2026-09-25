package com.javierperron.motoassistant.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.javierperron.motoassistant.R
import com.javierperron.motoassistant.domain.usecase.ToggleAudioScoUseCase

class AudioForegroundService : Service() {

    private lateinit var toggleAudioScoUseCase: ToggleAudioScoUseCase
    private val CHANNEL_ID = "MotoAssistantAudioChannel"
    private val NOTIFICATION_ID = 1001

    override fun onCreate() {
        super.onCreate()
        val repository = AudioRepositoryImpl(this)
        toggleAudioScoUseCase = ToggleAudioScoUseCase(repository)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = createNotification()

        // Asignación explícita del tipo de servicio para Android 14+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        toggleAudioScoUseCase(enable = true)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        toggleAudioScoUseCase(enable = false)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Canal de Audio MotoAssistant",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Mantiene activo el micrófono del intercomunicador Bluetooth"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MotoAssistant Activo")
            .setContentText("Escuchando el intercomunicador...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}