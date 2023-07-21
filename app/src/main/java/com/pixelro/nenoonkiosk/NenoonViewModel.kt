package com.pixelro.nenoonkiosk

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.util.SizeF
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PackageManagerCompat
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
import com.harang.domain.model.SendPresbyopiaTestResultRequest
import com.harang.domain.model.SendSurveyDataRequest
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.survey.SurveyAge
import com.pixelro.nenoonkiosk.survey.SurveyData
import com.pixelro.nenoonkiosk.survey.SurveyDiabetes
import com.pixelro.nenoonkiosk.survey.SurveyGlass
import com.pixelro.nenoonkiosk.survey.SurveySex
import com.pixelro.nenoonkiosk.survey.SurveySurgery
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridTestResult
import com.pixelro.nenoonkiosk.test.macular.mchart.MChartTestResult
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.children.ChildrenVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.longdistance.LongVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.shortdistance.ShortVisualAcuityTestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.time.LocalDateTime
import java.util.Locale
import javax.inject.Inject

@SuppressLint("HardwareIds")
@HiltViewModel
class NenoonViewModel @Inject constructor(
    application: Application,
//    private val api: NenoonKioskApi
) : AndroidViewModel(application) {

    private fun checkBackgroundStatus() {
        viewModelScope.launch(CoroutineName("checkBackgroundStatus")) {
            while (true) {
                if (_isResumed.value) {
                    // Check screen saver timer
                    _screenSaverTimer.update { _screenSaverTimer.value - 0 }

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
    private val _screenSaverTimer = MutableStateFlow(40)
    private val _timeValue = MutableStateFlow(40)

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
    }

    // Survey Data

    private val _surveyId = MutableStateFlow(0L)
    val surveyId: StateFlow<Long> = _surveyId

//    fun updateSurveyData(surveyData: SurveyData) {
//        viewModelScope.launch {
//            val request = SendSurveyDataRequest(
//                age = when (surveyData.surveyAge) {
//                    SurveyAge.First -> 9
//                    SurveyAge.Second -> 10
//                    SurveyAge.Third -> 20
//                    SurveyAge.Fourth -> 30
//                    SurveyAge.Fifth -> 40
//                    SurveyAge.Sixth -> 50
//                    SurveyAge.Seventh -> 60
//                    else -> 70
//                },
//                gender = when (surveyData.surveySex) {
//                    SurveySex.Man -> "M"
//                    else -> "W"
//                },
//                glasses = when (surveyData.surveyGlass) {
//                    SurveyGlass.Yes -> true
//                    else -> false
//                },
//                surgery = when (surveyData.surveySurgery) {
//                    SurveySurgery.Normal -> "normal"
//                    SurveySurgery.LASIK -> "correction"
//                    SurveySurgery.Cataract -> "cataract"
//                    else -> "etc"
//                },
//                diabetes = when (surveyData.surveyDiabetes) {
//                    SurveyDiabetes.Yes -> true
//                    else -> false
//                },
//                createAt = LocalDateTime.now().toString()
//            )
//            Log.e("surveyDataRequest", request.toString())
//            val response = try {
//                api.sendSurveyData(request)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            } catch (e: HttpException) {
//                e.printStackTrace()
//                null
//            }
//            Log.e("surveyDataResponse", response?.body().toString())
//            if (response != null) {
//                _surveyId.update { ((response.body()?.data?.get("tid") ?: 0) as Double).toLong() }
//            } else {
//                _surveyId.update { 0L }
//            }
//        }
//    }

    // Test Result
    private val _isPresbyopiaTestDone = MutableStateFlow(false)
    val isPresbyopiaTestDone: StateFlow<Boolean> = _isPresbyopiaTestDone
    private val _isShortVisualAcuityTestDone = MutableStateFlow(false)
    val isShortVisualAcuityTestDone: StateFlow<Boolean> = _isShortVisualAcuityTestDone
    private val _isAmslerGridTestDone = MutableStateFlow(false)
    val isAmslerGridTestDone: StateFlow<Boolean> = _isAmslerGridTestDone
    private val _isMChartTestDone = MutableStateFlow(false)
    val isMChartTestDone: StateFlow<Boolean> = _isMChartTestDone

    fun updateIsPresbyopiaTestDone(isDone: Boolean) {
        _isPresbyopiaTestDone.update { isDone }
    }

    fun updateIsShortVisualAcuityTestDone(isDone: Boolean) {
        _isShortVisualAcuityTestDone.update { isDone }
    }

    fun updateIsAmslerGridTestDone(isDone: Boolean) {
        _isAmslerGridTestDone.update { isDone }
    }

    fun updateIsMChartTestDone(isDone: Boolean) {
        _isMChartTestDone.update { isDone }
    }

    fun initializeTestDoneStatus() {
        _isPresbyopiaTestDone.update { false }
        _isShortVisualAcuityTestDone.update { false }
        _isAmslerGridTestDone.update { false }
        _isMChartTestDone.update { false }
    }

    fun checkIsTestDone(testType: TestType): Boolean {
        when (testType) {
            TestType.Presbyopia -> {
                return _isPresbyopiaTestDone.value
            }

            TestType.ShortDistanceVisualAcuity -> {
                return _isShortVisualAcuityTestDone.value
            }

            TestType.AmslerGrid -> {
                return _isAmslerGridTestDone.value
            }

            TestType.MChart -> {
                return _isMChartTestDone.value
            }

            else -> {
                return false
            }
        }
    }

    var presbyopiaTestResult = PresbyopiaTestResult()
    var shortVisualAcuityTestResult = ShortVisualAcuityTestResult()
    var longVisualAcuityTestResult = LongVisualAcuityTestResult()
    var childrenVisualAcuityTestResult = ChildrenVisualAcuityTestResult()
    var amslerGridTestResult = AmslerGridTestResult()
    var mChartTestResult = MChartTestResult()

    init {
        showSplashScreen()
        checkBackgroundStatus()
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.fromFile(File("/storage/emulated/0/Download/ad1.mp4"))))
        exoPlayer.prepare()
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.volume = 0f
    }
}