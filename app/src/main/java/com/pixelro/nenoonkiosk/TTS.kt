package com.pixelro.nenoonkiosk

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaViewModel

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
    fun setOnDoneListener(onDone: () -> Unit) {
        tts.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    onDone()
                }
            }
        )
    }

    fun clearOnDoneListener() {
        tts.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {}
            }
        )
    }
}