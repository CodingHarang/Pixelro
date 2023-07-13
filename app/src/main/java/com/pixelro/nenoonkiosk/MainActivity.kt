package com.pixelro.nenoonkiosk

import android.annotation.SuppressLint
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.util.SizeF
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import com.pixelro.nenoonkiosk.ui.NenoonApp
import com.pixelro.nenoonkiosk.ui.theme.NenoonKioskTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: NenoonViewModel by viewModels()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        viewModel.resetScreenSaverTimer()
        return super.onTouchEvent(event)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateToResumed()
        viewModel.resetScreenSaverTimer()
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TTS.initTTS()
        if (SharedPreferencesManager.getString("language") == "") {
            SharedPreferencesManager.putString("language", "en")
            NenoonKioskApplication.applicationContext().resources.configuration.setLocale(Locale("en"))
        } else {
            when (SharedPreferencesManager.getString("language")) {
                "en" -> NenoonKioskApplication.applicationContext().resources.configuration.setLocale(
                    Locale("en")
                )

                else -> NenoonKioskApplication.applicationContext().resources.configuration.setLocale(
                    Locale("ko")
                )
            }
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val statusBarResourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val navigationBarResourceId =
            resources.getIdentifier("navigation_bar_height", "dimen", "android")
        GlobalValue.statusBarPadding = resources.getDimension(statusBarResourceId)
        GlobalValue.navigationBarPadding = 0f
        Log.e("padding", "${GlobalValue.statusBarPadding}, ${GlobalValue.navigationBarPadding}")

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        setContent {
            NenoonKioskTheme {
                Surface(
                    modifier = Modifier
                        .systemBarsPadding()
                        .fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setStatusBarColor(
                        color = Color(0x00000000)
                    )
//                    systemUiController.setNavigationBarColor(
//                        color = Color(0x00000000)
//                    )
                    systemUiController.isNavigationBarVisible = false
                    val context = LocalContext.current
                    val configuration = LocalConfiguration.current
                    LaunchedEffect(true) {
                        val cameraManager =
                            context.getSystemService(CAMERA_SERVICE) as CameraManager
                        val cameraCharacteristics =
                            (context.getSystemService(CAMERA_SERVICE) as CameraManager).getCameraCharacteristics(
                                cameraManager.cameraIdList[1]
                            )
                        viewModel.updateLocalConfigurationValues(
                            pixelDensity = context.resources.displayMetrics.density,
                            screenWidthDp = configuration.screenWidthDp,
                            screenHeightDp = configuration.screenHeightDp,
                            focalLength = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
                                ?.get(0) ?: 0f,
                            lensSize = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
                                ?: SizeF(0f, 0f)
                        )
                    }
                    NenoonApp()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateToPaused()
    }

    override fun onBackPressed() {
        viewModel.resetScreenSaverTimer()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        TTS.tts.stop()
        TTS.destroyTTS()
    }
}

