package com.javierperron.motoassistant.presentation

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.javierperron.motoassistant.R
import com.javierperron.motoassistant.core.BeepPlayer

class MainActivity : AppCompatActivity() {

    private lateinit var beepPlayer: BeepPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pasamos 'this' como contexto
        beepPlayer = BeepPlayer(this)

        val btnTestBeep = findViewById<Button>(R.id.btnTestBeep)
        btnTestBeep.setOnClickListener {
            // Llamamos al nuevo método playBeep()
            beepPlayer.playBeep()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        beepPlayer.release()
    }
}