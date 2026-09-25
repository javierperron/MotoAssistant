package com.javierperron.motoassistant.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.javierperron.motoassistant.R
import com.javierperron.motoassistant.data.AudioForegroundService

class MainActivity : AppCompatActivity() {

    private var isServiceRunning = false
    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTestBeep = findViewById<Button>(R.id.btnTestBeep)

        btnTestBeep.setOnClickListener {
            if (checkAndRequestPermissions()) {
                toggleService()
            }
        }
    }

    private fun toggleService() {
        val serviceIntent = Intent(this, AudioForegroundService::class.java)

        if (!isServiceRunning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
            isServiceRunning = true
            Toast.makeText(this, "Servicio de Audio Iniciado", Toast.LENGTH_SHORT).show()
        } else {
            stopService(serviceIntent)
            isServiceRunning = false
            Toast.makeText(this, "Servicio de Audio Detenido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val listPermissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                toggleService()
            } else {
                Toast.makeText(this, "Se requieren permisos de micrófono para continuar", Toast.LENGTH_LONG).show()
            }
        }
    }
}