package com.pixelro.nenoonkiosk.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.provider.Settings
import android.util.Log
import android.util.SizeF
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.ui.testscreen.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun NenoonApp(
    bluetoothAdapter: BluetoothAdapter,
    receiver: BroadcastReceiver,
    viewModel: NenoonViewModel,
    mainNavController: NavHostController = rememberAnimatedNavController(),
    subNavController: NavHostController = rememberAnimatedNavController(),
    testNavController: NavHostController = rememberAnimatedNavController()
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraCharacteristics =
        (context.getSystemService(Context.CAMERA_SERVICE) as CameraManager).getCameraCharacteristics(cameraManager.cameraIdList[1])

    viewModel.updateLocalConfigurationValues(
        pixelDensity = context.resources.displayMetrics.density,
        screenHeightDp = configuration.screenHeightDp,
        screenWidthDp = configuration.screenWidthDp,
        focalLength = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.get(0) ?: 0f,
        lensSize = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE) ?: SizeF(0f, 0f)
    )

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_SECURE_SETTINGS, Manifest.permission.WRITE_SETTINGS, Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            Settings.ACTION_MANAGE_WRITE_SETTINGS
        )
    )
    DisposableEffect(true) {
        permissionState.launchMultiplePermissionRequest()
        onDispose {

        }
    }
    Log.e("permissionState1", "${ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SECURE_SETTINGS)}")
    Log.e("permissionState2", "${ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS)}")
    Log.e("permissionState3", "${ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)}")
    Log.e("permissionState4", "${ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)}")
    Log.e("permissionState5", "${ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH)}")
    Log.e("permissionState6", "${ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)}")
    Log.e("permissionState7", "${ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)}")
    Log.e("permissionState8", "${ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)}")
    Log.e("permissionState9", "${ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)}")
    Log.e("permissionState0", "${ContextCompat.checkSelfPermission(context, Settings.ACTION_MANAGE_WRITE_SETTINGS)}")
    if(permissionState.allPermissionsGranted) {
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 3600_000)
        AnimatedNavHost(
            navController = mainNavController,
            startDestination = GlobalConstants.ROUTE_TEST_LIST,
            contentAlignment = Alignment.TopCenter
        ) {

            // 검사 선택 화면
            composable(
                route = GlobalConstants.ROUTE_TEST_LIST,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                TestListScreen(
                    toPreDescriptionScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_PRE_DESCRIPTION) },
                    navController = subNavController,
                    viewModel = viewModel
                )
            }

            // 안구 나이 검사
            composable(
                GlobalConstants.ROUTE_PRESBYOPIA_TEST,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                PresbyopiaTestScreen(
                    toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                    viewModel = viewModel
                )
            }

            // 근거리 시력 검사
            composable(
                GlobalConstants.ROUTE_SHORT_VISUAL_ACUITY_TEST,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                ShortDistanceVisualAcuityTestScreen(
                    toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                    viewModel = viewModel
                )
            }

            // 원거리 시력 검사
            composable(
                GlobalConstants.ROUTE_LONG_VISUAL_ACUITY_TEST,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                LongDistanceVisualAcuityTestScreen(
                    toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                    viewModel = viewModel)
            }

            // 어린이 시력 검사
            composable(
                GlobalConstants.ROUTE_CHILDREN_VISUAL_ACUITY_TEST,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                ChildrenVisualAcuityTestScreen(
                    toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                    viewModel = viewModel)
            }

            // 암슬러 차트 검사
            composable(
                GlobalConstants.ROUTE_AMSLER_GRID_TEST,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                AmslerGridTestScreen(
                    toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                    viewModel = viewModel)
            }

            // 엠식 변형시 검사
            composable(
                GlobalConstants.ROUTE_M_CHART_TEST,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                MChartTestScreen(
                    navController = testNavController,
                    viewModel = viewModel)
            }

            // 검사 사전 설명 페이지
            composable(
                GlobalConstants.ROUTE_TEST_PRE_DESCRIPTION,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                TestPreDescriptionScreen(
                    viewModel,
                    mainNavController
                )
            }

            // 검사 결과 화면
            composable(
                route = GlobalConstants.ROUTE_TEST_RESULT,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentScope.SlideDirection.Left,
                        animationSpec = TweenSpec(durationMillis = 500)
                    )
                }
            ) {
                TestResultScreen(
                    viewModel = viewModel,
                    navController = mainNavController
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "권한이 필요합니다.",
                fontSize = 30.sp
            )
        }
    }


}