package com.pixelro.nenoonkiosk.test.presbyopia

import android.app.Application
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixelro.nenoonkiosk.TTS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PresbyopiaViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _isMovedTo40cm = MutableStateFlow(false)
    val isMovedTo40cm: StateFlow<Boolean> = _isMovedTo40cm
    private val _isUnder25cm = MutableStateFlow(false)
    val isUnder25cm: StateFlow<Boolean> = _isUnder25cm
    private val _isNumberShowing = MutableStateFlow(true)
    val isNumberShowing: StateFlow<Boolean> = _isNumberShowing
    private val _isBlinkingDone = MutableStateFlow(false)
    val isBlinkingDone: StateFlow<Boolean> = _isBlinkingDone
    private val _tryCount = MutableStateFlow(0)
    val tryCount: StateFlow<Int> = _tryCount
    private var firstDistance = 0f
    private var secondDistance = 0f
    private var thirdDistance = 0f
    private lateinit var tts: TextToSpeech

    fun speechTTS(string: String) {
        tts = TextToSpeech(getApplication()) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.KOREAN
            }
            tts.stop()
            tts.language = Locale.KOREAN
            tts.setSpeechRate(1.0f)
            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null, null)
            tts.shutdown()
        }
    }

    fun updateIsMovedTo40cm(isMoved: Boolean) {
        _isMovedTo40cm.update { isMoved }
    }

    fun updateIsUnder25cm(isUnder: Boolean) {
        _isUnder25cm.update { isUnder }
    }

    fun moveToNextStep(dist: Float, handleProgress: (Float) -> Unit, toResultScreen: () -> Unit) {
        when (_tryCount.value) {
            0 -> {
                firstDistance = if (_isUnder25cm.value) 20f
                else dist / 10

                _tryCount.update { it + 1 }
                handleProgress(0.33f)
                _isMovedTo40cm.update { false }
                _isUnder25cm.update { false }
                _isNumberShowing.update { true }
                _isBlinkingDone.update { false }
                viewModelScope.launch {
                    TTS.speechTTS("두번째 측정을 시작하겠습니다. 화면으로부터 40~60cm 사이로 거리를 조정해주세요.", TextToSpeech.QUEUE_ADD)
                }
            }
            1 -> {
                secondDistance = if (_isUnder25cm.value) 20f
                else dist / 10

                _tryCount.update { it + 1 }
                handleProgress(0.66f)
                _isMovedTo40cm.update { false }
                _isUnder25cm.update { false }
                _isNumberShowing.update { true }
                _isBlinkingDone.update { false }
                viewModelScope.launch {
                    TTS.speechTTS("세번째 측정을 시작하겠습니다. 화면으로부터 40~60cm 사이로 거리를 조정해주세요.", TextToSpeech.QUEUE_ADD)
                }
            }
            else -> {
                viewModelScope.launch {
                    thirdDistance = if (_isUnder25cm.value) 20f
                    else dist / 10

                    handleProgress(1.2f)
                    TTS.speechTTS("결과가 나올 때 까지 잠시 기다려주세요.", TextToSpeech.QUEUE_ADD)
                    delay(500)
                    toResultScreen()
                }
            }
        }
    }

    fun blinkNumber() {
        viewModelScope.launch {
            var count = 12
            while (TTS.tts.isSpeaking) {
                delay(100)
            }
            TTS.speechTTS("아래의 깜빡이는 숫자를 봐주세요.", TextToSpeech.QUEUE_ADD)
            while (count > 0) {
                _isNumberShowing.update { !it }
                delay(250)
                count--
            }
            _isBlinkingDone.update { true }
            TTS.speechTTS("조금씩 앞으로 오다가 숫자가 흐릿해지는 지점에서 멈추고, 아래의 다음 버튼을 눌러주세요", TextToSpeech.QUEUE_ADD)
        }
    }

    fun getPresbyopiaTestResult(): PresbyopiaTestResult {
        var max = 2147483647f
        val avgDistance = (firstDistance + secondDistance + thirdDistance) / 3
        var age = 25
        for (entry in AccommodationData.allEntries) {
            var diff = avgDistance - entry.x
            if (diff < 0) diff = -diff
            if (diff < max) {
                max = avgDistance - entry.x
                age = entry.y.toInt()
            }
        }
        Log.e("presbyopiaResult", "firstDistance: ${firstDistance}\nsecondDistance: ${secondDistance}\nthirdDistance: ${thirdDistance}\n${avgDistance}\nage: $age")
        return PresbyopiaTestResult(firstDistance, secondDistance, thirdDistance, avgDistance, age)
    }

    fun init() {
        _isMovedTo40cm.update { false }
        _isUnder25cm.update { false }
        _isNumberShowing.update { true }
        _isBlinkingDone.update { false }
        _tryCount.update { 0 }
    }
}