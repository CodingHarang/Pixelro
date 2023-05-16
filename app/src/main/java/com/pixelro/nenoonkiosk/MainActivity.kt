package com.pixelro.nenoonkiosk

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.SizeF
import android.view.MotionEvent
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.pixelro.nenoonkiosk.ui.NenoonApp
import com.pixelro.nenoonkiosk.ui.theme.NenoonKioskTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: NenoonViewModel by viewModels()

    private var isChecking = true

//    private val settingResultRequest = registerForActivityResult(
//        ActivityResultContracts.StartIntentSenderForResult()
//    ) { activityResult ->
//        if (activityResult.resultCode == RESULT_OK)
//            Log.d("appDebug", "Accepted")
//        else {
//            Log.d("appDebug", "Denied")
//        }
//        isChecking = true
//    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        viewModel.resetScreenSaverTimer()
        return super.onTouchEvent(event)
    }

    override fun onResume() {
        Log.e("onResume", "Resumed")
        super.onResume()
        viewModel.updateToResumed()
        viewModel.resetScreenSaverTimer()
//        viewModel.startScreenSaverTimer()
//        val context = this
//        if(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            viewModel.updateIsBluetoothPermissionsGranted(true)
//        } else {
//            viewModel.updateIsBluetoothPermissionsGranted(false)
//        }
//        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            viewModel.updateIsCameraPermissionGranted(true)
//        } else {
//            viewModel.updateIsCameraPermissionGranted(false)
//        }
//        if(Settings.System.canWrite(context)) {
//            viewModel.updateIsWriteSettingsPermissionGranted(true)
//        } else {
//            viewModel.updateIsWriteSettingsPermissionGranted(false)
//        }
//        viewModel.checkIfAllPermissionsGranted()

        lifecycleScope.launch {
//            while(isChecking) {
//                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
////                    Log.e("lifecycleScope", "not enabled")
////                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//
//                    val locationRequest: LocationRequest = LocationRequest.Builder(10000).build()
//                    val client: SettingsClient = LocationServices.getSettingsClient(context)
//                    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
//                        .Builder()
//                        .addLocationRequest(locationRequest)
//                    val gpsSettingTask: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
//
//                    gpsSettingTask.addOnSuccessListener {  }
//                    gpsSettingTask.addOnFailureListener {
//                        if(it is ResolvableApiException) {
//                            try {
//                                val intentSenderRequest = IntentSenderRequest
//                                    .Builder(it.resolution)
//                                    .build()
//                                isChecking = false
//                                settingResultRequest.launch(intentSenderRequest)
//                            } catch(sendEx: IntentSender.SendIntentException) {
//
//                            }
//                        }
//                    }
//                } else {
////                    Log.e("lifecycleScope", "enabled")
//                }
//                delay(1000)
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.navigationBarColor = 0x00000000

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightNavigationBars = true


//        WindowCompat.getInsetsController(window, window.decorView).apply {
//            hide(WindowInsetsCompat.Type.statusBars())
//            hide(WindowInsetsCompat.Type.navigationBars())
//            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        applicationContext
        setContent {
            NenoonKioskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                        .navigationBarsPadding(),
                    color = MaterialTheme.colors.background
                ) {
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setStatusBarColor(
                        color = Color(0x00000000),
                        darkIcons = true
                    )
                    val context = LocalContext.current
                    val configuration = LocalConfiguration.current
                    DisposableEffect(true) {
                        val cameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
                        val cameraCharacteristics =
                            (context.getSystemService(CAMERA_SERVICE) as CameraManager).getCameraCharacteristics(cameraManager.cameraIdList[1])
                        viewModel.updateLocalConfigurationValues(
                            pixelDensity = context.resources.displayMetrics.density,
                            screenWidthDp = configuration.screenWidthDp,
                            screenHeightDp = configuration.screenHeightDp,
                            focalLength = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.get(0) ?: 0f,
                            lensSize = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE) ?: SizeF(0f, 0f)
                        )
                        onDispose {}
                    }
//                    systemUiController.isSystemBarsVisible = false
//                    DisposableEffect(true) {
//                        onDispose {}
//                    }
                    NenoonApp(
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    override fun onPause() {
        Log.e("Paused", "Paused")
        super.onPause()
        viewModel.updateToPaused()
    }
}

