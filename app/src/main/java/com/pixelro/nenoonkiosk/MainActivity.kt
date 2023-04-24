package com.pixelro.nenoonkiosk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.ui.NenoonApp
import com.pixelro.nenoonkiosk.ui.theme.NenoonKioskTheme
import mangoslab.nemonicsdk.nemonicWrapper

class MainActivity : ComponentActivity() {

    private val viewModel: NenoonViewModel by viewModels()

    override fun onResume() {
        super.onResume()

        viewModel.checkIfAllPermissionsGranted()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContent {
            NenoonKioskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val systemUiController = rememberSystemUiController()
                    DisposableEffect(true) {
                        systemUiController.setStatusBarColor(
                            color = Color(0xff1d71e1),
                            darkIcons = false
                        )
                        onDispose {}
                    }
                    NenoonApp(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

