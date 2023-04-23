package com.pixelro.nenoonkiosk

import android.graphics.PointF
import android.util.Log
import android.util.SizeF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.data.VisionDisorderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.tan

class NenoonViewModel : ViewModel() {

    // Face detection
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
    private val _leftEyeOpenProbability = MutableStateFlow(0f)
    private val _rightEyeOpenProbability = MutableStateFlow(0f)

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
    val leftEyeOpenProbability: StateFlow<Float> = _leftEyeOpenProbability
    val rightEyeOpenProbability: StateFlow<Float> = _rightEyeOpenProbability

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

    fun updateLocalConfigurationValues(pixelDensity: Float, screenWidthDp: Int, screenHeightDp: Int, focalLength: Float, lensSize: SizeF) {
        _pixelDensity.update { pixelDensity }
        _screenWidthDp.update { screenWidthDp }
        _screenHeightDp.update { screenHeightDp }
        _focalLength.update { focalLength }
        _lensSize.update { lensSize }
    }

    fun updateEyeOpenProbability(left: Float, right: Float) {
        _leftEyeOpenProbability.update { left }
        _rightEyeOpenProbability.update { right }
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

    // Nemonic Printer


    // UI


    // Global
    private val _selectedTestType = MutableStateFlow(TestType.None)
    val selectedTestType: StateFlow<TestType> = _selectedTestType
    private val _selectedTestName = MutableStateFlow("")
    val selectedTestName: StateFlow<String> = _selectedTestName
    private val _selectedTestDescription = MutableStateFlow("")
    val selectedTestDescription: StateFlow<String> = _selectedTestDescription
    private val _selectedTestMenuDescription = MutableStateFlow("")
    val selectedTestMenuDescription: StateFlow<String> = _selectedTestMenuDescription
    private val _testDistance = MutableStateFlow(0)
    val testDistance: StateFlow<Int> = _testDistance
    private val _nemonicList = MutableStateFlow(emptyList<Pair<String, String>>())
    val nemonicList: StateFlow<List<Pair<String, String>>> = _nemonicList



    fun updateSelectedTestType(testType: TestType) {
        _selectedTestType.update { testType }
        _selectedTestName.update {
            when(testType) {
                TestType.Presbyopia -> "조절력 검사\n(안구 나이 검사)"
                TestType.ShortDistanceVisualAcuity -> "근거리 시력 검사"
                TestType.LongDistanceVisualAcuity -> "원거리 시력 검사"
                TestType.ChildrenVisualAcuity -> "어린이 시력 검사"
                TestType.AmslerGrid -> "암슬러 차트"
                else -> "엠식 변형시 검사"
            }
        }
        _selectedTestDescription.update {
            when(testType) {
                TestType.Presbyopia -> "나이가 들면서 수정체의 탄성력이 감소되어 조절력이 떨어지는 안질환으로 가까운 곳의 글자가 잘 안보이는 현상을 노안이라고 말합니다."
                TestType.ShortDistanceVisualAcuity -> "눈이 두 점을 구별할 수 있는 최소의 시각을 최소시각이라고 하며, 시력은 이 최소시각이 어느정도인가를 말하는 것입니다.\n\n정식 검사는 6m 거리에서 시행합니다. 본 검사는 3m 이하에서 측정가능하도록 개발되었습니다."
                TestType.LongDistanceVisualAcuity -> "눈이 두 점을 구별할 수 있는 최소의 시각을 최소시각이라고 하며, 시력은 이 최소시각이 어느정도인가를 말하는 것입니다.\n\n정식 검사는 6m 거리에서 시행합니다. 본 검사는 3m 이하에서 측정가능하도록 개발되었습니다."
                TestType.ChildrenVisualAcuity -> "눈이 두 점을 구별할 수 있는 최소의 시각을 최소시각이라고 하며, 시력은 이 최소시각이 어느정도인가를 말하는 것입니다.\n\n정식 검사는 6m 거리에서 시행합니다. 본 검사는 3m 이하에서 측정가능하도록 개발되었습니다."
                TestType.AmslerGrid -> "망막신경 중에서 초점이 맺히는 부분인 황반에 변성이 생기면 격자 무늬가 휘어져 보이거나 공백 또는 검게 보이는 현상이 발생합니다."
                else -> "굴절이상은 망막에 초점이 정확하게 맺히지 못할 때 생기며, 근시와 원시로 구별하여 안경으로 교정합니다. 성장기 어린이와 청소년의 경우 6개월, 성인의 경우 1년마다 안경 렌즈를 바꾸는 것이 좋습니다."
            }
        }
    }

    fun updateTestDistance() {
        _testDistance.update { screenToFaceDistance.value.toInt() }
    }

    fun updateNemonicList(name: String, addr: String) {
        _nemonicList.update {
            if(Pair(name, addr) !in it) {
                it + (Pair(name, addr))
            } else {
                it
            }
        }
        Log.e("viewModel", "${_nemonicList.value}")
    }

    // Presbyopia test



    // Visual acuity test

    private val _sightHistory = MutableStateFlow(mutableMapOf<Int, Pair<Int, Int>>())
    val sightHistory: StateFlow<MutableMap<Int, Pair<Int, Int>>> = _sightHistory
    private val _sightValue = MutableStateFlow(1)
    val sightValue: StateFlow<Int> = _sightValue
    private val _leftEyeSightValue = MutableStateFlow(1)
    val leftEyeSightValue: StateFlow<Int> = _leftEyeSightValue
    private val _rightEyeSightValue = MutableStateFlow(1)
    val rightEyeSightValue: StateFlow<Int> = _rightEyeSightValue
    private val _leftEyeSightedValue = MutableStateFlow(VisionDisorderType.Normal)
    val leftEyeSightedValue: StateFlow<VisionDisorderType> = _leftEyeSightedValue
    private val _rightEyeSightedValue = MutableStateFlow(VisionDisorderType.Normal)
    val rightEyeSightedValue: StateFlow<VisionDisorderType> = _rightEyeSightedValue
    private val _randomList = MutableStateFlow(mutableListOf(0))
    val randomList: StateFlow<MutableList<Int>> = _randomList
    private val _isLeftEye = MutableStateFlow(false)
    val isLeftEye: StateFlow<Boolean> = _isLeftEye
    private val _ansNum = MutableStateFlow(0)
    val ansNum: StateFlow<Int> = _ansNum
    private val _isSightednessTesting = MutableStateFlow(false)
    val isSightednessTesting: StateFlow<Boolean> = _isSightednessTesting

    fun updateLeftEyeSightedValue(type: VisionDisorderType) {
        _leftEyeSightedValue.update { type }
    }

    fun updateRightEyeSightedValue(type: VisionDisorderType) {
        _rightEyeSightedValue.update { type }
    }

    fun updateIsSightednessTesting(isTesting: Boolean) {
        _isSightednessTesting.update { isTesting }
    }

    fun updateSightValue(value: Int) {
        _sightValue.update { value }
    }

    fun updateLeftEyeSightValue(value: Int) {
        _leftEyeSightValue.update { value }
    }

    fun updateRightEyeSightValue(value: Int) {
        _rightEyeSightValue.update { value }
    }

    fun updateIsLeftEye(isLeftEye: Boolean) {
        _isLeftEye.update { isLeftEye }
    }

    fun initializeSightTest() {
        _sightHistory.update {
            mutableMapOf(
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
        }
        _sightValue.update { 1 }
        _isSightednessTesting.update { false }
        _isLeftEye.update { false }
    }

    fun initializeRightTest() {
        _sightHistory.update {
            mutableMapOf(
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
        }
        _sightValue.update { 1 }
    }

    fun updateSightHistory(sightValue: Int, rightCount: Int, wrongCount: Int) {
        _sightHistory.update {
            it[sightValue] = Pair(rightCount, wrongCount)
            it
        }
    }

    fun updateRandomList() {
        _randomList.update { mutableListOf() }
        var ranNum = (2..7).random()
        for(i in 1..3) {
            while(ranNum in randomList.value) {
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
            while(prevNum == newNum) {
                newNum = randomList.value[(0..2).random()]
            }
            newNum
        }
    }

    // Macular degeneration test
    //

    private val _color = MutableStateFlow(Color(0x00000000))
    val color: StateFlow<Color> = _color
    private val _gazingPoint = MutableStateFlow(Offset(0f, 0f))
    val gazingPoint:StateFlow<Offset> = _gazingPoint
    private val _touchPosition = MutableStateFlow(Offset(0f, 0f))
    val touchPosition: StateFlow<Offset> = _touchPosition
    private val _widthSize = MutableStateFlow(100f)
    val widthSize: StateFlow<Float> = _widthSize
    private val _heightSize = MutableStateFlow(100f)
    val heightSize: StateFlow<Float> = _heightSize

    fun updateColor(color: Color) {
        _color.update { color }
    }

    fun updateWidthSize(size: Float) {
        _widthSize.update { size }
    }

    fun updateHeightSize(size: Float) {
        _heightSize.update { size }
    }

    fun updateTouchPosition(position: Offset) {
        _touchPosition.update { position }
    }

















    init {
        updateRandomList()
    }
}