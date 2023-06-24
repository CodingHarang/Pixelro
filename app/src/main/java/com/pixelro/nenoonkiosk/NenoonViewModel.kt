package com.pixelro.nenoonkiosk

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.util.SizeF
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.ui.geometry.Offset
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
import com.harang.data.api.NenoonKioskApi
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridTestResult
import com.pixelro.nenoonkiosk.data.AccommodationData
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.test.macular.amslergrid.MacularDisorderType
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.SurveyAge
import com.pixelro.nenoonkiosk.data.SurveyDiabetes
import com.pixelro.nenoonkiosk.data.SurveyGlass
import com.pixelro.nenoonkiosk.data.SurveySex
import com.pixelro.nenoonkiosk.data.SurveySurgery
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.data.VisionDisorderType
import com.pixelro.nenoonkiosk.test.macular.mchart.MChartTestResult
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.children.ChildrenVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.longdistance.LongVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.shortdistance.ShortVisualAcuityTestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class NenoonViewModel @Inject constructor(
    application: Application,
    private val api: NenoonKioskApi
) : AndroidViewModel(application) {

    private fun checkBackgroundStatus() {
        viewModelScope.launch {
            while (true) {
                if (_isResumed.value) {
                    // Check screen saver timer
                    _screenSaverTimer.update { _screenSaverTimer.value - 0 }
//                    Log.e("screenSaver", "${_screenSaverTimer.value}")
                    if (_screenSaverTimer.value < 0) {
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

    // signIn
    private val _inputSignInId = MutableStateFlow("")
    val inputSignInId: StateFlow<String> = _inputSignInId

    fun updateInputSignInId(id: String) {
        _inputSignInId.update { id }
    }

    // Survey
    private val _surveyAge = MutableStateFlow(SurveyAge.None)
    val surveyAge: StateFlow<SurveyAge> = _surveyAge
    private val _surveySex = MutableStateFlow(SurveySex.None)
    val surveySex: StateFlow<SurveySex> = _surveySex
    private val _surveyGlass = MutableStateFlow(SurveyGlass.None)
    val surveyGlass: StateFlow<SurveyGlass> = _surveyGlass
    private val _surveySurgery = MutableStateFlow(SurveySurgery.None)
    val surveySurgery: StateFlow<SurveySurgery> = _surveySurgery
    private val _surveyDiabetes = MutableStateFlow(SurveyDiabetes.None)
    val surveyDiabetes: StateFlow<SurveyDiabetes> = _surveyDiabetes

    fun updateSurveyAge(type: SurveyAge) {
        _surveyAge.update { type }
    }

    fun updateSurveySex(type: SurveySex) {
        _surveySex.update { type }
    }

    fun updateSurveyGlass(type: SurveyGlass) {
        _surveyGlass.update { type }
    }

    fun updateSurveySurgery(type: SurveySurgery) {
        _surveySurgery.update { type }
    }

    fun updateSurveyDiabetes(type: SurveyDiabetes) {
        _surveyDiabetes.update { type }
    }

    fun submitSurvey() {

    }

    // Settings
    private val _isLanguageSelectDialogShowing = MutableStateFlow(false)
    val isLanguageSelectDialogShowing: StateFlow<Boolean> = _isLanguageSelectDialogShowing

    fun updateIsLanguageSelectDialogShowing(isShowing: Boolean) {
        _isLanguageSelectDialogShowing.update { isShowing }
    }

    fun updateLanguage(language: String) {
        SharedPreferencesManager.putString("language", language)
        getApplication<Application>().resources.configuration.setLocale(Locale(language))
        _isLanguageSelectDialogShowing.update { false }
    }

    // Screen Saver
    private val _isResumed = MutableStateFlow(false)
    private val _isPaused = MutableStateFlow(false)
    val exoPlayer = ExoPlayer.Builder(getApplication()).build()
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

    fun updateLocalConfigurationValues(
        pixelDensity: Float,
        screenWidthDp: Int,
        screenHeightDp: Int,
        focalLength: Float,
        lensSize: SizeF
    ) {
        GlobalValue.pixelDensity = pixelDensity
        GlobalValue.screenWidthDp = screenWidthDp
        GlobalValue.screenHeightDp = screenHeightDp
        GlobalValue.focalLength = focalLength
        GlobalValue.lensSize = lensSize
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
            while (count < 10) {
                delay(500)
                if (!coveredEyeCheckingContentVisibleState.targetState) {
                    return@launch
                }
                if (
//                    when(isLeftEye.value) {
//                        true -> leftEyeOpenProbability.value
//                        else -> rightEyeOpenProbability.value
//                    } < 0.7f && abs(leftEyeOpenProbability.value - rightEyeOpenProbability.value) > 0.3f
                    true
                ) {
                    if (!isTimerShowing.value) {
                        _isTimerShowing.update { true }
                    }
                    count++
                    _leftTime.update { (it - 0.5f) }
                } else {
                    if (isTimerShowing.value) {
                        _isTimerShowing.update { false }
                    }
                    count = 0
                    _leftTime.update { 5f }
                }
            }
            coveredEyeCheckingContentVisibleState.targetState = false
            if (_selectedTestType.value == TestType.ShortDistanceVisualAcuity) {
//                visualAcuityTestCommonContentVisibleState.targetState = true
            } else if (_selectedTestType.value == TestType.AmslerGrid) {
//                amslerGridContentVisibleState.targetState = true
            } else if (_selectedTestType.value == TestType.MChart) {
//                mChartContentVisibleState.targetState = true
            }
        }
    }

    // Nemonic Printer


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
    private val _resolvableApiException =
        MutableStateFlow(ResolvableApiException(Status.RESULT_CANCELED))
    val resolvableApiException: StateFlow<ResolvableApiException> = _resolvableApiException

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.BLUETOOTH_ADMIN
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            _isBluetoothPermissionsGranted.update { true }
        } else {
            _isBluetoothPermissionsGranted.update { false }
        }
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            _isCameraPermissionGranted.update { true }
        } else {
            _isCameraPermissionGranted.update { false }
        }
        if (Settings.System.canWrite(getApplication())) {
            _isWriteSettingsPermissionGranted.update { true }
        } else {
            _isWriteSettingsPermissionGranted.update { false }
        }

        if (_isBluetoothPermissionsGranted.value && _isCameraPermissionGranted.value && _isWriteSettingsPermissionGranted.value && _isLocationOn.value && _isBluetoothOn.value) {
            _isAllPermissionsGranted.update { true }
        } else {
            _isAllPermissionsGranted.update { false }
        }
    }

    private fun checkIsLocationOn() {
        val locationManager =
            getSystemService(getApplication(), LocationManager::class.java) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            val locationRequest: LocationRequest = LocationRequest.Builder(10000).build()
            val client: SettingsClient =
                LocationServices.getSettingsClient(getApplication() as Context)
            val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest)
            val gpsSettingTask: Task<LocationSettingsResponse> =
                client.checkLocationSettings(builder.build())

            gpsSettingTask.addOnSuccessListener {
            }
            gpsSettingTask.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        _resolvableApiException.update { exception }
                        _isLocationOn.update { false }
                    } catch (sendEx: IntentSender.SendIntentException) {

                    }
                }
            }
        } else {
            _isLocationOn.update { true }
        }
    }

    private fun checkIsBluetoothOn() {
        val bluetoothAdapter =
            (getApplication<Application>().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if (bluetoothAdapter.isEnabled) {
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
            when (testType) {
                TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_name1)
                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_name)
                TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_name)
                TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_name)
                TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_name)
                else -> StringProvider.getString(R.string.mchart_name)
            }
        }
        _selectedTestDescription.update {
            when (testType) {
                TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_long_description)
                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_long_description)
                TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_long_description)
                TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_long_desciption)
                TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_long_description)
                else -> StringProvider.getString(R.string.mchart_long_description)
            }
        }
        _predescriptionTitle.update {
            when (testType) {
                TestType.Presbyopia -> StringProvider.getString(R.string.test_predescription_presbyopia_title)
                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.test_predescription_short_visual_acuity_title)
                TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.test_predescription_long_visual_acuity_title)
                TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.test_predescription_children_visual_acuity_title)
                TestType.AmslerGrid -> StringProvider.getString(R.string.test_predescription_amsler_title)
                else -> StringProvider.getString(R.string.test_predescription_mchart_title)
            }
        }
    }

    // Test Result
    var presbyopiaTestResult = PresbyopiaTestResult()
    var shortVisualAcuityTestResult = ShortVisualAcuityTestResult()
    var longVisualAcuityTestResult = LongVisualAcuityTestResult()
    var childrenVisualAcuityTestResult = ChildrenVisualAcuityTestResult()
    var amslerGridTestResult = AmslerGridTestResult()
    var mChartTestResult = MChartTestResult()


    fun updateIsLeftEye(isLeft: Boolean) {
        _isLeftEye.update { isLeft }
    }

    init {
        viewModelScope.launch {
            val response = api.getTest()
            Log.e(
                "response",
                "code: ${response.code()}\nbody: ${response.body()}\nerrorbody: ${response.errorBody()}\n"
            )
        }
//        updateRandomList()
        showSplashScreen()
        checkBackgroundStatus()
//        exoPlayer.setMediaItem(MediaItem.fromUri("https://drive.google.com/uc?export=view&id=1vNW4Xia8pG4tfGoao4Nb7hEJtOd9Cg8F"))
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.fromFile(File("/storage/emulated/0/Download/ad1.mp4"))))
        exoPlayer.prepare()
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
//        exoPlayer.playWhenReady = true
    }
}