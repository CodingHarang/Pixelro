package com.pixelro.nenoonkiosk.facedetection

import android.app.Application
import android.graphics.PointF
import android.graphics.Rect
import androidx.compose.animation.core.MutableTransitionState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.TestType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceDetectionViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val _screenToFaceDistance = MutableStateFlow(0f)
    val screenToFaceDistance: StateFlow<Float> = _screenToFaceDistance
    private val _inputImageSizeX = MutableStateFlow(1f)
    val inputImageSizeX: StateFlow<Float> = _inputImageSizeX
    private val _inputImageSizeY = MutableStateFlow(1f)
    val inputImageSizeY: StateFlow<Float> = _inputImageSizeY
    private val _rightEyePosition = MutableStateFlow(PointF(0f, 0f))
    val rightEyePosition: StateFlow<PointF> = _rightEyePosition
    private val _leftEyePosition = MutableStateFlow(PointF(0f, 0f))
    val leftEyePosition: StateFlow<PointF> = _leftEyePosition
    private val _rotX = MutableStateFlow(0f)
    val rotX: StateFlow<Float> = _rotX
    private val _rotY = MutableStateFlow(0f)
    val rotY: StateFlow<Float> = _rotY
    private val _rotZ = MutableStateFlow(0f)
    val rotZ: StateFlow<Float> = _rotZ
    private val _leftEyeContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    val leftEyeContour: StateFlow<List<PointF>> = _leftEyeContour
    private val _rightEyeContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    val rightEyeContour: StateFlow<List<PointF>> = _rightEyeContour
    private val _upperLipTopContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    val upperLipTopContour: StateFlow<List<PointF>> = _upperLipTopContour
    private val _upperLipBottomContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    val upperLipBottomContour: StateFlow<List<PointF>> = _upperLipBottomContour
    private val _lowerLipTopContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    val lowerLipTopContour: StateFlow<List<PointF>> = _lowerLipTopContour
    private val _lowerLipBottomContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    val lowerLipBottomContour: StateFlow<List<PointF>> = _lowerLipBottomContour
    private val _faceContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    val faceContour: StateFlow<List<PointF>> = _faceContour
    private val _leftEyeOpenProbability = MutableStateFlow(0f)
    val leftEyeOpenProbability: StateFlow<Float> = _leftEyeOpenProbability
    private val _rightEyeOpenProbability = MutableStateFlow(0f)
    val rightEyeOpenProbability: StateFlow<Float> = _rightEyeOpenProbability
//    private val _bitmap = MutableStateFlow(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
//    val bitmap: StateFlow<Bitmap> = _bitmap

    fun updateFaceDetectionData(
        boundingBox: Rect,
        leftEyePosition: PointF?,
        rightEyePosition: PointF?,
        rotX: Float,
        rotY: Float,
        rotZ: Float,
        leftEyeOpenProbability: Float?,
        rightEyeOpenProbability: Float?,
    ) {
        _rightEyePosition.update { PointF(rightEyePosition?.x ?: it.x, rightEyePosition?.y ?: it.y) }
        _leftEyePosition.update { PointF(leftEyePosition?.x ?: it.x, leftEyePosition?.y ?: it.y) }
        _rotX.update { rotX }
        _rotY.update { rotY }
        _rotZ.update { rotZ }
        _leftEyeOpenProbability.update { leftEyeOpenProbability ?: 100f }
        _rightEyeOpenProbability.update { rightEyeOpenProbability ?: 100f }
        updateScreenToFaceDistance()
    }

//    fun updateBitmap(bitmap: Bitmap) {
//        _bitmap.update { bitmap }
//    }

    fun updateFaceContourData(leftEyeContour: List<PointF>, rightEyeContour: List<PointF>, upperLipTopContour: List<PointF>, upperLipBottomContour: List<PointF>, lowerLipTopContour: List<PointF>, lowerLipBottomContour: List<PointF>, faceContour: List<PointF>, width: Float, height: Float) {
        _leftEyeContour.update {
            List(leftEyeContour.size) {
                PointF(leftEyeContour[it].x / width, leftEyeContour[it].y / height)
            }
        }
        _rightEyeContour.update {
            List(rightEyeContour.size) {
                PointF(rightEyeContour[it].x / width, rightEyeContour[it].y / height)
            }
        }
        _upperLipTopContour.update {
            List(upperLipTopContour.size) {
                PointF(upperLipTopContour[it].x / width, upperLipTopContour[it].y / height)
            }
        }
        _upperLipBottomContour.update {
            List(upperLipBottomContour.size) {
                PointF(upperLipBottomContour[it].x / width, upperLipBottomContour[it].y / height)
            }
        }
        _lowerLipTopContour.update {
            List(lowerLipTopContour.size) {
                PointF(lowerLipTopContour[it].x / width, lowerLipTopContour[it].y / height)
            }
        }
        _lowerLipBottomContour.update {
            List(lowerLipBottomContour.size) {
                PointF(lowerLipBottomContour[it].x / width, lowerLipBottomContour[it].y / height)
            }
        }
        _faceContour.update {
            List(faceContour.size) {
                PointF(faceContour[it].x / width, faceContour[it].y / height)
            }
        }
    }

    fun updateInputImageSize(x: Float, y: Float) {
        _inputImageSizeX.update { x }
        _inputImageSizeY.update { y }
    }

    private fun updateScreenToFaceDistance() {
        if((rightEyePosition.value.x - leftEyePosition.value.y) != 0f && GlobalValue.lensSize.width != 0f) {
            _screenToFaceDistance.update {
                val prev = _screenToFaceDistance.value
                val dist = 1.1f * (GlobalValue.focalLength * 63) * inputImageSizeX.value / ((rightEyePosition.value.x - leftEyePosition.value.x) * (GlobalValue.lensSize.width))
                if(dist > 600f || dist < 1f) prev
                else dist
            }
        } else {
            return
        }
    }

    // Covered Eye Checking
    private val _leftTime = MutableStateFlow(0f)
    val leftTime: StateFlow<Float> = _leftTime
    private val _isTimerShowing = MutableStateFlow(false)
    val isTimerShowing: StateFlow<Boolean> = _isTimerShowing

    fun initializeCoveredEyeChecking(isLeftEye: Boolean, toNextContent: () -> Unit) {
        _leftTime.update { 5f }
        _isTimerShowing.update { false }
        viewModelScope.launch {
            when(isLeftEye) {
                true -> {
//                    while(_leftEyeOpenProbability.value < 50f) {}
                    _isTimerShowing.update { true }
                    startTimer { toNextContent() }
                }
                false -> {
//                    while(_rightEyeOpenProbability.value < 50f) {}
                    _isTimerShowing.update { true }
                    startTimer { toNextContent() }
                }
            }
        }
    }

    private fun startTimer(toNextContent: () -> Unit) {
        viewModelScope.launch {
            var count = 0
            _leftTime.update { 5.5f }
            while(count < 3) {
                delay(500)
                count++
                _leftTime.update { (it - 0.5f) }
            }
            toNextContent()
        }
    }
}