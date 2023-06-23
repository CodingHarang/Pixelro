package com.pixelro.nenoonkiosk.amslergrid

import android.app.Application
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AmslerGridViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val _isMeasuringDistanceContentVisible = MutableStateFlow(true)
    val isMeasuringDistanceContentVisible: StateFlow<Boolean> = _isMeasuringDistanceContentVisible
    private val _isCoveredEyeCheckingContentVisible = MutableStateFlow(false)
    val isCoveredEyeCheckingContentVisible: StateFlow<Boolean> = _isCoveredEyeCheckingContentVisible
    private val _isAmslerGridContentVisible = MutableStateFlow(false)
    val isAmslerGridContentVisible: StateFlow<Boolean> = _isAmslerGridContentVisible
    private val _isMacularDegenerationTypeVisible = MutableStateFlow(false)
    val isMacularDegenerationTypeVisible: StateFlow<Boolean> = _isMacularDegenerationTypeVisible
    private val _isLeftEye = MutableStateFlow(true)
    val isLeftEye: StateFlow<Boolean> = _isLeftEye
    private val _currentSelectedPosition = MutableStateFlow(Offset(0f, 0f))
    val currentSelectedPosition: StateFlow<Offset> = _currentSelectedPosition
    private val _currentSelectedArea = MutableStateFlow(mutableListOf(MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal))
    val currentSelectedArea: StateFlow<List<MacularDisorderType>> = _currentSelectedArea
    private val _leftSelectedArea = MutableStateFlow(emptyList<MacularDisorderType>())
    val leftSelectedArea: StateFlow<List<MacularDisorderType>> = _leftSelectedArea
    private val _rightSelectedArea = MutableStateFlow(emptyList<MacularDisorderType>())
    val rightSelectedArea: StateFlow<List<MacularDisorderType>> = _rightSelectedArea


    fun toCoveredEyeCheckingContent() {
        _isMeasuringDistanceContentVisible.update { false }
        _isCoveredEyeCheckingContentVisible.update { true }
    }

    fun toAmslerGridContent() {
        _isCoveredEyeCheckingContentVisible.update { false }
        _isAmslerGridContentVisible.update { true }
        _isMacularDegenerationTypeVisible.update { false }
    }

    fun updateIsLeftEye(isLeft: Boolean) {
        _isLeftEye.update { isLeft }
    }

    fun updateLeftSelectedArea() {
        _leftSelectedArea.update { currentSelectedArea.value }
        _currentSelectedArea.update {
            for(i in 0..8) {
                it[i] = MacularDisorderType.Normal
            }
            it
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
        if(position.x in 0f..299f && position.y in 0f..299f) {
            if(_currentSelectedArea.value[0] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[0] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else if(position.x in 300f..599f && position.y in 0f..299f) {
            if(_currentSelectedArea.value[1] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[1] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else if(position.x in 600f..900f && position.y in 0f..299f) {
            if(_currentSelectedArea.value[2] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[2] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else if(position.x in 0f..299f && position.y in 300f..599f) {
            if(_currentSelectedArea.value[3] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[3] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else if(position.x in 300f..599f && position.y in 300f..599f) {
            if(_currentSelectedArea.value[4] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[4] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else if(position.x in 600f..900f && position.y in 300f..599f) {
            if(_currentSelectedArea.value[5] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[5] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else if(position.x in 0f..299f && position.y in 600f..900f) {
            if(_currentSelectedArea.value[6] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[6] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else if(position.x in 300f..599f && position.y in 600f..900f) {
            if(_currentSelectedArea.value[7] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[7] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        } else {
            if(_currentSelectedArea.value[8] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[8] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                _isMacularDegenerationTypeVisible.update { true }
            }
        }
    }

    fun updateCurrentSelectedArea(idx: Int) {
        val position = _currentSelectedPosition.value

        Log.e("clicked", "${position.x}, ${position.y}")
        _currentSelectedArea.update {
            val tmpList = it.toMutableList()
            if(position.x in 0f..299f && position.y in 0f..299f) {
                when(idx) {
                    0 -> { tmpList[0] = MacularDisorderType.Distorted }
                    1 -> { tmpList[0] = MacularDisorderType.Blacked }
                    else -> { tmpList[0] = MacularDisorderType.Whited }
                }
            } else if(position.x in 300f..599f && position.y in 0f..299f) {
                when(idx) {
                    0 -> { tmpList[1] = MacularDisorderType.Distorted }
                    1 -> { tmpList[1] = MacularDisorderType.Blacked }
                    else -> { tmpList[1] = MacularDisorderType.Whited }
                }
            } else if(position.x in 600f..900f && position.y in 0f..299f) {
                when(idx) {
                    0 -> { tmpList[2] = MacularDisorderType.Distorted }
                    1 -> { tmpList[2] = MacularDisorderType.Blacked }
                    else -> { tmpList[2] = MacularDisorderType.Whited }
                }
            } else if(position.x in 0f..299f && position.y in 300f..599f) {
                when(idx) {
                    0 -> { tmpList[3] = MacularDisorderType.Distorted }
                    1 -> { tmpList[3] = MacularDisorderType.Blacked }
                    else -> { tmpList[3] = MacularDisorderType.Whited }
                }
            } else if(position.x in 300f..599f && position.y in 300f..599f) {
                when(idx) {
                    0 -> { tmpList[4] = MacularDisorderType.Distorted }
                    1 -> { tmpList[4] = MacularDisorderType.Blacked }
                    else -> { tmpList[4] = MacularDisorderType.Whited }
                }
            } else if(position.x in 600f..900f && position.y in 300f..599f) {
                when(idx) {
                    0 -> { tmpList[5] = MacularDisorderType.Distorted }
                    1 -> { tmpList[5] = MacularDisorderType.Blacked }
                    else -> { tmpList[5] = MacularDisorderType.Whited }
                }
            } else if(position.x in 0f..299f && position.y in 600f..900f) {
                when(idx) {
                    0 -> { tmpList[6] = MacularDisorderType.Distorted }
                    1 -> { tmpList[6] = MacularDisorderType.Blacked }
                    else -> { tmpList[6] = MacularDisorderType.Whited }
                }
            } else if(position.x in 300f..599f && position.y in 600f..900f) {
                when(idx) {
                    0 -> { tmpList[7] = MacularDisorderType.Distorted }
                    1 -> { tmpList[7] = MacularDisorderType.Blacked }
                    else -> { tmpList[7] = MacularDisorderType.Whited }
                }
            } else {
                when(idx) {
                    0 -> { tmpList[8] = MacularDisorderType.Distorted }
                    1 -> { tmpList[8] = MacularDisorderType.Blacked }
                    else -> { tmpList[8] = MacularDisorderType.Whited }
                }
            }
            tmpList
        }
    }
}