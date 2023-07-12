package com.pixelro.nenoonkiosk.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
//import com.pixelro.nenoonkiosk.test.dementia.DementiaResultScreen
import com.pixelro.nenoonkiosk.test.dementia.DementiaTestContent
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridTestContent
import com.pixelro.nenoonkiosk.test.macular.mchart.MChartTestContent
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaTestContent
import com.pixelro.nenoonkiosk.test.visualacuity.children.ChildrenVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.longdistance.LongVisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.shortdistance.ShortDistanceVisualAcuityTestContent
import com.pixelro.nenoonkiosk.test.visualacuity.shortdistance.ShortVisualAcuityTestResult
import com.pixelro.nenoonkiosk.ui.screen.*
import com.pixelro.nenoonkiosk.ui.testcontent.ChildrenVisualAcuityTestContent
import com.pixelro.nenoonkiosk.ui.testcontent.LongDistanceVisualAcuityTestContent

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NenoonApp(
    viewModel: NenoonViewModel = hiltViewModel(),
    mainNavController: NavHostController = rememberAnimatedNavController()
) {
    val selectedTest = viewModel.selectedTestType.collectAsState().value
    // Splash Screen
    if (viewModel.isShowingSplashScreen.collectAsState().value) {
        SplashScreen(
            viewModel = viewModel
        )
    } else {
        // Screen Saver
        if (viewModel.isScreenSaverOn.collectAsState().value) {
            ScreenSaverScreen(
                viewModel,
                toSurveyScreen = {
                    mainNavController.popBackStack(GlobalConstants.ROUTE_SURVEY, false)
                }
            )
        } else {
            // Permission Request Screen
            if (!viewModel.isAllPermissionsGranted.collectAsState().value) {
                PermissionRequestScreen(viewModel)
            } // Main Content
            else {
                AnimatedNavHost(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = mainNavController,

                    startDestination = GlobalConstants.ROUTE_PRIMARY_TEST_LIST,
//                    startDestination = GlobalConstants.ROUTE_SURVEY,
//                    startDestination = GlobalConstants.ROUTE_LOGIN,

                    contentAlignment = Alignment.TopCenter
                ) {

                    //초기 선택 화면
                    composable(
                        route = GlobalConstants.ROUTE_PRIMARY_TEST_LIST,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        BodyListScreen(
                            toSettingsScreen = {
                                mainNavController.navigate(GlobalConstants.ROUTE_SETTINGS)
                            },
                            toSurveyScreen = {
                                mainNavController.navigate(GlobalConstants.ROUTE_SURVEY)
                            },
                            toDementiaScreen = {
                                viewModel.updateSelectedTestType(TestType.Dementia)
                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_CONTENT)
                            },
                        )
                    }

                    //로그인 화면
                    composable(
                        route = GlobalConstants.ROUTE_LOGIN,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
//                            navBackStackEntry ->
                        LoginScreen(

                            toSurveyScreen = {
                                mainNavController.navigate(GlobalConstants.ROUTE_SURVEY)
                            },
                            viewModel = viewModel
                        )
                    }

                    // 문진표 작성 화면
                    composable(
                        route = GlobalConstants.ROUTE_SURVEY,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        SurveyScreen(
                            toPrimaryScreen = {
                                mainNavController.navigate(GlobalConstants.ROUTE_PRIMARY_TEST_LIST)
                            },
                            toTestListScreen = {
                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_LIST)
                                viewModel.initializeTestDoneStatus()
                                viewModel.surveyData = it
                            }
                        )
                    }

                    // 검사 선택 화면
                    composable(
                        route = GlobalConstants.ROUTE_TEST_LIST,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        TestListScreen(
                            checkIsTestDone = viewModel::checkIsTestDone,
                            toTestScreen = {
                                viewModel.updateSelectedTestType(it)
                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_CONTENT)
                            },
                            toSettingsScreen = {
                                mainNavController.navigate(GlobalConstants.ROUTE_SETTINGS)
                            },
                            toSurveyScreen = {
                                mainNavController.popBackStack(GlobalConstants.ROUTE_SURVEY, false)
                            },

                            isPresbyopiaDone = viewModel.isPresbyopiaTestDone.collectAsState().value,
                            isShortVisualAcuityDone = viewModel.isShortVisualAcuityTestDone.collectAsState().value,
                            isAmslerGridDone = viewModel.isAmslerGridTestDone.collectAsState().value,
                            isMChartDone = viewModel.isMChartTestDone.collectAsState().value
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

                    // 검사 화면
                    composable(
                        route = GlobalConstants.ROUTE_TEST_CONTENT,
                        enterTransition = { AnimationProvider.enterTransition },
                        exitTransition = { AnimationProvider.exitTransition }
                    ) {
                        TestScreen(
                            viewModel = viewModel,
                            navController = mainNavController,
                            content = {
                                when (selectedTest) {
                                    TestType.Presbyopia -> {
                                        PresbyopiaTestContent(
                                            toResultScreen = {
                                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT)
                                                viewModel.presbyopiaTestResult = it
                                            }
                                        )
                                    }

                                    TestType.ShortDistanceVisualAcuity -> {
                                        ShortDistanceVisualAcuityTestContent(
                                            toResultScreen = {
                                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT)
                                                viewModel.shortVisualAcuityTestResult =
                                                    ShortVisualAcuityTestResult(
                                                        it.leftEye,
                                                        it.rightEye
                                                    )
                                            }
                                        )
                                    }

                                    TestType.LongDistanceVisualAcuity -> {
                                        LongDistanceVisualAcuityTestContent(
                                            toResultScreen = {
                                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT)
                                                viewModel.longVisualAcuityTestResult =
                                                    LongVisualAcuityTestResult(
                                                        it.leftEye,
                                                        it.rightEye
                                                    )
                                            }
                                        )
                                    }

                                    TestType.ChildrenVisualAcuity -> {
                                        ChildrenVisualAcuityTestContent(
                                            toResultScreen = {
                                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT)
                                                viewModel.childrenVisualAcuityTestResult =
                                                    ChildrenVisualAcuityTestResult(
                                                        it.leftEye,
                                                        it.rightEye
                                                    )
                                            }
                                        )
                                    }

                                    TestType.AmslerGrid -> {
                                        AmslerGridTestContent(
                                            toResultScreen = {
                                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT)
                                                viewModel.amslerGridTestResult = it
                                            },
                                        )
                                    }

                                    TestType.MChart -> {
                                        MChartTestContent(
                                            toResultScreen = {
                                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT)
                                                viewModel.mChartTestResult = it
                                            }
                                        )
                                    }

                                    TestType.Dementia -> {
                                        DementiaTestContent(
                                            toBackScreen = {
                                                mainNavController.popBackStack(GlobalConstants.ROUTE_PRIMARY_TEST_LIST, false)
                                            },
                                            toResultScreen = {
                                                mainNavController.navigate(GlobalConstants.ROUTE_TEST_RESULT)
                                                viewModel.dementiaTestResult = it
                                            }
                                        )
                                    }

                                    TestType.None -> {
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
                        when (viewModel.selectedTestType.collectAsState().value) {
                            TestType.Presbyopia -> viewModel.updateIsPresbyopiaTestDone(true)
                            TestType.ShortDistanceVisualAcuity -> viewModel.updateIsShortVisualAcuityTestDone(
                                true
                            )

                            TestType.AmslerGrid -> viewModel.updateIsAmslerGridTestDone(true)
                            TestType.MChart -> viewModel.updateIsMChartTestDone(true)
                            else -> {
                            }
                        }
                        TestResultScreen(
                            testType = viewModel.selectedTestType.collectAsState().value,
                            testResult = when (
                                viewModel.selectedTestType.collectAsState().value) {
                                TestType.Presbyopia -> viewModel.presbyopiaTestResult
                                TestType.ShortDistanceVisualAcuity -> viewModel.shortVisualAcuityTestResult
                                TestType.LongDistanceVisualAcuity -> viewModel.longVisualAcuityTestResult
                                TestType.ChildrenVisualAcuity -> viewModel.childrenVisualAcuityTestResult
                                TestType.AmslerGrid -> viewModel.amslerGridTestResult
                                TestType.MChart -> viewModel.mChartTestResult
                                TestType.Dementia -> viewModel.dementiaTestResult
                                TestType.None -> null
                            },
                            navController = mainNavController,
                        )
                    }
                }
            }
        }
    }
}

