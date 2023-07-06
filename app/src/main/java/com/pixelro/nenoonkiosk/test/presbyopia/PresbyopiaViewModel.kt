package com.pixelro.nenoonkiosk.test.presbyopia

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
class PresbyopiaViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

//    private val _isFirstItemVisible = MutableStateFlow(true)
//    val isFirstItemVisible: StateFlow<Boolean> = _isFirstItemVisible
//    private val _isSecondItemVisible = MutableStateFlow(false)
//    val isSecondItemVisible: StateFlow<Boolean> = _isSecondItemVisible
//    private val _isThirdItemVisible = MutableStateFlow(false)
//    val isThirdItemVisible: StateFlow<Boolean> = _isThirdItemVisible
    private val _isMovedTo40cm = MutableStateFlow(false)
    val isMovedTo50cm: StateFlow<Boolean> = _isMovedTo40cm
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

    fun updateIsMovedTo50cm(isMoved: Boolean) {
        _isMovedTo40cm.update { isMoved }
    }

    fun updateIsUnder25cm(isUnder: Boolean) {
        _isUnder25cm.update { isUnder }
    }

    fun moveToNextStep(dist: Float, handleProgress: (Float) -> Unit, toResultScreen: () -> Unit) {
        when (_tryCount.value) {
            0 -> {
                _isMovedTo40cm.update { false }
                _isUnder25cm.update { false }
                _isNumberShowing.update { true }
                _isBlinkingDone.update { false }
                firstDistance = dist / 10
                _tryCount.update { it + 1 }
                handleProgress(0.33f)
            }
            1 -> {
                _isMovedTo40cm.update { false }
                _isUnder25cm.update { false }
                _isNumberShowing.update { true }
                _isBlinkingDone.update { false }
                secondDistance = dist / 10
                _tryCount.update { it + 1 }
                handleProgress(0.66f)
            }
            else -> {
                viewModelScope.launch {
                    handleProgress(1.2f)
                    delay(500)
                    thirdDistance = dist / 10
                    toResultScreen()
                }
            }
        }
    }

    fun blinkNumber() {
        viewModelScope.launch {
            var count = 16
            while (count > 0) {
                _isNumberShowing.update { !it }
                delay(250)
                count--
            }
            _isBlinkingDone.update { true }
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
        age -= 20
        Log.e("presbyopiaResult", "firstDistance: ${firstDistance}\nsecondDistance: ${secondDistance}\nthirdDistance: ${thirdDistance}\n${avgDistance}\nage: $age")
        return PresbyopiaTestResult(firstDistance, secondDistance, thirdDistance, avgDistance, age)
    }

    fun init() {
        _isMovedTo40cm.update { false }
        _isUnder25cm.update { false }
        _isNumberShowing.update { true }
        _isBlinkingDone.update { false }
        _tryCount.update { 0 }
//        _isFirstItemVisible.update { true }
//        _isSecondItemVisible.update { false }
//        _isThirdItemVisible.update { false }
    }
}