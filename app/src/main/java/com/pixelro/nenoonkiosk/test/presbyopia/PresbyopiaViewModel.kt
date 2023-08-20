package com.pixelro.nenoonkiosk.test.presbyopia

import android.app.Application
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.pixelro.nenoonkiosk.TTS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Thread.State
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PresbyopiaViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private var testState = TestState.Started

    private val _tryCount = MutableStateFlow(0)

    val tryCount: StateFlow<Int> = _tryCount
    private var firstDistance = 0f
    private var secondDistance = 0f
    private var thirdDistance = 0f
    private val _isNumberShowing = MutableStateFlow(true)
    val isNumberShowing: StateFlow<Boolean> = _isNumberShowing
    private val _isTTSDescriptionDone = MutableStateFlow(false)
    val isTTSDescriptionDone: StateFlow<Boolean> = _isTTSDescriptionDone
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(getApplication()).build()

    fun updateIsTTSDescriptionDone(isDone: Boolean) {
        _isTTSDescriptionDone.update { isDone }
    }

    fun toNextState(dist: Float) {
        if (!TTS.tts.isSpeaking) {
            when (testState) {
                TestState.Started -> {
                    testState = TestState.AdjustingDistance
                    if (_tryCount.value == 0) {
                        TTS.speechTTS("조절력 검사를 시작하겠습니다. 화면으로부터 40~60cm 사이로 거리를 조정해주세요.", TextToSpeech.QUEUE_ADD)
                    } else {
                        TTS.speechTTS("화면으로부터 40~60cm 사이로 거리를 조정해주세요.", TextToSpeech.QUEUE_ADD)
                    }
                }
                TestState.AdjustingDistance -> {
                    if (dist > 400f) {
                        testState = TestState.TextBlinking
                        var count = 12
                        TTS.speechTTS("아래의 깜빡이는 숫자를 봐주세요.", TextToSpeech.QUEUE_ADD)
                        while (count > 0) {
                            _isNumberShowing.update { !it }
                            delay(250)
                            count--
                        }
//                        TTS.tts.setOnUtteranceProgressListener(
//                            object : UtteranceProgressListener() {
//                                override fun onStart(utteranceId: String?) {}
//                                override fun onError(utteranceId: String?) {}
//                                override fun onDone(utteranceId: String?) {
//                                }
//                            }
//                        )
                    }
                }
                TestState.TextBlinking -> {

                }
                TestState.ComingCloser -> {

                }
                TestState.Ended -> {

                }
            }
        }
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
                    TTS.speechTTS("마지막 측정을 시작하겠습니다. 화면으로부터 40~60cm 사이로 거리를 조정해주세요.", TextToSpeech.QUEUE_ADD)
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

            TTS.speechTTS("조금씩 앞으로 오다가 숫자가 흐릿해보이는 지점에서 멈추고, 아래의 다음 버튼을 눌러주세요", TextToSpeech.QUEUE_ADD)
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
        _isNumberShowing.update { true }
    }

    enum class TestState {
        Started, AdjustingDistance, TextBlinking, ComingCloser, Ended
    }

    init {
        _tryCount.update { 0 }
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.volume = 0f
    }
}