package com.pixelro.nenoonkiosk.test.visualacuity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisualAcuityViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _isMeasuringDistanceContentVisible = MutableStateFlow(true)
    val isMeasuringDistanceContentVisible: StateFlow<Boolean> = _isMeasuringDistanceContentVisible
    private val _isCoveredEyeCheckingContentVisible = MutableStateFlow(false)
    val isCoveredEyeCheckingContentVisible: StateFlow<Boolean> = _isCoveredEyeCheckingContentVisible
    private val _isVisualAcuityContentVisible = MutableStateFlow(false)
    val isVisualAcuityContentVisible: StateFlow<Boolean> = _isVisualAcuityContentVisible
    private val _sightLevel = MutableStateFlow(1)
    val sightLevel: StateFlow<Int> = _sightLevel
    private val _isLeftEye = MutableStateFlow(true)
    val isLeftEye: StateFlow<Boolean> = _isLeftEye
    private var leftEyeSightValue = 1
    private var rightEyeSightValue = 1
    private var _randomList = MutableStateFlow(mutableListOf(0))
    val randomList: StateFlow<MutableList<Int>> = _randomList
    private var _ansNum = MutableStateFlow(0)
    val ansNum: StateFlow<Int> = _ansNum

    fun updateIsMeasuringDistanceContentVisible(visible: Boolean) {
        _isMeasuringDistanceContentVisible.update { visible }
    }

    fun updateIsVisualAcuityContentVisible(visible: Boolean) {
        _isVisualAcuityContentVisible.update { visible }
    }

    private var sightHistory = mutableMapOf(
        1 to Pair(0, 0),
        2 to Pair(0, 0),
        3 to Pair(0, 0),
        4 to Pair(0, 0),
        5 to Pair(0, 0),
        6 to Pair(0, 0),
        7 to Pair(0, 0),
        8 to Pair(0, 0),
        9 to Pair(0, 0),
        10 to Pair(0, 0)
    )

    fun processAnswerSelected(
        idx: Int,
        toResultScreen: () -> Unit
    ) {
        var isEnd = false
        // choose number
        if (idx != 3) {
            // if correct
            if (ansNum.value == _randomList.value[idx]) {
                sightHistory[_sightLevel.value] = Pair(
                    sightHistory[_sightLevel.value]!!.first + 1,
                    sightHistory[_sightLevel.value]!!.second
                )
                // if first trial
                if (sightHistory[_sightLevel.value]!!.first == 1 && sightHistory[_sightLevel.value]!!.second == 0) {
                    // if level == 10
                    if (_sightLevel.value == 10) {
                        isEnd = true
                        moveToNextStep(toResultScreen)
                    } else {
                        _sightLevel.update { it + 1 }
//                        if(_sightLevel.value > 10) moveToRightVisualAcuityTest(toResultScreen)
                    }
                }
            } // if wrong
            else {
                sightHistory[_sightLevel.value] = Pair(
                    sightHistory[_sightLevel.value]!!.first,
                    sightHistory[_sightLevel.value]!!.second + 1
                )
            }
        } // choose question mark
        else {
            sightHistory[_sightLevel.value] = Pair(
                sightHistory[_sightLevel.value]!!.first,
                sightHistory[_sightLevel.value]!!.second + 1
            )
        }

        // if 3th trial
        if (sightHistory[_sightLevel.value]!!.first + sightHistory[_sightLevel.value]!!.second >= 3) {
            // if correct >= 2
            if (sightHistory[_sightLevel.value]!!.first >= 2) {
                // if next level trial >= 3
                if (sightHistory[_sightLevel.value + 1]!!.first + sightHistory[_sightLevel.value + 1]!!.second >= 3) {
                    isEnd = true
                    moveToNextStep(toResultScreen)
                } // to next level
                else {
                    _sightLevel.update { it + 1 }
                }
            } // if correct < 1
            else {
                // if level == 1
                if (_sightLevel.value == 1) {
                    isEnd = true
                    moveToNextStep(toResultScreen)
                } // level--
                else {
                    // if prev level trial >= 3
                    if (sightHistory[_sightLevel.value - 1]!!.first + sightHistory[_sightLevel.value - 1]!!.second >= 3) {
                        isEnd = true
                        moveToNextStep(toResultScreen)
                    } else {
                        _sightLevel.update { it - 1 }
                    }
                }
            }
        }
        if (!isEnd) updateRandomList()
//        Log.e("", "end: ${_sightLevel.value}")
        Log.e(
            "visualAcuityTest",
            "${sightHistory[1]}\n${sightHistory[2]}\n${sightHistory[3]}\n${sightHistory[4]}\n${sightHistory[5]}\n${sightHistory[6]}\n${sightHistory[7]}\n${sightHistory[8]}\n${sightHistory[9]}\n${sightHistory[10]}"
        )
    }

    fun getVisualAcuityTestResult(): VisualAcuityTestResult {
        return VisualAcuityTestResult(
            leftEyeSightValue,
            rightEyeSightValue,
        )
    }

    private fun moveToNextStep(
        toResultScreen: () -> Unit
    ) {
        if (_isLeftEye.value) {
            sightHistory = mutableMapOf(
                1 to Pair(0, 0),
                2 to Pair(0, 0),
                3 to Pair(0, 0),
                4 to Pair(0, 0),
                5 to Pair(0, 0),
                6 to Pair(0, 0),
                7 to Pair(0, 0),
                8 to Pair(0, 0),
                9 to Pair(0, 0),
                10 to Pair(0, 0)
            )
            leftEyeSightValue = _sightLevel.value
        } else {
            rightEyeSightValue = _sightLevel.value
        }
        viewModelScope.launch {
            delay(700)
            _sightLevel.update { 1 }
        }
        _isVisualAcuityContentVisible.update { false }
        _isMeasuringDistanceContentVisible.update { true }
        if (!_isLeftEye.value) {
            toResultScreen()
        } else {
            _isLeftEye.update { false }
        }
    }

    private fun updateRandomList() {
        _randomList.update { mutableListOf() }
        var ranNum = (2..7).random()
        for (i in 1..3) {
            while (ranNum in randomList.value) {
                ranNum = (2..7).random()
            }
            _randomList.update {
                it.add(ranNum)
                it
            }
        }
        val prevNum = ansNum.value
        _ansNum.update {
            var newNum = randomList.value[(0..2).random()]
            while (prevNum == newNum) {
                newNum = randomList.value[(0..2).random()]
            }
            newNum
        }
    }

    fun init() {
        _isLeftEye.update { true }
        _sightLevel.update { 1 }
        updateRandomList()
        leftEyeSightValue = 1
        rightEyeSightValue = 1
        sightHistory = mutableMapOf(
            1 to Pair(0, 0),
            2 to Pair(0, 0),
            3 to Pair(0, 0),
            4 to Pair(0, 0),
            5 to Pair(0, 0),
            6 to Pair(0, 0),
            7 to Pair(0, 0),
            8 to Pair(0, 0),
            9 to Pair(0, 0),
            10 to Pair(0, 0)
        )
        _isMeasuringDistanceContentVisible.update { true }
        _isCoveredEyeCheckingContentVisible.update { false }
        _isVisualAcuityContentVisible.update { false }
    }
}