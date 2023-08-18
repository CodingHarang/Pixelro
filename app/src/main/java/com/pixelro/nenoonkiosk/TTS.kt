package com.pixelro.nenoonkiosk

import android.speech.tts.TextToSpeech

object TTS {
    lateinit var tts: TextToSpeech
    fun speechTTS(string: String, queueType: Int) {
        tts.speak(string, queueType, null, "main")
    }
    fun initTTS() {
        this.tts = TextToSpeech(NenoonKioskApplication.applicationContext()) {}
    }
    fun destroyTTS() {
        tts.shutdown()
    }
}