package com.pixelro.nenoonkiosk

import android.app.Application
import android.graphics.PointF
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.SizeF
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.pixelro.nenoonkiosk.data.TestType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class NenoonViewModel : ViewModel() {


    private val _screenToFaceDistance = MutableStateFlow(0f)
    val screenToFaceDistance: StateFlow<Float> = _screenToFaceDistance

    private val _inputImageSizeX = MutableStateFlow(1f)
    private val _inputImageSizeY = MutableStateFlow(1f)
    private val _rightEyePosition = MutableStateFlow(PointF(0f, 0f))
    private val _leftEyePosition = MutableStateFlow(PointF(0f, 0f))
    private val _rotX = MutableStateFlow(0f)
    private val _rotY = MutableStateFlow(0f)
    private val _rotZ = MutableStateFlow(0f)
    private val _leftEyeContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    private val _rightEyeContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    private val _upperLipTopContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    private val _upperLipBottomContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    private val _lowerLipTopContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    private val _lowerLipBottomContour = MutableStateFlow(listOf(PointF(0f, 0f)))
    private val _faceContour = MutableStateFlow(listOf(PointF(0f, 0f)))

    val inputImageSizeX: StateFlow<Float> = _inputImageSizeX
    val inputImageSizeY: StateFlow<Float> = _inputImageSizeY
    val rightEyePosition: StateFlow<PointF> = _rightEyePosition
    val leftEyePosition: StateFlow<PointF> = _leftEyePosition
    val rotX: StateFlow<Float> = _rotX
    val rotY: StateFlow<Float> = _rotY
    val rotZ: StateFlow<Float> = _rotZ
    val leftEyeContour: StateFlow<List<PointF>> = _leftEyeContour
    val rightEyeContour: StateFlow<List<PointF>> = _rightEyeContour
    val upperLipTopContour: StateFlow<List<PointF>> = _upperLipTopContour
    val upperLipBottomContour: StateFlow<List<PointF>> = _upperLipBottomContour
    val lowerLipTopContour: StateFlow<List<PointF>> = _lowerLipTopContour
    val lowerLipBottomContour: StateFlow<List<PointF>> = _lowerLipBottomContour
    val faceContour: StateFlow<List<PointF>> = _faceContour

    private val _selectedTestType = MutableStateFlow(TestType.None)
    val selectedTestType: StateFlow<TestType> = _selectedTestType

    private val _pixelDensity = MutableStateFlow(0f)
    val pixelDensity: StateFlow<Float> = _pixelDensity
    private val _screenWidthDp = MutableStateFlow(0)
    val screenWidthDp: StateFlow<Int> = _screenWidthDp
    private val _screenHeightDp = MutableStateFlow(0)
    val screenHeightDp: StateFlow<Int> = _screenHeightDp
    private val _focalLength = MutableStateFlow(0f)
    val focalLength: StateFlow<Float> = _focalLength
    private val _lensSize = MutableStateFlow(SizeF(0f, 0f))
    val lensSize: StateFlow<SizeF> = _lensSize

    fun updateLocalConfigurationValues(pixelDensity: Float, screenWidthDp: Int, screenHeightDp: Int, focalLength: Float, lensSize: SizeF) {
        _pixelDensity.update { pixelDensity }
        _screenWidthDp.update { screenWidthDp }
        _screenHeightDp.update { screenHeightDp }
        _focalLength.update { focalLength }
        _lensSize.update { lensSize }
    }

    fun updateSelectedTestType(testType: TestType) {
        _selectedTestType.update { testType }
    }

    fun updateFaceDetectionData(rightEyePosition: PointF, leftEyePosition: PointF, rotX: Float, rotY: Float, rotZ: Float, width: Float, height: Float) {
        _rightEyePosition.update { PointF(rightEyePosition.x, rightEyePosition.y) }
        _leftEyePosition.update { PointF(leftEyePosition.x, leftEyePosition.y) }
        _rotX.update { rotX }
        _rotY.update { rotY }
        _rotZ.update { rotZ }
        _inputImageSizeX.update { width }
        _inputImageSizeY.update { height }

        updateScreenToFaceDistance()
    }

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

    fun updateScreenToFaceDistance() {
        if((rightEyePosition.value.x - leftEyePosition.value.y) != 0f && lensSize.value.width != 0f) {
            _screenToFaceDistance.update {
                (50f / 41.5f) * (focalLength.value * 63) * inputImageSizeX.value / ((rightEyePosition.value.x - leftEyePosition.value.x) * (lensSize.value.width))
            }
        } else {
            return
        }
    }
}