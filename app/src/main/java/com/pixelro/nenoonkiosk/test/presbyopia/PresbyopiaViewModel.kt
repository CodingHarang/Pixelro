package com.pixelro.nenoonkiosk.test.presbyopia

import android.app.Application
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.pixelro.nenoonkiosk.R
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

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@HiltViewModel
class  PresbyopiaViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _testState = MutableStateFlow(TestState.Started)
    var testState: StateFlow<TestState> = _testState

    private val _tryCount = MutableStateFlow(0)

    val tryCount: StateFlow<Int> = _tryCount
    private var firstDistance = 0f
    private var secondDistance = 0f
    private var thirdDistance = 0f
    private val _isTextShowing = MutableStateFlow(true)
    val isTextShowing: StateFlow<Boolean> = _isTextShowing
    private var isTTSDescriptionDone = false
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(getApplication()).build()

    fun checkCondition(dist: Float) {
        if (!TTS.tts.isSpeaking) {
            when (_testState.value) {
                TestState.Started -> {
                    isTTSDescriptionDone = false
                    TTS.setOnDoneListener { _testState.update { TestState.AdjustingDistance } }
                    when (_tryCount.value) {
                        0 -> TTS.speechTTS("조절력 검사를 시작하겠습니다.", TextToSpeech.QUEUE_ADD)
                        1 -> TTS.speechTTS("두 번째 검사를 시작하겠습니다.", TextToSpeech.QUEUE_ADD)
                        2 -> TTS.speechTTS("마지막 검사를 시작하겠습니다.", TextToSpeech.QUEUE_ADD)
                    }
                }

                TestState.AdjustingDistance -> {
                    if (!isTTSDescriptionDone) {
                        TTS.setOnDoneListener { isTTSDescriptionDone = true }
                        TTS.speechTTS("화면으로부터 40~60cm 사이로 거리를 조정해주세요.", TextToSpeech.QUEUE_ADD)
                    }
                    if (dist > 400f && isTTSDescriptionDone) {
                        TTS.clearOnDoneListener()
                        _testState.update { TestState.TextBlinking }
                        isTTSDescriptionDone = false
                    }
                }

                TestState.TextBlinking -> {
                    if (!isTTSDescriptionDone) {
                        isTTSDescriptionDone = true
                        TTS.speechTTS("아래의 깜빡이는 숫자를 봐주세요.", TextToSpeech.QUEUE_ADD)
                        viewModelScope.launch {
                            for (i in 1..12) {
                                _isTextShowing.update { !it }
                                delay(250)
                            }
                            _testState.update { TestState.ComingCloser }
                            isTTSDescriptionDone = false
                        }
                    }
                }

                TestState.ComingCloser -> {
                    if (!isTTSDescriptionDone) {
                        TTS.setOnDoneListener { isTTSDescriptionDone = true }
                        TTS.speechTTS("조금씩 앞으로 오다가, 숫자가 흐릿해보이는 지점에서 멈추고, 아래의 다음 버튼을 눌러주세요.", TextToSpeech.QUEUE_ADD)
                    }
                    if (dist < 250f && isTTSDescriptionDone) {
                        TTS.clearOnDoneListener()
                        _testState.update { TestState.NoPresbyopia }
                        isTTSDescriptionDone = false
                    }
                }

                TestState.NoPresbyopia -> {
                    if (!isTTSDescriptionDone) {
                        isTTSDescriptionDone = true
                        TTS.speechTTS(
                            when (_tryCount.value) {
                                0 -> "첫 번째 측정에서 노안이 발견되지 않았습니다\n아래의 '다음'을 눌러주세요."
                                1 -> "두 번째 측정에서 노안이 발견되지 않았습니다\n아래의 '다음'을 눌러주세요."
                                else -> "마지막 측정에서 노안이 발견되지 않았습니다\n아래의 '다음'을 눌러주세요."
                            }, TextToSpeech.QUEUE_ADD
                        )
                    }
                }
            }
        }
    }

    fun moveToNextStep(dist: Float, handleProgress: (Float) -> Unit, toResultScreen: () -> Unit) {
        when (_tryCount.value) {
            0 -> {
                firstDistance = if (_testState.value == TestState.NoPresbyopia) 20f
                else dist / 10
                _tryCount.update { it + 1 }
                handleProgress(0.33f)
                _isTextShowing.update { true }
                _testState.update { TestState.Started }
            }
            1 -> {
                secondDistance = if (_testState.value == TestState.NoPresbyopia) 20f
                else dist / 10

                _tryCount.update { it + 1 }
                handleProgress(0.66f)
                _isTextShowing.update { true }
                _testState.update { TestState.Started }
            }
            else -> {
                viewModelScope.launch {
                    thirdDistance = if (_testState.value == TestState.NoPresbyopia) 20f
                    else dist / 10

                    handleProgress(1.2f)
                    TTS.speechTTS("결과가 나올 때 까지 잠시 기다려주세요.", TextToSpeech.QUEUE_ADD)
                    delay(500)
                    toResultScreen()
                }
            }
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
        _isTextShowing.update { true }
    }

    enum class TestState {
        Started, AdjustingDistance, TextBlinking, ComingCloser, NoPresbyopia
    }

    init {
        _tryCount.update { 0 }
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.volume = 0f
    }
}