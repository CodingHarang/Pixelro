package com.pixelro.nenoonkiosk

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.util.SizeF
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.pixelro.nenoonkiosk.data.AccommodationData
import com.pixelro.nenoonkiosk.data.MacularDisorderType
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.data.VisionDisorderType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import kotlin.math.roundToInt

class NenoonViewModel(application: Application) : AndroidViewModel(application) {

    private fun checkBackgroundStatus() {
        viewModelScope.launch {
            while(true) {
                if(_isResumed.value) {
                    // Check screen saver timer
                    _screenSaverTimer.update { _screenSaverTimer.value - 1 }
                    Log.e("screenSaver", "${_screenSaverTimer.value}")
                    if(_screenSaverTimer.value < 0) {
                        _isScreenSaverOn.update { true }
                    }
                    // Check permissions
                    checkPermissions()
                    checkIsLocationOn()
                    checkIsBluetoothOn()
                }
                delay(1000)
            }
        }
    }

    // Settings
    private val _isLanguageSelectDialogShowing = MutableStateFlow(false)
    val isLanguageSelectDialogShowing: StateFlow<Boolean> = _isLanguageSelectDialogShowing

    fun updateIsLanguageSelectDialogShowing(isShowing: Boolean) {
        _isLanguageSelectDialogShowing.update { isShowing }
    }

    fun updateLanguage(language: String) {
        SharedPreferencesManager.putString("language", language)
        getApplication<Application>().applicationContext.resources.configuration.setLocale(Locale(language))
        _isLanguageSelectDialogShowing.update { false }
    }

    // Screen Saver
    private val _isResumed = MutableStateFlow(false)
    private val _isPaused = MutableStateFlow(false)
    val exoPlayer = ExoPlayer.Builder(application.applicationContext).build()
    private val _screenSaverTimer = MutableStateFlow(30)
    private val _timeValue = MutableStateFlow(30)
//    val screenSaverTimer: StateFlow<Int> = _screenSaverTimer
    private val _isScreenSaverOn = MutableStateFlow(false)
    val isScreenSaverOn: StateFlow<Boolean> = _isScreenSaverOn

    fun resetScreenSaverTimer() {
        _screenSaverTimer.update { _timeValue.value }
        _isScreenSaverOn.update { false }
    }

    fun updateScreenSaverTimerValue(time: Int) {
        _timeValue.update { time }
        resetScreenSaverTimer()
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
//    private val _bitmap = MutableStateFlow(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
//    val bitmap: StateFlow<Bitmap> = _bitmap

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
                val prev = _screenToFaceDistance.value
                val dist = 1.1f * (focalLength.value * 63) * inputImageSizeX.value / ((rightEyePosition.value.x - leftEyePosition.value.x) * (lensSize.value.width))
                if(dist > 600f || dist < 1f) prev
                else dist
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
        measuringDistanceContentVisibleState.targetState = false
        when(_selectedTestType.value) {
            TestType.ShortDistanceVisualAcuity -> { coveredEyeCheckingContentVisibleState.targetState = true }
            TestType.LongDistanceVisualAcuity -> { coveredEyeCheckingContentVisibleState.targetState = true }
            TestType.ChildrenVisualAcuity -> { coveredEyeCheckingContentVisibleState.targetState = true }
            TestType.AmslerGrid -> { coveredEyeCheckingContentVisibleState.targetState = true }
            else -> { coveredEyeCheckingContentVisibleState.targetState = true }
        }
    }

    // Covered Eye Checking
    val coveredEyeCheckingContentVisibleState = MutableTransitionState(false)
    private val _leftTime = MutableStateFlow(0f)
    val leftTime: StateFlow<Float> = _leftTime
    private val _isTimerShowing = MutableStateFlow(false)
    val isTimerShowing: StateFlow<Boolean> = _isTimerShowing

    fun initializeCoveredEyeChecking() {
//        _isCoveredEyeCheckingDone.update { false }
        _leftTime.update { 5f }
        _isTimerShowing.update { false }
    }

//    fun updateIsCoveredEyeCheckingDone(isDone: Boolean) {
//        _isCoveredEyeCheckingDone.update { isDone }
//    }

    fun checkCoveredEye() {

        viewModelScope.launch {
            var count = 0
            _leftTime.update { 5.5f }
            while(count < 10) {
                delay(500)
                if(!coveredEyeCheckingContentVisibleState.targetState) {
                    return@launch
                }
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
                    _leftTime.update { 5f }
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
    private val _statusBarPadding = MutableStateFlow(0)
    val statusBarPadding: StateFlow<Int> = _statusBarPadding
    private val _navigationBarPadding = MutableStateFlow(0)
    val navigationBarPadding: StateFlow<Int> = _navigationBarPadding

    fun updateSystemBarsPadding(statusBar: Float, navigationBar: Float) {
        _statusBarPadding.update { (statusBar / getApplication<Application>().resources.displayMetrics.density).toInt() }
        _navigationBarPadding.update { navigationBar.toInt() }
    }

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
    private val _isBluetoothOn = MutableStateFlow(false)
    val isBlueToothOn: StateFlow<Boolean> = _isBluetoothOn
    private val _resolvableApiException = MutableStateFlow(ResolvableApiException(Status.RESULT_CANCELED))
    val resolvableApiException: StateFlow<ResolvableApiException> = _resolvableApiException

    private fun checkPermissions() {
        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _isBluetoothPermissionsGranted.update { true }
        } else {
            _isBluetoothPermissionsGranted.update { false }
        }
        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            _isCameraPermissionGranted.update { true }
        } else {
            _isCameraPermissionGranted.update { false }
        }
        if(Settings.System.canWrite(getApplication())) {
            _isWriteSettingsPermissionGranted.update { true }
        } else {
            _isWriteSettingsPermissionGranted.update { false }
        }

        if(_isBluetoothPermissionsGranted.value && _isCameraPermissionGranted.value && _isWriteSettingsPermissionGranted.value && _isLocationOn.value && _isBluetoothOn.value) {
            _isAllPermissionsGranted.update { true }
        } else {
            _isAllPermissionsGranted.update { false }
        }
    }

    private fun checkIsLocationOn() {
        val locationManager = getSystemService(getApplication(), LocationManager::class.java) as LocationManager

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            val locationRequest: LocationRequest = LocationRequest.Builder(10000).build()
            val client: SettingsClient = LocationServices.getSettingsClient(getApplication() as Context)
            val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
            val gpsSettingTask: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            gpsSettingTask.addOnSuccessListener {
            }
            gpsSettingTask.addOnFailureListener { exception ->
                if(exception is ResolvableApiException) {
                    try {
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

    private fun checkIsBluetoothOn() {
        val bluetoothAdapter = (getApplication<Application>().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if(bluetoothAdapter.isEnabled) {
            _isBluetoothOn.update { true }
        } else {
            _isBluetoothOn.update { false }
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
    private val _predescriptionTitle = MutableStateFlow("")
    val predescriptionTitle: StateFlow<String> = _predescriptionTitle

    private fun showSplashScreen() {
        viewModelScope.launch {
            delay(3000)
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
                TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_name1)
                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_name)
                TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_name)
                TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_name)
                TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_name)
                else -> StringProvider.getString(R.string.mchart_name)
            }
        }
        _selectedTestDescription.update {
            when(testType) {
                TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_long_description)
                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_long_description)
                TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_long_description)
                TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_long_desciption)
                TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_long_description)
                else -> StringProvider.getString(R.string.mchart_long_description)
            }
        }
        _predescriptionTitle.update {
            when(testType) {
                TestType.Presbyopia -> StringProvider.getString(R.string.test_predescription_presbyopia_title)
                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.test_predescription_short_visual_acuity_title)
                TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.test_predescription_long_visual_acuity_title)
                TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.test_predescription_children_visual_acuity_title)
                TestType.AmslerGrid -> StringProvider.getString(R.string.test_predescription_amsler_title)
                else -> StringProvider.getString(R.string.test_predescription_mchart_title)
            }
        }
    }

    // Presbyopia test
    val firstItemVisibleState = MutableTransitionState(true)
    val secondItemVisibleState = MutableTransitionState(false)
    val thirdItemVisibleState = MutableTransitionState(false)
    private val _firstDistance = MutableStateFlow(0f)
    private val _secondDistance = MutableStateFlow(0f)
    private val _thirdDistance = MutableStateFlow(0f)
    private val _avgDistance = MutableStateFlow(0f)
    val avgDistance: StateFlow<Float> = _avgDistance
    private val _eyeAge = MutableStateFlow(0)
    val eyeAge: StateFlow<Int> = _eyeAge

    fun updateIsLeftEye(isLeft: Boolean) {
        _isLeftEye.update { isLeft }
    }

    fun initializePresbyopiaTest() {
//        _bitmap.update {
//        Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
//    }
        firstItemVisibleState.targetState = true
        secondItemVisibleState.targetState = false
        thirdItemVisibleState.targetState = false
    }

    fun updateFirstDistance() {
        _firstDistance.update { screenToFaceDistance.value }
        firstItemVisibleState.targetState = false
        secondItemVisibleState.targetState = true
        thirdItemVisibleState.targetState = false
    }

    fun updateSecondDistance() {
        _secondDistance.update { screenToFaceDistance.value }
        firstItemVisibleState.targetState = false
        secondItemVisibleState.targetState = false
        thirdItemVisibleState.targetState = true
    }

    fun updateThirdDistance() {
        _thirdDistance.update { screenToFaceDistance.value }
        updateAvgDistance()
    }

    private fun updateAvgDistance() {
        _avgDistance.update {
            (_firstDistance.value + _secondDistance.value + _thirdDistance.value) / 3
        }
        _printString.update {
            "조절근점: ${(avgDistance.value).roundToInt().toFloat() / 10}cm"
        }

        var max = 100000f
        for(entry in AccommodationData.allEntries) {
            var diff = (entry.x * 10) - _avgDistance.value
            if(diff < 0) diff = -diff
            if(max > diff) {
                max = diff
                _eyeAge.update { entry.y.toInt() - 15 }
            }
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

    fun processAnswerSelected(
        idx: Int,
        toResultScreen: () -> Unit
    ) {
        var isEnd = false
        if(idx != 3) {
            // if correct
            if(ansNum.value == _randomList.value[idx]) {
                sightHistory[_sightLevel.value] = Pair(sightHistory[_sightLevel.value]!!.first + 1, sightHistory[_sightLevel.value]!!.second)
                // if first trial
                if(sightHistory[_sightLevel.value]!!.first == 1 && sightHistory[_sightLevel.value]!!.second == 0) {
                    // if level == 10
                    if(_sightLevel.value == 10) {
                        isEnd = true
                        moveToRightVisualAcuityTest(toResultScreen)
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

        // if 3th trial
        if(sightHistory[_sightLevel.value]!!.first + sightHistory[_sightLevel.value]!!.second >= 3) {
            // if correct >= 2
            if(sightHistory[_sightLevel.value]!!.first >= 2) {
                // if next level trial >= 3
                if(sightHistory[_sightLevel.value + 1]!!.first + sightHistory[_sightLevel.value + 1]!!.second >= 3) {
                    isEnd = true
                    moveToRightVisualAcuityTest(toResultScreen)
                } // to next level
                else {
                    _sightLevel.update { it + 1 }
                }
            } // if correct < 1
            else {
                // if level == 1
                if(_sightLevel.value == 1) {
                    isEnd = true
                    moveToRightVisualAcuityTest(toResultScreen)
                } // level--
                else {
                    // if prev level trial >= 3
                    if(sightHistory[_sightLevel.value - 1]!!.first + sightHistory[_sightLevel.value - 1]!!.second >= 3) {
                        isEnd = true
                        moveToRightVisualAcuityTest(toResultScreen)
                    } else {
                        _sightLevel.update { it - 1 }
                    }
                }
            }
        }
        if(!isEnd) updateRandomList()
    }

    private fun moveToRightVisualAcuityTest(
        toResultScreen: () -> Unit
    ) {
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
            delay(700)
            _sightLevel.update { 1 }
        }
        visualAcuityTestCommonContentVisibleState.targetState = false
        coveredEyeCheckingContentVisibleState.targetState = true
        if(!_isLeftEye.value) { toResultScreen() }
        else { _isLeftEye.update { false } }
    }

    fun updateLeftEyeSightedValue(type: VisionDisorderType) {
        viewModelScope.launch {
            delay(700)
            visualAcuityTestContentVisibleState.targetState = true
            visualAcuityTestSightednessTestContentVisibleState.targetState = false
        }
        _leftEyeSightedValue.update { type }
    }

    fun updateRightEyeSightedValue(type: VisionDisorderType) {
        _rightEyeSightedValue.update { type }
    }

    fun initializeVisualAcuityTest() {
//        _bitmap.update {
//            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
//        }
        _isLeftEye.update { true }
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
            "${_leftEyeSightValue.value.toFloat() / 10} $leftEyeSighted," +
            "${_rightEyeSightValue.value.toFloat() / 10} $rightEyeSighted"
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
    val macularDistortedTypeVisibleState = MutableTransitionState(false)
    private val _currentSelectedPosition = MutableStateFlow(Offset(0f, 0f))
    val currentSelectedPosition: StateFlow<Offset> = _currentSelectedPosition
    private val _currentSelectedArea = MutableStateFlow(listOf(MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal))
    val currentSelectedArea: StateFlow<List<MacularDisorderType>> = _currentSelectedArea
    private val _leftSelectedArea = MutableStateFlow(emptyList<MacularDisorderType>())
    val leftSelectedArea: StateFlow<List<MacularDisorderType>> = _leftSelectedArea
    private val _rightSelectedArea = MutableStateFlow(emptyList<MacularDisorderType>())
    val rightSelectedArea: StateFlow<List<MacularDisorderType>> = _rightSelectedArea

    fun updateLeftSelectedArea() {
        _leftSelectedArea.update { currentSelectedArea.value }
        _currentSelectedArea.update { listOf(MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal) }
        _printString.update {
            _leftSelectedArea.value.toString()
        }
    }

    fun updateRightSelectedArea() {
        _rightSelectedArea.update { currentSelectedArea.value }
        _currentSelectedArea.update { listOf(MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal) }
        _printString.update {
            (it + _rightSelectedArea.value.toString()).replace("][", ",").replace("[", "").replace("]", "").replace(" ", "")
        }
    }

    fun initializeAmslerGridTest() {
//        _bitmap.update {
//            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
//        }
        _currentSelectedArea.update { listOf(MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal, MacularDisorderType.Normal) }
        measuringDistanceContentVisibleState.targetState = true
        coveredEyeCheckingContentVisibleState.targetState = false
        amslerGridContentVisibleState.targetState = false
        _isLeftEye.update { true }
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
                macularDistortedTypeVisibleState.targetState = true
            }
        } else if(position.x in 300f..599f && position.y in 0f..299f) {
            if(_currentSelectedArea.value[1] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[1] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
            }
        } else if(position.x in 600f..900f && position.y in 0f..299f) {
            if(_currentSelectedArea.value[2] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[2] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
            }
        } else if(position.x in 0f..299f && position.y in 300f..599f) {
            if(_currentSelectedArea.value[3] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[3] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
            }
        } else if(position.x in 300f..599f && position.y in 300f..599f) {
            if(_currentSelectedArea.value[4] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[4] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
            }
        } else if(position.x in 600f..900f && position.y in 300f..599f) {
            if(_currentSelectedArea.value[5] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[5] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
            }
        } else if(position.x in 0f..299f && position.y in 600f..900f) {
            if(_currentSelectedArea.value[6] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[6] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
            }
        } else if(position.x in 300f..599f && position.y in 600f..900f) {
            if(_currentSelectedArea.value[7] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[7] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
            }
        } else {
            if(_currentSelectedArea.value[8] != MacularDisorderType.Normal) {
                _currentSelectedArea.update {
                    val tmpList = it.toMutableList()
                    tmpList[8] = MacularDisorderType.Normal
                    tmpList
                }
            } else {
                macularDistortedTypeVisibleState.targetState = true
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
//        _bitmap.update {
//            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
//        }
        _isVertical.update { true }
        _currentLevel.update { 0 }
        _mChartResult.update { listOf() }
        measuringDistanceContentVisibleState.targetState = true
        coveredEyeCheckingContentVisibleState.targetState = false
        mChartContentVisibleState.targetState = false
        _isLeftEye.update { true }
    }

    fun toNextMChartTest() {
        updateIsLeftEye(false)
        viewModelScope.launch {
            delay(700)
            updateCurrentLevel(0)
            updateIsVertical(true)
        }
    }

    fun updateMChartResult(value: Int) {
        _mChartResult.update {
            if(value == 0) {
                val list = it + value
                list
            } else {
                val list = it + (value + 1)
                list
            }
        }
        if(_mChartResult.value.size == 4) {
            _printString.update {
                _mChartResult.value.toString().replace("[", "").replace("]", "").replace(" ", "")
            }
        }
    }

    fun updateIsVertical(isVertical: Boolean) {
        _isVertical.update { isVertical }
    }

    fun updateSavedResult() {
        _savedResult.update { mChartResult.value }
        viewModelScope.launch {
            delay(700)
            updateCurrentLevel(0)
        }
    }

    fun updateCurrentLevel(level: Int) {
        _currentLevel.update { level }
        _mChartImageId.update {
            when(currentLevel.value) {
                0 -> R.drawable.mchart_0_0
                1 -> R.drawable.mchart_0_2
                2 -> R.drawable.mchart_0_3
                3 -> R.drawable.mchart_0_4
                4 -> R.drawable.mchart_0_5
                5 -> R.drawable.mchart_0_6
                6 -> R.drawable.mchart_0_7
                7 -> R.drawable.mchart_0_8
                8 -> R.drawable.mchart_0_9
                9 -> R.drawable.mchart_1_0
                10 -> R.drawable.mchart_1_1
                11 -> R.drawable.mchart_1_2
                12 -> R.drawable.mchart_1_3
                13 -> R.drawable.mchart_1_4
                14 -> R.drawable.mchart_1_5
                15 -> R.drawable.mchart_1_6
                16 -> R.drawable.mchart_1_7
                17 -> R.drawable.mchart_1_8
                18 -> R.drawable.mchart_1_9
                else -> R.drawable.mchart_2_0
            }
        }
    }

    init {
        updateRandomList()
        showSplashScreen()
        checkBackgroundStatus()
//        exoPlayer.setMediaItem(MediaItem.fromUri("https://drive.google.com/uc?export=view&id=1vNW4Xia8pG4tfGoao4Nb7hEJtOd9Cg8F"))
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.fromFile(File("/storage/emulated/0/Download/ad1.mp4"))))
        exoPlayer.prepare()
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
//        exoPlayer.playWhenReady = true
    }
}