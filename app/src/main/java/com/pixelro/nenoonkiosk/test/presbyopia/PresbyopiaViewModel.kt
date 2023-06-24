package com.pixelro.nenoonkiosk.test.presbyopia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PresbyopiaViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private var currentItemNumber = ItemNumber.First
    private val _isFirstItemVisible = MutableStateFlow(true)
    val isFirstItemVisible: StateFlow<Boolean> = _isFirstItemVisible
    private val _isSecondItemVisible = MutableStateFlow(false)
    val isSecondItemVisible: StateFlow<Boolean> = _isSecondItemVisible
    private val _isThirdItemVisible = MutableStateFlow(false)
    val isThirdItemVisible: StateFlow<Boolean> = _isThirdItemVisible
    private var firstDistance = 0f
    private var secondDistance = 0f
    private var thirdDistance = 0f

    fun moveToNextStep(dist: Float, toResultScreen: () -> Unit) {
        when (currentItemNumber) {
            ItemNumber.First -> {
                currentItemNumber = ItemNumber.Second
                firstDistance = dist
                _isFirstItemVisible.update { false }
                _isSecondItemVisible.update { true }
            }

            ItemNumber.Second -> {
                currentItemNumber = ItemNumber.Third
                secondDistance = dist
                _isSecondItemVisible.update { false }
                _isThirdItemVisible.update { true }
            }

            ItemNumber.Third -> {
                thirdDistance = dist
                toResultScreen()
            }
        }
    }

    fun getPresbyopiaTestResult(): PresbyopiaTestResult {
        return PresbyopiaTestResult(firstDistance, secondDistance, thirdDistance)
    }

    fun init() {
        currentItemNumber = ItemNumber.First
        _isFirstItemVisible.update { true }
        _isSecondItemVisible.update { false }
        _isThirdItemVisible.update { false }
    }

    enum class ItemNumber {
        First, Second, Third
    }
}