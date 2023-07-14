package com.pixelro.nenoonkiosk.test.macular.amslergrid

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pixelro.nenoonkiosk.TTS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmslerGridViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _isMeasuringDistanceContentVisible = MutableStateFlow(true)
    val isMeasuringDistanceContentVisible: StateFlow<Boolean> = _isMeasuringDistanceContentVisible
    private val _isAmslerGridContentVisible = MutableStateFlow(false)
    val isAmslerGridContentVisible: StateFlow<Boolean> = _isAmslerGridContentVisible
    private val _isLeftEye = MutableStateFlow(true)
    val isLeftEye: StateFlow<Boolean> = _isLeftEye
    private val _currentSelectedPosition = MutableStateFlow(Offset(0f, 0f))
    val currentSelectedPosition: StateFlow<Offset> = _currentSelectedPosition
    private val _currentSelectedArea = MutableStateFlow(listOf(
        MacularDisorderType.Normal,
        MacularDisorderType.Normal,
        MacularDisorderType.Normal,
        MacularDisorderType.Normal,
        MacularDisorderType.Normal,
        MacularDisorderType.Normal,
        MacularDisorderType.Normal,
        MacularDisorderType.Normal,
        MacularDisorderType.Normal
    ))
    val currentSelectedArea: StateFlow<List<MacularDisorderType>> = _currentSelectedArea
    private val _leftSelectedArea = MutableStateFlow(listOf<MacularDisorderType>())
    val leftSelectedArea: StateFlow<List<MacularDisorderType>> = _leftSelectedArea
    private val _rightSelectedArea = MutableStateFlow(listOf<MacularDisorderType>())
    val rightSelectedArea: StateFlow<List<MacularDisorderType>> = _rightSelectedArea
    private val _isBlinkingDone = MutableStateFlow(false)
    val isBlinkingDone: StateFlow<Boolean> = _isBlinkingDone
    private val _isDotShowing = MutableStateFlow(true)
    val isDotShowing: StateFlow<Boolean> = _isDotShowing
    private val _isFaceCenter = MutableStateFlow(false)
    val isFaceCenter: StateFlow<Boolean> = _isFaceCenter

    //TTS 전용 변수
    private val _isLookAtTheDotTTSDone = MutableStateFlow(true)
    val isLookAtTheDotTTSDone: StateFlow<Boolean> = _isLookAtTheDotTTSDone
    private val _isSelectTTSDone = MutableStateFlow(true)
    val isSelectTTSDone: StateFlow<Boolean> = _isSelectTTSDone

    fun updateIsLookAtTheDotTTSDone(isDone: Boolean) {
        _isLookAtTheDotTTSDone.update { isDone }
    }

    fun updateIsSelectTTSDone(isDone: Boolean) {
        _isSelectTTSDone.update { isDone }
    }

    fun updateIsBlinkingDone(isDone: Boolean) {
        _isBlinkingDone.update { isDone }
    }

    fun updateIsDotShowing(isShowing: Boolean) {
        _isDotShowing.update { isShowing }
    }

    fun updateIsFaceCenter(isCenter: Boolean) {
        _isFaceCenter.update { isCenter }
    }

    fun updateIsMeasuringDistanceContentVisible(visible: Boolean) {
        _isMeasuringDistanceContentVisible.update { visible }
    }

    fun updateIsAmslerGridContentVisible(visible: Boolean) {
        _isAmslerGridContentVisible.update { visible }
    }


    fun updateIsLeftEye(isLeft: Boolean) {
        _isLeftEye.update { isLeft }
    }

    fun updateLeftSelectedArea() {
        TTS.speechTTS("오른쪽 눈 검사를 시작하겠습니다.", TextToSpeech.QUEUE_ADD)
        viewModelScope.launch {
            delay(450)
            _isBlinkingDone.update { false }
            _isDotShowing.update { true }
            _isFaceCenter.update { false }
        }
        _leftSelectedArea.update { currentSelectedArea.value }
        _currentSelectedArea.update {
            val tmpList = it.toMutableList()
            for(i in 0..8) {
                tmpList[i] = MacularDisorderType.Normal
            }
            tmpList.toList()
        }
    }

    fun updateRightSelectedArea() {
        _rightSelectedArea.update { currentSelectedArea.value }
    }

    fun getAmslerGridTestResult(): AmslerGridTestResult {
        return AmslerGridTestResult(_leftSelectedArea.value, _rightSelectedArea.value)
    }

    fun updateCurrentSelectedPosition(position: Offset) {
        _currentSelectedPosition.update { position }
//        Log.e("position", "${position.x}, ${position.y}")
        for(i in 0..8) {
            if(position.x in ((i % 3) * 300f)..((i % 3) * 300f + 299f) && position.y in ((i / 3) * 300f)..((i / 3) * 300f + 299f)) {
                if(_currentSelectedArea.value[i] != MacularDisorderType.Normal) {
                    _currentSelectedArea.update {
                        val tmpList = it.toMutableList()
                        tmpList[i] = MacularDisorderType.Normal
                        tmpList.toList()
                    }
                } else {
                    updateCurrentSelectedArea()
                }
                return
            }
        }
    }

    private fun updateCurrentSelectedArea() {
        val position = _currentSelectedPosition.value
        _currentSelectedArea.update {
            val tmpList = it.toMutableList()
            for(i in 0..8) {
                if(position.x in ((i % 3) * 300f)..((i % 3) * 300f + 299f) && position.y in ((i / 3) * 300f)..((i / 3) * 300f + 299f)) {
                    tmpList[i] = MacularDisorderType.Distorted
                }
            }
            tmpList.toList()
        }
//        Log.e("selectedArea", "${_currentSelectedArea.value[0]}\n" +
//                "${_currentSelectedArea.value[1]}\n" +
//                "${_currentSelectedArea.value[2]}\n" +
//                "${_currentSelectedArea.value[3]}\n" +
//                "${_currentSelectedArea.value[4]}\n" +
//                "${_currentSelectedArea.value[5]}\n" +
//                "${_currentSelectedArea.value[6]}\n" +
//                "${_currentSelectedArea.value[7]}\n" +
//                "${_currentSelectedArea.value[8]}\n"
//        )
    }

    fun startBlinking() {
        var count = 16
        viewModelScope.launch {
            while(count > 0) {
                _isDotShowing.update { !it }
                delay(250)
                count--
            }
            _isBlinkingDone.update { true }
        }
    }

    fun init() {
        _isBlinkingDone.update { false }
        _isDotShowing.update { true }
        _isFaceCenter.update { false }
        _isMeasuringDistanceContentVisible.update { true }
        _isAmslerGridContentVisible.update { false }
//        _isMacularDegenerationTypeVisible.update { false }
        _isLeftEye.update { true }
        _currentSelectedArea.update {
            listOf(
                MacularDisorderType.Normal,
                MacularDisorderType.Normal,
                MacularDisorderType.Normal,
                MacularDisorderType.Normal,
                MacularDisorderType.Normal,
                MacularDisorderType.Normal,
                MacularDisorderType.Normal,
                MacularDisorderType.Normal,
                MacularDisorderType.Normal
            )
        }
    }
}