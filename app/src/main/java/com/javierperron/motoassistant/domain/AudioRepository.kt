package com.javierperron.motoassistant.domain

interface AudioRepository {
    fun startBluetoothSco()
    fun stopBluetoothSco()
    fun playConfirmationBeep()
}