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
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.screen.*
import com.pixelro.nenoonkiosk.ui.testcontent.PresbyopiaTestContent

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NenoonApp(
    viewModel: NenoonViewModel,
    mainNavController: NavHostController = rememberAnimatedNavController(),
    subNavController: NavHostController = rememberAnimatedNavController()
) {
    val selectedTest = viewModel.selectedTestType.collectAsState().value
    if(viewModel.isShowingSplashScreen.collectAsState().value) {
        SplashScreen(
            viewModel = viewModel
        )
    }
    else {
        if(viewModel.isScreenSaverOn.collectAsState().value) {
            ScreenSaverScreen(viewModel)
        } else {
            if(!viewModel.isAllPermissionsGranted.collectAsState().value) {
                PermissionRequestScreen(
                    viewModel
                )
            } else {
//            DisposableEffect(true) {
//                Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 3600_000)
//                onDispose {  }
//            }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
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
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    initialOffset = { IntOffset(100, 0) }
                                ) + fadeIn(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            },
                            exitTransition = {
                                slideOut(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    targetOffset = { IntOffset(-100, 0) }
                                ) + fadeOut(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            }
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
                            GlobalConstants.ROUTE_SETTINGS,
                            enterTransition = {
                                slideIn(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    initialOffset = { IntOffset(100, 0) }
                                ) + fadeIn(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            },
                            exitTransition = {
                                slideOut(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    targetOffset = { IntOffset(-100, 0) }
                                ) + fadeOut(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            }
                        ) {
                            SettingsScreen(
                                viewModel = viewModel
                            )
                        }

                        // 검사 사전 설명 화면
                        composable(
                            GlobalConstants.ROUTE_TEST_PRE_DESCRIPTION,
                            enterTransition = {
                                slideIn(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    initialOffset = { IntOffset(100, 0) }
                                ) + fadeIn(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            },
                            exitTransition = {
                                slideOut(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    targetOffset = { IntOffset(-100, 0) }
                                ) + fadeOut(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            }
                        ) {
                            TestPreDescriptionScreen(
                                viewModel,
                                mainNavController
                            )
                        }

                        // 검사 화면
                        composable(
                            GlobalConstants.ROUTE_TEST_CONTENT,
                            enterTransition = {
                                slideIn(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    initialOffset = { IntOffset(100, 0) }
                                ) + fadeIn(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            },
                            exitTransition = {
                                slideOut(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    targetOffset = { IntOffset(-100, 0) }
                                ) + fadeOut(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            }
                        ) {
                            EyeTestScreen(
                                viewModel = viewModel,
                                content = {
                                    when(selectedTest) {
                                        TestType.Presbyopia -> PresbyopiaTestContent(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                        TestType.ShortDistanceVisualAcuity -> ShortDistanceVisualAcuityTestScreen(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                        TestType.LongDistanceVisualAcuity -> LongDistanceVisualAcuityTestScreen(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                        TestType.ChildrenVisualAcuity -> ChildrenVisualAcuityTestScreen(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                        TestType.AmslerGrid -> AmslerGridTestScreen(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
                                        TestType.MChart -> MChartTestScreen(
                                            toResultScreen = { mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT) },
                                            viewModel = viewModel
                                        )
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
                            enterTransition = {
                                slideIn(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    initialOffset = { IntOffset(100, 0) }
                                ) + fadeIn(
                                    animationSpec = TweenSpec(durationMillis = 500)
                                )
                            },
                            exitTransition = {
                                slideOut(
                                    animationSpec = TweenSpec(durationMillis = 500),
                                    targetOffset = { IntOffset(-100, 0) }
                                ) + fadeOut(
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
                }
            }
        }
    }
}