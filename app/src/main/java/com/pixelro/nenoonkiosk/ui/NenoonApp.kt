package com.pixelro.nenoonkiosk.ui

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.provider.Settings
import android.util.SizeF
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.testscreen.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NenoonApp(
    viewModel: NenoonViewModel,
    mainNavController: NavHostController = rememberAnimatedNavController(),
    subNavController: NavHostController = rememberAnimatedNavController()
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    val cameraCharacteristics =
        (context.getSystemService(Context.CAMERA_SERVICE) as CameraManager).getCameraCharacteristics(cameraManager.cameraIdList[1])

    viewModel.updateLocalConfigurationValues(
        pixelDensity = context.resources.displayMetrics.density,
        screenWidthDp = configuration.screenWidthDp,
        screenHeightDp = configuration.screenHeightDp,
        focalLength = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)?.get(0) ?: 0f,
        lensSize = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE) ?: SizeF(0f, 0f)
    )
    if(viewModel.isLaunching.collectAsState().value) {
        SplashScreen(
            viewModel = viewModel
        )
    }
    else {
        if(!viewModel.isAllPermissionsGranted.collectAsState().value) {
            PermissionRequestScreen(
                viewModel
            )
        } else {
            DisposableEffect(true) {
                Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 3600_000)
                onDispose {  }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            color = when (viewModel.selectedTestType.collectAsState().value) {
                                TestType.None -> Color(0xff000000)
                                else -> Color(0xff000000)
                            }
                        )
                        .statusBarsPadding()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .width(12.dp)
                            )
                            Image(
                                modifier = Modifier
                                    .height(40.dp),
                                painter = painterResource(id = R.drawable.nenoon_logo),
                                contentDescription = ""
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(12.dp)
                            )
                            Text(
                                text = "내눈",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xffffffff),
                                fontSize = 30.sp
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row() {
                            Image(
                                modifier = Modifier
                                    .height(40.dp),
                                painter = painterResource(id = R.drawable.pixelro_logo),
                                contentDescription = ""
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(12.dp)
                            )
                        }
                    }
                }

                AnimatedNavHost(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = mainNavController,
                    startDestination = GlobalConstants.ROUTE_TEST_LIST,
                    contentAlignment = Alignment.TopCenter
                ) {

                    // 검사 선택 화면
                    composable(
                        route = GlobalConstants.ROUTE_TEST_LIST,
                        enterTransition = {
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
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
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
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
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
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
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
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
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
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
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        }
                    ) {
                        AmslerGridTestScreen(
                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                            viewModel = viewModel
                        )
                    }

                    // 엠식 변형시 검사
                    composable(
                        GlobalConstants.ROUTE_M_CHART_TEST,
                        enterTransition = {
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        }
                    ) {
                        MChartTestScreen(
                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                            viewModel = viewModel
                        )
                    }

                    // 검사 사전 설명 페이지
                    composable(
                        GlobalConstants.ROUTE_TEST_PRE_DESCRIPTION,
                        enterTransition = {
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
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
                            slideIn(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                initialOffset = { IntOffset(100, 0) }
                            ) + fadeIn(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        },
                        exitTransition = {
                            slideOut(
                                animationSpec = TweenSpec(durationMillis = 1000),
                                targetOffset = { IntOffset(-100, 0) }
                            ) + fadeOut(
                                animationSpec = TweenSpec(durationMillis = 1000)
                            )
                        }
                    ) {
                        TestResultScreen(
                            viewModel = viewModel,
                            navController = mainNavController
                        )
                    }
                }
            }
        }
    }
}