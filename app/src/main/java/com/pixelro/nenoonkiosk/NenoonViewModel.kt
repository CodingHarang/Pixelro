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
import android.widget.Toast
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
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.test.dementia.DementiaData
import com.pixelro.nenoonkiosk.test.dementia.DementiaViewModel
import com.pixelro.nenoonkiosk.login.LoginData
import com.pixelro.nenoonkiosk.survey.SurveyData
import com.pixelro.nenoonkiosk.test.dementia.DementiaTestResult
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridTestResult
import com.pixelro.nenoonkiosk.test.macular.mchart.MChartTestResult
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.children.ChildrenVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.longdistance.LongVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.shortdistance.ShortVisualAcuityTestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import javax.inject.Inject

@SuppressLint("HardwareIds")
@HiltViewModel
class NenoonViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private fun checkBackgroundStatus() {
        viewModelScope.launch(CoroutineName("checkBackgroundStatus")) {
            while (true) {
                if (_isResumed.value) {
                    // Check screen saver timer

//                    Log.e("threadName", Thread.currentThread().name)
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
    private val _inputSignInPassword = MutableStateFlow("")
    val inputSignInPassword: StateFlow<String> = _inputSignInPassword

    fun updateInputSignInId(id: String) {
        _inputSignInId.update { id }
    }
    fun updateInputSignInPassword(id: String) {
        _inputSignInPassword.update { id }
    }

    fun checkLoginIsDone(): Boolean {
        if (inputSignInId.value == "") {
            Toast.makeText(getApplication(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        if (inputSignInPassword.value == "") {
            Toast.makeText(getApplication(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun retrieveLoginData(): LoginData {
        return LoginData(
            id = inputSignInId.value,
            password = inputSignInPassword.value,

            )
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

//        val screenSaverTimer: StateFlow<Int> = _screenSaverTimer
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

    // Login Data
    var loginData = LoginData()

//    var dementiaData = DementiaData(scores = mapOf(DementiaScore.One to true, DementiaScore.Two to false))
    var dementiaData = DementiaData(
        scores = List(14) { DementiaViewModel.DementiaAnswer.None }
    )

    // Survey Data
    var surveyData = SurveyData()

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
        when(testType) {
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
    var dementiaTestResult = DementiaTestResult(scores = List(14) { DementiaViewModel.DementiaAnswer.None })

    init {
//        viewModelScope.launch {
//            val response = api.getTest()
//            Log.e(
//                "response",
//                "code: ${response.code()}\nbody: ${response.body()}\nerrorbody: ${response.errorBody()}\n"
//            )
//        }
        showSplashScreen()
        checkBackgroundStatus()
//        exoPlayer.setMediaItem(MediaItem.fromUri("https://drive.google.com/uc?export=view&id=1vNW4Xia8pG4tfGoao4Nb7hEJtOd9Cg8F"))
        exoPlayer.setMediaItem(MediaItem.fromUri(Uri.fromFile(File("/storage/emulated/0/Download/ad1.mp4"))))
        exoPlayer.prepare()
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
//        exoPlayer.playWhenReady = true
        viewModelScope.launch {
//            val context = getApplication<Application>().applicationContext
//            val intent = context.packageManager.getLaunchIntentForPackage("com.teamviewer.host.market")
//            context.startActivity(intent)
            Log.e("serial", Build.SERIAL)
        }
    }
}