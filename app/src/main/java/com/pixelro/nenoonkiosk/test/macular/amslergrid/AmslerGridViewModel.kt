package com.pixelro.nenoonkiosk.test.macular.amslergrid

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
    private val _isAmslerGridContentVisible = MutableStateFlow(false)
    val isAmslerGridContentVisible: StateFlow<Boolean> = _isAmslerGridContentVisible
    private val _isMacularDegenerationTypeVisible = MutableStateFlow(false)
    val isMacularDegenerationTypeVisible: StateFlow<Boolean> = _isMacularDegenerationTypeVisible
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

    fun updateIsMeasuringDistanceContentVisible(visible: Boolean) {
        _isMeasuringDistanceContentVisible.update { visible }
    }

    fun updateIsAmslerGridContentVisible(visible: Boolean) {
        _isAmslerGridContentVisible.update { visible }
    }

    fun updateIsMacularDegenerationTypeVisible(visible: Boolean) {
        _isMacularDegenerationTypeVisible.update { visible }
    }

    fun updateIsLeftEye(isLeft: Boolean) {
        _isLeftEye.update { isLeft }
    }

    fun updateLeftSelectedArea() {
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
        Log.e("position", "${position.x}, ${position.y}")
        for(i in 0..8) {
            if(position.x in ((i % 3) * 300f)..((i % 3) * 300f + 299f) && position.y in ((i / 3) * 300f)..((i / 3) * 300f + 299f)) {
                if(_currentSelectedArea.value[i] != MacularDisorderType.Normal) {
                    _currentSelectedArea.update {
                        val tmpList = it.toMutableList()
                        tmpList[i] = MacularDisorderType.Normal
                        tmpList.toList()
                    }
                } else {
                    _isMacularDegenerationTypeVisible.update { true }
                }
                return
            }
        }
    }

    fun updateCurrentSelectedArea(idx: Int) {
        val position = _currentSelectedPosition.value
        _currentSelectedArea.update {
            val tmpList = it.toMutableList()
            for(i in 0..8) {
                if(position.x in ((i % 3) * 300f)..((i % 3) * 300f + 299f) && position.y in ((i / 3) * 300f)..((i / 3) * 300f + 299f)) {
                    when(idx) {
                        0 -> { tmpList[i] = MacularDisorderType.Distorted
                        }
                        1 -> { tmpList[i] = MacularDisorderType.Blacked
                        }
                        else -> { tmpList[i] = MacularDisorderType.Whited
                        }
                    }
                }
            }
            tmpList.toList()
        }
        Log.e("selectedArea", "${_currentSelectedArea.value[0]}\n" +
                "${_currentSelectedArea.value[1]}\n" +
                "${_currentSelectedArea.value[2]}\n" +
                "${_currentSelectedArea.value[3]}\n" +
                "${_currentSelectedArea.value[4]}\n" +
                "${_currentSelectedArea.value[5]}\n" +
                "${_currentSelectedArea.value[6]}\n" +
                "${_currentSelectedArea.value[7]}\n" +
                "${_currentSelectedArea.value[8]}\n"
        )
    }

    fun init() {
        _isMeasuringDistanceContentVisible.update { true }
        _isAmslerGridContentVisible.update { false }
        _isMacularDegenerationTypeVisible.update { false }
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