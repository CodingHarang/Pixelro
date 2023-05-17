package com.pixelro.nenoonkiosk

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.util.SizeF
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
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

class NenoonViewModel(application: Application) : AndroidViewModel(application) {

    val enterTransition = slideIn(
        animationSpec = TweenSpec(durationMillis = 500),
        initialOffset = { IntOffset(100, 0) }
    ) + fadeIn(
        animationSpec = TweenSpec(durationMillis = 500)
    )
    val exitTransition = slideOut(
        animationSpec = TweenSpec(durationMillis = 500),
        targetOffset = { IntOffset(-100, 0) }
    ) + fadeOut(
        animationSpec = TweenSpec(durationMillis = 500)
    )

    private fun checkBackgroundStatus() {
        viewModelScope.launch {
            while(true) {
                if(_isResumed.value) {
                    // Check screen saver timer
                    Log.e("viewModelScope", "${screenSaverTimer.value}")
                    _screenSaverTimer.update { screenSaverTimer.value - 0 }
                    if(screenSaverTimer.value < 0) {
                        _isScreenSaverOn.update { true }
                    }
                    // Check permissions
                    checkPermissions()
                    checkIsLocationOn()
                }
                delay(1000)
            }
        }
    }

    // check location is on


    // Screen Saver
    private val _isResumed = MutableStateFlow(false)
    private val _isPaused = MutableStateFlow(false)
    val exoPlayer = ExoPlayer.Builder(application.applicationContext).build()
    private val _screenSaverTimer = MutableStateFlow(10)
    val screenSaverTimer: StateFlow<Int> = _screenSaverTimer
    private val _isScreenSaverOn = MutableStateFlow(false)
    val isScreenSaverOn: StateFlow<Boolean> = _isScreenSaverOn

    fun resetScreenSaverTimer() {
        _screenSaverTimer.update { 10 }
        _isScreenSaverOn.update { false }
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

    fun checkCoveredEye() {
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
            if(_selectedTestType.value == TestType.ShortDistanceVisualAcuity) {
                visualAcuityTestCommonContentVisibleState.targetState = true
            } else if(_selectedTestType.value == TestType.AmslerGrid) {
                amslerGridContentVisibleState.targetState = true
            } else if(_selectedTestType.value == TestType.MChart) {
                mChartContentVisibleState.targetState = true
            }
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


    // Checking permission, location, bluetooth
    private val _isWriteSettingsPermissionGranted = MutableStateFlow(false)
    val isWriteSettingsPermissionGranted: StateFlow<Boolean> = _isWriteSettingsPermissionGranted
    private val _isBluetoothPermissionsGranted = MutableStateFlow(false)
    val isBluetoothPermissionsGranted: StateFlow<Boolean> = _isBluetoothPermissionsGranted
    private val _isCameraPermissionGranted = MutableStateFlow(false)
    val isCameraPermissionGranted: StateFlow<Boolean> = _isCameraPermissionGranted
    private val _isAllPermissionsGranted = MutableStateFlow(false)
    val isAllPermissionsGranted: StateFlow<Boolean> = _isAllPermissionsGranted
    private val _isLocationOn = MutableStateFlow(false)
    val isLocationOn: StateFlow<Boolean> = _isLocationOn
    private val _resolvableApiException = MutableStateFlow(ResolvableApiException(Status.RESULT_CANCELED))
    val resolvableApiException: StateFlow<ResolvableApiException> = _resolvableApiException

    private fun checkPermissions() {
        Log.e("checkPermission", "checkPermissions")
        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _isBluetoothPermissionsGranted.update { true }
            Log.e("checkPermission", "Bluetooth Permission Granted")
        } else {
            _isBluetoothPermissionsGranted.update { false }
            Log.e("checkPermission", "Bluetooth Permission not Granted")
        }
        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            _isCameraPermissionGranted.update { true }
            Log.e("checkPermission", "Camera Permission Granted")
        } else {
            _isCameraPermissionGranted.update { false }
            Log.e("checkPermission", "Camera Permission not Granted")
        }
        if(Settings.System.canWrite(getApplication())) {
            _isWriteSettingsPermissionGranted.update { true }
            Log.e("checkPermission", "Settings Permission Granted")
        } else {
            _isWriteSettingsPermissionGranted.update { false }
            Log.e("checkPermission", "Settings Permission not Granted")
        }

        if(isBluetoothPermissionsGranted.value && isCameraPermissionGranted.value && isWriteSettingsPermissionGranted.value && isLocationOn.value) {
            _isAllPermissionsGranted.update { true }
        } else {
            _isAllPermissionsGranted.update { false }
        }
    }

    private fun checkIsLocationOn() {
        val locationManager = getSystemService(getApplication(), LocationManager::class.java) as LocationManager

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    Log.e("lifecycleScope", "not enabled")
//                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

            val locationRequest: LocationRequest = LocationRequest.Builder(10000).build()
            val client: SettingsClient = LocationServices.getSettingsClient(getApplication() as Context)
            val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
            val gpsSettingTask: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            gpsSettingTask.addOnSuccessListener {
                Log.e("checkIsLocationOn", "success")
            }
            gpsSettingTask.addOnFailureListener { exception ->
                Log.e("checkIsLocationOn", "failure")
                if(exception is ResolvableApiException) {
                    try {
//                        val intentSenderRequest = IntentSenderRequest
//                            .Builder(exception.resolution)
//                            .build()
//                        Log.e("exception", "${exception}, ${exception.resolution}")
                        _resolvableApiException.update { exception }
                        _isLocationOn.update { false }
                    } catch(sendEx: IntentSender.SendIntentException) {

                    }
                }
            }
        } else {
            _isLocationOn.update { true }
        }
    }

    // Global
    private val _isShowingSplashScreen = MutableStateFlow(true)
    val isShowingSplashScreen: StateFlow<Boolean> = _isShowingSplashScreen
    private val _selectedTestType = MutableStateFlow(TestType.None)
    val selectedTestType: StateFlow<TestType> = _selectedTestType
    private val _selectedTestName = MutableStateFlow("")
    val selectedTestName: StateFlow<String> = _selectedTestName
    private val _selectedTestDescription = MutableStateFlow("")
    val selectedTestDescription: StateFlow<String> = _selectedTestDescription
    private val _selectedTestMenuDescription = MutableStateFlow("")
    val selectedTestMenuDescription: StateFlow<String> = _selectedTestMenuDescription
    private val _isLeftEye = MutableStateFlow(true)
    val isLeftEye: StateFlow<Boolean> = _isLeftEye

    private fun showSplashScreen() {
        viewModelScope.launch {
            delay(2000)
            _isShowingSplashScreen.update { false }
        }
    }

    fun updateToResumed() {
        _isResumed.update { true }
        _isPaused.update { false }
    }

    fun updateToPaused() {
        _isResumed.update { false }
        _isPaused.update { true }
    }

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
    val firstItemVisibleState = MutableTransitionState(true)
    val secondItemVisibleState = MutableTransitionState(false)
    val thirdItemVisibleState = MutableTransitionState(false)
    private val _firstDistance = MutableStateFlow(0f)
    val firstDistance: StateFlow<Float> = _firstDistance
    private val _secondDistance = MutableStateFlow(0f)
    val secondDistance: StateFlow<Float> = _secondDistance
    private val _thirdDistance = MutableStateFlow(0f)
    val thirdDistance: StateFlow<Float> = _thirdDistance
    private val _avgDistance = MutableStateFlow(0f)
    val avgDistance: StateFlow<Float> = _avgDistance

    fun updateIsLeftEye(isLeft: Boolean) {
        _isLeftEye.update { isLeft }
    }

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
    val visualAcuityTestContentVisibleState = MutableTransitionState(true)
    val visualAcuityTestSightednessTestContentVisibleState = MutableTransitionState(false)
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
    private val _sightLevel = MutableStateFlow(1)
    val sightLevel: StateFlow<Int> = _sightLevel
    private val _leftEyeSightValue = MutableStateFlow(1)
    val leftEyeSightValue: StateFlow<Int> = _leftEyeSightValue
    private val _rightEyeSightValue = MutableStateFlow(1)
    val rightEyeSightValue: StateFlow<Int> = _rightEyeSightValue
    private val _leftEyeSightedValue = MutableStateFlow(VisionDisorderType.Normal)
    val leftEyeSightedValue: StateFlow<VisionDisorderType> = _leftEyeSightedValue
    private val _rightEyeSightedValue = MutableStateFlow(VisionDisorderType.Normal)
    val rightEyeSightedValue: StateFlow<VisionDisorderType> = _rightEyeSightedValue
    private var _randomList = MutableStateFlow(mutableListOf(0))
    val randomList: StateFlow<MutableList<Int>> = _randomList
    private var _ansNum = MutableStateFlow(0)
    val ansNum: StateFlow<Int> = _ansNum

//    fun processCorrectAnswerSelected() {
//        _sightHistory = Pair(_sightHistory[_sightLevel]!!.first, _sightHistory[_sightLevel]!!.second)
//        }
//    }

    fun processAnswerSelected(idx: Int) {
        if(idx != 3) {
            // if correct
            if(ansNum.value == _randomList.value[idx]) {
                sightHistory[_sightLevel.value] = Pair(sightHistory[_sightLevel.value]!!.first + 1, sightHistory[_sightLevel.value]!!.second)
                // if first trial
                if(sightHistory[_sightLevel.value]!!.first == 1 && sightHistory[_sightLevel.value]!!.second == 0) {
                    // if level == 10
                    if(_sightLevel.value == 10) {
                        moveToSightednessTestContent()
                    } else {
                        _sightLevel.update { it + 1 }
                    }
                }
            } // if wrong
            else {
                sightHistory[_sightLevel.value] = Pair(sightHistory[_sightLevel.value]!!.first, sightHistory[_sightLevel.value]!!.second + 1)
            }
        } else {
            sightHistory[_sightLevel.value] = Pair(sightHistory[_sightLevel.value]!!.first, sightHistory[_sightLevel.value]!!.second + 1)
        }

        // if 5th trial
        if(sightHistory[_sightLevel.value]!!.first + sightHistory[_sightLevel.value]!!.second >= 5) {
            // if correct >= 4
            if(sightHistory[_sightLevel.value]!!.first >= 4) {
                // if next level trial >= 5
                if(sightHistory[_sightLevel.value + 1]!!.first + sightHistory[_sightLevel.value + 1]!!.second >= 5) {
                    moveToSightednessTestContent()
                } // to next level
                else {
                    _sightLevel.update { it + 1 }
                }
            } // if correct == 3
            else if(sightHistory[_sightLevel.value]!!.first == 3) {
                moveToSightednessTestContent()
            } // if correct <= 2
            else {
                // if level == 1
                if(_sightLevel.value == 1) {
                    moveToSightednessTestContent()
                } // level--
                else {
                    // if prev level trial >= 5
                    if(sightHistory[_sightLevel.value - 1]!!.first + sightHistory[_sightLevel.value - 1]!!.second >= 5) {
                        moveToSightednessTestContent()
                    } else {
                        _sightLevel.update { it - 1 }
                    }
                }
            }
        }
        updateRandomList()
    }

    private fun moveToSightednessTestContent() {
        if(_isLeftEye.value) {
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
            _leftEyeSightValue.update { _sightLevel.value }
        } else {
            _rightEyeSightValue.update { _sightLevel.value }
        }
        viewModelScope.launch {
            delay(500)
            visualAcuityTestContentVisibleState.targetState = false
            visualAcuityTestSightednessTestContentVisibleState.targetState = true
        }
    }

    fun updateLeftEyeSightedValue(type: VisionDisorderType) {
        _leftEyeSightedValue.update { type }
        visualAcuityTestContentVisibleState.targetState = true
        visualAcuityTestSightednessTestContentVisibleState.targetState = false
        _isLeftEye.update { false }
    }

    fun updateRightEyeSightedValue(type: VisionDisorderType) {
        _rightEyeSightedValue.update { type }
    }

    fun initializeVisualAcuityTest() {
        _isLeftEye.update { true }
        measuringDistanceContentVisibleState.targetState = true
        coveredEyeCheckingContentVisibleState.targetState = false
        visualAcuityTestCommonContentVisibleState.targetState = false
        visualAcuityTestContentVisibleState.targetState = true
        visualAcuityTestSightednessTestContentVisibleState.targetState = false

    }

    fun updateSightTestResult() {
        _printString.update {
            val leftEyeSighted = when(_leftEyeSightedValue.value) {
                VisionDisorderType.Normal -> "정상"
                VisionDisorderType.Myopia -> "근시"
                VisionDisorderType.Hyperopia -> "원시"
                else -> "난시"
            }

            val rightEyeSighted = when(_rightEyeSightedValue.value) {
                VisionDisorderType.Normal -> "정상"
                VisionDisorderType.Myopia -> "근시"
                VisionDisorderType.Hyperopia -> "원시"
                else -> "난시"
            }
            "좌안 시력: ${_leftEyeSightValue.value.toFloat() / 10} $leftEyeSighted," +
            "우안 시력: ${_rightEyeSightValue.value.toFloat() / 10} $rightEyeSighted"
        }
    }

    private fun updateRandomList() {
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

    fun updateLeftSelectedArea() {
        _leftSelectedArea.update { currentSelectedArea.value }
        _currentSelectedArea.update { listOf(false, false, false, false, false, false, false, false, false) }
    }

    fun updateRightSelectedArea() {
        _rightSelectedArea.update { currentSelectedArea.value }
        _currentSelectedArea.update { listOf(false, false, false, false, false, false, false, false, false) }
    }

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
    private val _savedResult = MutableStateFlow(listOf<Int>())
    val savedResult: StateFlow<List<Int>> = _savedResult
    private val _isVertical = MutableStateFlow(true)
    val isVertical: StateFlow<Boolean> = _isVertical
    private val _currentLevel = MutableStateFlow(0)
    val currentLevel: StateFlow<Int> = _currentLevel
    private val _mChartImageId = MutableStateFlow(R.drawable.mchart_0_0)
    val mChartImageId: StateFlow<Int> = _mChartImageId

    fun initializeMChartTest() {
        Log.e("initializeMChart", "initialize")
        _isVertical.update { true }
        _currentLevel.update { 0 }
        _mChartResult.update { listOf() }
        measuringDistanceContentVisibleState.targetState = true
        coveredEyeCheckingContentVisibleState.targetState = false
        mChartContentVisibleState.targetState = false
        _isLeftEye.update { true }
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

    fun updateSavedResult() {
        _savedResult.update { mChartResult.value }
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
        showSplashScreen()
        checkBackgroundStatus()
        exoPlayer.setMediaItem(MediaItem.fromUri("https://drive.google.com/uc?export=download&id=1c3khlZTVvAiqpn7hYaXw8NOI5AbK0V96"))
        exoPlayer.prepare()
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
//        exoPlayer.playWhenReady = true
    }
}