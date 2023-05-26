package com.pixelro.nenoonkiosk.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.screen.*
import com.pixelro.nenoonkiosk.ui.testcontent.AmslerGridTestContent
import com.pixelro.nenoonkiosk.ui.testcontent.ChildrenVisualAcuityTestContent
import com.pixelro.nenoonkiosk.ui.testcontent.LongDistanceVisualAcuityTestContent
import com.pixelro.nenoonkiosk.ui.testcontent.MChartTestContent
import com.pixelro.nenoonkiosk.ui.testcontent.PresbyopiaTestContent
import com.pixelro.nenoonkiosk.ui.testcontent.ShortDistanceVisualAcuityTestContent

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NenoonApp(
    viewModel: NenoonViewModel,
    mainNavController: NavHostController = rememberAnimatedNavController(),
    subNavController: NavHostController = rememberAnimatedNavController()
) {
    val selectedTest = viewModel.selectedTestType.collectAsState().value
    // Splash Screen
    if(viewModel.isShowingSplashScreen.collectAsState().value) {
        SplashScreen(
            viewModel = viewModel
        )
    } else {
        // Splash Screen
        if(viewModel.isScreenSaverOn.collectAsState().value) {
            ScreenSaverScreen(viewModel)
        } else {
            // Permission Request Screen
            if(!viewModel.isAllPermissionsGranted.collectAsState().value) {
                PermissionRequestScreen(
                    viewModel
                )
            } // Main Content
            else {
//            DisposableEffect(true) {
//                Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 3600_000)
//                onDispose {  }
//            }
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
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        TestListScreen(
                            toPreDescriptionScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_PRE_DESCRIPTION) },
                            toSettingsScreen = { mainNavController.navigate(GlobalConstants.ROUTE_SETTINGS) },
                            navController = subNavController,
                            viewModel = viewModel
                        )
                    }

                    // 설정 화면
                    composable(
                        route = GlobalConstants.ROUTE_SETTINGS,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        SettingsScreen(
                            viewModel = viewModel
                        )
                    }

                    // 검사 사전 설명 화면
                    composable(
                        route = GlobalConstants.ROUTE_TEST_PRE_DESCRIPTION,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        TestPreDescriptionScreen(
                            viewModel,
                            mainNavController
                        )
                    }

                    // 검사 화면
                    composable(
                        route = GlobalConstants.ROUTE_TEST_CONTENT,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        EyeTestScreen(
                            viewModel = viewModel,
                            content = {
                                when(selectedTest) {
                                    TestType.Presbyopia -> {
                                        viewModel.initializePresbyopiaTest()
                                        PresbyopiaTestContent(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                    }
                                    TestType.ShortDistanceVisualAcuity -> {
                                        viewModel.initializeVisualAcuityTest()
                                        ShortDistanceVisualAcuityTestContent(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                    }
                                    TestType.LongDistanceVisualAcuity -> {
                                        viewModel.initializeVisualAcuityTest()
                                        LongDistanceVisualAcuityTestContent(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                    }
                                    TestType.ChildrenVisualAcuity -> {
                                        viewModel.initializeVisualAcuityTest()
                                        ChildrenVisualAcuityTestContent(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                    }
                                    TestType.AmslerGrid -> {
                                        viewModel.initializeAmslerGridTest()
                                        AmslerGridTestContent(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                    }
                                    TestType.MChart -> {
                                        viewModel.initializeMChartTest()
                                        MChartTestContent(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                    }
                                    else -> {
                                        Box() {

                                        }
                                    }
                                }
                            }
                        )
                    }

                    // 검사 결과 화면
                    composable(
                        route = GlobalConstants.ROUTE_TEST_RESULT,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
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