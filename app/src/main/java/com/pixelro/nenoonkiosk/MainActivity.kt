package com.pixelro.nenoonkiosk

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
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

    private val settingResultRequest = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK)
            Log.d("appDebug", "Accepted")
        else {
            Log.d("appDebug", "Denied")
        }
    }

    override fun onResume() {
        super.onResume()

        val context = this
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            viewModel.updateIsBluetoothPermissionsGranted(true)
        } else {
            viewModel.updateIsBluetoothPermissionsGranted(false)
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            viewModel.updateIsCameraPermissionGranted(true)
        } else {
            viewModel.updateIsCameraPermissionGranted(false)
        }
        if(Settings.System.canWrite(context)) {
            viewModel.updateIsWriteSettingsPermissionGranted(true)
        } else {
            viewModel.updateIsWriteSettingsPermissionGranted(false)
        }
        viewModel.checkIfAllPermissionsGranted()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        val context = this
        lifecycleScope.launch {
            while(true) {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    Log.e("lifecycleScope", "not enabled")
//                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

                    val locationRequest: LocationRequest = LocationRequest.Builder(10000).build()
                    val client: SettingsClient = LocationServices.getSettingsClient(context)
                    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
                        .Builder()
                        .addLocationRequest(locationRequest)
                    val gpsSettingTask: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

                    gpsSettingTask.addOnSuccessListener {  }
                    gpsSettingTask.addOnFailureListener {
                        if(it is ResolvableApiException) {
                            try {
                                val intentSenderRequest = IntentSenderRequest
                                    .Builder(it.resolution)
                                    .build()
                                settingResultRequest.launch(intentSenderRequest)
                            } catch(sendEx: IntentSender.SendIntentException) {

                            }
                        }
                    }
                } else {
//                    Log.e("lifecycleScope", "enabled")
                }
                delay(1000)


            }
        }
        setContent {
            NenoonKioskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    val systemUiController = rememberSystemUiController()
//                    DisposableEffect(true) {
//                        systemUiController.setStatusBarColor(
//                            color = Color(0xff000000),
//                            darkIcons = false
//                        )
//                        onDispose {}
//                    }
                    NenoonApp(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

