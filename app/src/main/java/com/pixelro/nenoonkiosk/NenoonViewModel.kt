package com.pixelro.nenoonkiosk

import android.graphics.PointF
import android.util.Log
import android.util.SizeF
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.data.VisionDisorderType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.tan

class NenoonViewModel : ViewModel() {

    init {
        showSplashScreen()

    }

    private fun checkBackground() {
        viewModelScope.launch {
            while(true) {

                delay(100000)
            }
        }
    }

    private fun showSplashScreen() {
        viewModelScope.launch {
            delay(1000)
            _isLaunching.update { false }
        }
    }

    // Face detection
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

    // Measuring Distance
    val measuringDistanceContentVisibleState = MutableTransitionState(false)
    private val _testDistance = MutableStateFlow(0)
    val testDistance: StateFlow<Int> = _testDistance

    fun updateTestDistance() {
        _testDistance.update { screenToFaceDistance.value.toInt() }
    }

    // Covered Eye Checking
    val coveredEyeCheckingContentVisibleState = MutableTransitionState(false)
    private val _leftTime = MutableStateFlow(0f)
    val leftTime: StateFlow<Float> = _leftTime
    private val _isTimerShowing = MutableStateFlow(false)
    val isTimerShowing: StateFlow<Boolean> = _isTimerShowing

    fun initializeCoveredEyeChecking() {
//        _isCoveredEyeCheckingDone.update { false }
        _leftTime.update { 3f }
        _isTimerShowing.update { false }
    }

//    fun updateIsCoveredEyeCheckingDone(isDone: Boolean) {
//        _isCoveredEyeCheckingDone.update { isDone }
//    }

    fun checkCoveredEye(
        nextVisibleState: MutableTransitionState<Boolean>
    ) {
        viewModelScope.launch {
            var count = 0
            _leftTime.update { 3f }
            while(count < 6) {
                delay(500)
                Log.e("checkCoveredEye", "$count")
                if(
//                    when(isLeftEye.value) {
//                        true -> leftEyeOpenProbability.value
//                        else -> rightEyeOpenProbability.value
//                    } < 0.7f && abs(leftEyeOpenProbability.value - rightEyeOpenProbability.value) > 0.3f
                    true
                ) {
                    if(!isTimerShowing.value) {
                        _isTimerShowing.update { true }
                    }
                    count++
                    _leftTime.update { (it - 0.5f) }
                } else {
                    if(isTimerShowing.value) {
                        _isTimerShowing.update { false }
                    }
                    count = 0
                    _leftTime.update { 3f }
                }
            }
            coveredEyeCheckingContentVisibleState.targetState = false
            nextVisibleState.targetState = true
        }
    }

    // Nemonic Printer
    private val _printerName = MutableStateFlow("")
    val printerName: StateFlow<String> = _printerName
    private val _printerMacAddress = MutableStateFlow("")
    val printerMacAddress: StateFlow<String> = _printerMacAddress
    private val _nemonicList = MutableStateFlow(emptyList<Pair<String, String>>())
    val nemonicList: StateFlow<List<Pair<String, String>>> = _nemonicList
    private val _printString = MutableStateFlow("")
    val printString: StateFlow<String> = _printString

    fun updatePrintString(string: String) {
        _printString.update { string }
    }

    fun updatePrinter(name: String, address: String) {
        _printerName.update { name }
        _printerMacAddress.update { address }
    }

    // UI


    // Permission
    private val _isWriteSettingsPermissionGranted = MutableStateFlow(false)
    val isWriteSettingsPermissionGranted: StateFlow<Boolean> = _isWriteSettingsPermissionGranted
    private val _isBluetoothPermissionsGranted = MutableStateFlow(false)
    val isBluetoothPermissionsGranted: StateFlow<Boolean> = _isBluetoothPermissionsGranted
    private val _isCameraPermissionGranted = MutableStateFlow(false)
    val isCameraPermissionGranted: StateFlow<Boolean> = _isCameraPermissionGranted
    private val _isAllPermissionsGranted = MutableStateFlow(false)
    val isAllPermissionsGranted: StateFlow<Boolean> = _isAllPermissionsGranted

    fun updateIsWriteSettingsPermissionGranted(granted: Boolean) {
        _isWriteSettingsPermissionGranted.update { granted }
    }

    fun updateIsBluetoothPermissionsGranted(granted: Boolean) {
        _isBluetoothPermissionsGranted.update { granted }
    }

    fun updateIsCameraPermissionGranted(granted: Boolean) {
        _isCameraPermissionGranted.update { granted }
    }

    fun updateIsAllPermissionsGranted(granted: Boolean) {
        _isAllPermissionsGranted.update { granted }
    }

    fun checkIfAllPermissionsGranted() {
        if(isBluetoothPermissionsGranted.value && isCameraPermissionGranted.value && isWriteSettingsPermissionGranted.value) {
            _isAllPermissionsGranted.update { true }
        } else {
            _isAllPermissionsGranted.update { false }
        }
    }

    // Global
    private val _isLaunching = MutableStateFlow(true)
    val isLaunching: StateFlow<Boolean> = _isLaunching

    private val _selectedTestType = MutableStateFlow(TestType.None)
    val selectedTestType: StateFlow<TestType> = _selectedTestType
    private val _selectedTestName = MutableStateFlow("")
    val selectedTestName: StateFlow<String> = _selectedTestName
    private val _selectedTestDescription = MutableStateFlow("")
    val selectedTestDescription: StateFlow<String> = _selectedTestDescription
    private val _selectedTestMenuDescription = MutableStateFlow("")
    val selectedTestMenuDescription: StateFlow<String> = _selectedTestMenuDescription
    private val _isLeftEye = MutableStateFlow(false)
    val isLeftEye: StateFlow<Boolean> = _isLeftEye



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

    // Presbyopia test

    private val _firstDistance = MutableStateFlow(0f)
    val firstDistance: StateFlow<Float> = _firstDistance
    private val _secondDistance = MutableStateFlow(0f)
    val secondDistance: StateFlow<Float> = _secondDistance
    private val _thirdDistance = MutableStateFlow(0f)
    val thirdDistance: StateFlow<Float> = _thirdDistance
    private val _avgDistance = MutableStateFlow(0f)
    val avgDistance: StateFlow<Float> = _avgDistance

    fun initializePresbyopiaTest() {

    }

    fun updateFirstDistance() {
        _firstDistance.update { screenToFaceDistance.value }
    }

    fun updateSecondDistance() {
        _secondDistance.update { screenToFaceDistance.value }
    }

    fun updateThirdDistance() {
        _thirdDistance.update { screenToFaceDistance.value }
    }

    fun updateAvgDistance() {
        _avgDistance.update {
            (firstDistance.value + secondDistance.value + thirdDistance.value) / 3
        }
        _printString.update {
            "조절력: ${(avgDistance.value).roundToInt().toFloat() / 10}cm"
        }
    }

    // Visual acuity test
    val visualAcuityTestCommonContentVisibleState = MutableTransitionState(false)
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
    private val _ansNum = MutableStateFlow(0)
    val ansNum: StateFlow<Int> = _ansNum
    private val _isSightednessTesting = MutableStateFlow(false)
    val isSightednessTesting: StateFlow<Boolean> = _isSightednessTesting

    fun initializeVisualAcuityTest() {
        _isLeftEye.update { false }
        measuringDistanceContentVisibleState.targetState = true
        coveredEyeCheckingContentVisibleState.targetState = false
        visualAcuityTestCommonContentVisibleState.targetState = false
    }

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

    fun updateSightTestResult() {
        _printString.update {
            val leftEyeSighted = when(leftEyeSightedValue.value) {
                VisionDisorderType.Normal -> "정상"
                VisionDisorderType.Myopia -> "근시"
                VisionDisorderType.Hyperopia -> "원시"
                else -> "난시"
            }

            val rightEyeSighted = when(rightEyeSightedValue.value) {
                VisionDisorderType.Normal -> "정상"
                VisionDisorderType.Myopia -> "근시"
                VisionDisorderType.Hyperopia -> "원시"
                else -> "난시"
            }
            "좌안 시력: ${leftEyeSightValue.value.toFloat() / 10} $leftEyeSighted," +
            "우안 시력: ${rightEyeSightValue.value.toFloat() / 10} $rightEyeSighted"
        }
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
    // Amsler Grid Test
    val amslerGridContentVisibleState = MutableTransitionState(false)
    private val _widthSize = MutableStateFlow(100f)
    val widthSize: StateFlow<Float> = _widthSize
    private val _heightSize = MutableStateFlow(100f)
    val heightSize: StateFlow<Float> = _heightSize
    private val _currentSelectedArea = MutableStateFlow(listOf(false, false, false, false, false, false, false, false, false))
    val currentSelectedArea: StateFlow<List<Boolean>> = _currentSelectedArea
    private val _leftSelectedArea = MutableStateFlow(emptyList<Boolean>())
    val leftSelectedArea: StateFlow<List<Boolean>> = _leftSelectedArea
    private val _rightSelectedArea = MutableStateFlow(listOf(false, false, false, false, false, false, false, false, false))
    val rightSelectedArea: StateFlow<List<Boolean>> = _rightSelectedArea

    fun initializeAmslerGridTest() {
        measuringDistanceContentVisibleState.targetState = true
        coveredEyeCheckingContentVisibleState.targetState = false
        amslerGridContentVisibleState.targetState = false
        _isLeftEye.update { false }
    }

    fun updateCurrentSelectedArea(position: Offset) {
        _currentSelectedArea.update {
            val tmpList = it.toMutableList()
            if(position.x in 0f..299f && position.y in 0f..299f) {
                tmpList[0] = !it[0]
            } else if(position.x in 300f..599f && position.y in 0f..299f) {
                tmpList[1] = !it[1]
            } else if(position.x in 600f..900f && position.y in 0f..299f) {
                tmpList[2] = !it[2]
            } else if(position.x in 0f..299f && position.y in 300f..599f) {
                tmpList[3] = !it[3]
            } else if(position.x in 300f..599f && position.y in 300f..599f) {
                tmpList[4] = !it[4]
            } else if(position.x in 600f..900f && position.y in 300f..599f) {
                tmpList[5] = !it[5]
            } else if(position.x in 0f..299f && position.y in 600f..900f) {
                tmpList[6] = !it[6]
            } else if(position.x in 300f..599f && position.y in 600f..900f) {
                tmpList[7] = !it[7]
            } else {
                tmpList[8] = !it[8]
            }
            tmpList
        }
    }

    fun updateLeftSelectedArea(list: List<Boolean>) {
        _leftSelectedArea.update { list }
    }

    fun updateRightSelectedArea(list: List<Boolean>) {
        _rightSelectedArea.update { list }
    }

    // M-Chart Test

    val mChartContentVisibleState = MutableTransitionState(false)
    private val _mChartResult = MutableStateFlow(listOf<Int>())
    val mChartResult: StateFlow<List<Int>> = _mChartResult
    private val _isVertical = MutableStateFlow(true)
    val isVertical: StateFlow<Boolean> = _isVertical
    private val _currentLevel = MutableStateFlow(0)
    val currentLevel: StateFlow<Int> = _currentLevel
    private val _mChartImageId = MutableStateFlow(R.drawable.mchart_0_0)
    val mChartImageId: StateFlow<Int> = _mChartImageId

    fun initializeMChartTest() {
        _isVertical.update { true }
        _currentLevel.update { 0 }
        _mChartResult.update { listOf() }
        measuringDistanceContentVisibleState.targetState = true
        coveredEyeCheckingContentVisibleState.targetState = false
        mChartContentVisibleState.targetState = false
        _isLeftEye.update { false }
    }

    fun updateMChartResult(value: Int) {
        _mChartResult.update {
            val list = it + value
            list
        }
        Log.e("updateMChartResult", "${mChartResult.value}")
    }

    fun updateIsVertical(isVertical: Boolean) {
        _isVertical.update { isVertical }
    }

    fun updateCurrentLevel(level: Int) {
        _currentLevel.update { level }
        _mChartImageId.update {
            when(currentLevel.value) {
                0 -> R.drawable.mchart_0_0
                1 -> R.drawable.mchart_0_1
                2 -> R.drawable.mchart_0_2
                3 -> R.drawable.mchart_0_3
                4 -> R.drawable.mchart_0_4
                5 -> R.drawable.mchart_0_5
                6 -> R.drawable.mchart_0_6
                7 -> R.drawable.mchart_0_7
                8 -> R.drawable.mchart_0_8
                9 -> R.drawable.mchart_0_9
                10 -> R.drawable.mchart_1_0
                11 -> R.drawable.mchart_1_1
                12 -> R.drawable.mchart_1_2
                13 -> R.drawable.mchart_1_3
                14 -> R.drawable.mchart_1_4
                15 -> R.drawable.mchart_1_5
                16 -> R.drawable.mchart_1_6
                17 -> R.drawable.mchart_1_7
                18 -> R.drawable.mchart_1_8
                19 -> R.drawable.mchart_1_9
                else -> R.drawable.mchart_2_0
            }
        }
    }








    init {
        updateRandomList()
    }
}