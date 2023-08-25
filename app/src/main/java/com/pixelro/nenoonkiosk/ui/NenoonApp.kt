package com.pixelro.nenoonkiosk.ui

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.Constants
import com.pixelro.nenoonkiosk.data.TestType
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NenoonApp(
    viewModel: NenoonViewModel = hiltViewModel(),
    navController: NavHostController = rememberAnimatedNavController()
) {
    val selectedTest = viewModel.selectedTestType.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val isAllConditionsGranted = viewModel.isAllPermissionsGranted.collectAsState().value
    val isScreenSaving = viewModel.isScreenSaving.collectAsState().value

    LaunchedEffect(isAllConditionsGranted, isScreenSaving) {
        if (isAllConditionsGranted) {
            navController.popBackStack(Constants.ROUTE_PERMISSION, true)
        } else {
            if (navController.currentBackStackEntry?.destination?.route != Constants.ROUTE_SPLASH) {
                navController.navigate(Constants.ROUTE_PERMISSION)
            }
        }

//        if (isScreenSaving) {
//            if (navController.currentBackStackEntry?.destination?.route != Constants.ROUTE_SIGN_IN) {
//                navController.popBackStack(Constants.ROUTE_INTRO, false)
//                navController.navigate(Constants.ROUTE_SCREEN_SAVER)
//            }
//        } else {
//            navController.popBackStack(Constants.ROUTE_SCREEN_SAVER, true)
//        }
    }

    AnimatedNavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = Constants.ROUTE_SPLASH,
        contentAlignment = Alignment.TopCenter
    ) {
        /**
         * 스플래시 화면
        */
        composable(
            route = Constants.ROUTE_SPLASH,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            LaunchedEffect(true) {
                coroutineScope.launch {
                    delay(3000)
                    navController.popBackStack()
                    navController.navigate(Constants.ROUTE_SIGN_IN)
                }
            }
            SplashScreen()
        }

        /**
        * 로그인 화면
        */
        composable(
            route = Constants.ROUTE_SIGN_IN,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            SignInScreen(
                updateIsSignedIn = {
                    viewModel.updateIsSignedIn(it)
                    navController.popBackStack()
                    navController.navigate(Constants.ROUTE_INTRO)
                }
            )
        }

        /**
        * 화면 보호기 화면
        */
        composable(
            route = Constants.ROUTE_SCREEN_SAVER,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            ScreenSaverScreen(
                exoPlayer = viewModel.exoPlayer,
                isSignedIn = viewModel.isSignedIn.collectAsState().value,
                initializeTestDoneStatus = {
                    viewModel.initializeTestDoneStatus()
                }
            )
        }

        /**
        * 권한 확인 화면
        */
        composable(
            route = Constants.ROUTE_PERMISSION,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            PermissionRequestScreen(viewModel)
        }

        /**
         * 첫 시작 화면
         */
        composable(
            route = Constants.ROUTE_INTRO,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            IntroScreen(
                toSurveyScreen = {
                    navController.navigate(Constants.ROUTE_SURVEY)
                },
                toSettingsScreen = {
                    navController.navigate(Constants.ROUTE_SETTINGS)
                }
            )
        }

        /**
         * 문진표 작성 화면
         */
        composable(
            route = Constants.ROUTE_SURVEY,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            SurveyScreen(
                toTestListScreen = {
                    navController.navigate(Constants.ROUTE_TEST_LIST)
                    viewModel.initializeTestDoneStatus()
                    viewModel.updateSurveyData(it)
                }
            )
        }

        /**
         * 검사 목록 화면
         */
        composable(
            route = Constants.ROUTE_TEST_LIST,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            TestListScreen(
                checkIsTestDone = viewModel::checkIsTestDone,
                toTestScreen = {
                    viewModel.updateSelectedTestType(it)
                    navController.navigate(Constants.ROUTE_TEST_CONTENT)
                },
                toIntroScreen = {
                    navController.popBackStack(Constants.ROUTE_INTRO, false)
                },
                isPresbyopiaDone = viewModel.isPresbyopiaTestDone.collectAsState().value,
                isShortVisualAcuityDone = viewModel.isShortVisualAcuityTestDone.collectAsState().value,
                isAmslerGridDone = viewModel.isAmslerGridTestDone.collectAsState().value,
                isMChartDone = viewModel.isMChartTestDone.collectAsState().value
            )
        }

        /**
         * 설정 화면
         */
        composable(
            route = Constants.ROUTE_SETTINGS,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            SettingsScreen(
                viewModel = viewModel,
                isSignedIn = viewModel.isSignedIn.collectAsState().value,
                toSignInScreen = {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate(Constants.ROUTE_SIGN_IN)
                },
                toSoftwareInfoScreen = {
                    navController.navigate(Constants.ROUTE_SOFTWARE_INFO)
                }
            )
        }

        /**
         * 검사 화면
         */
        composable(
            route = Constants.ROUTE_TEST_CONTENT,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            TestScreen(
                viewModel = viewModel,
                navController = navController,
                content = {
                    when (selectedTest) {
                        TestType.Presbyopia -> {
                            PresbyopiaTestContent(
                                toResultScreen = {
                                    navController.navigate(Constants.ROUTE_TEST_RESULT)
                                    viewModel.presbyopiaTestResult = it
                                }
                            )
                        }

                        TestType.ShortDistanceVisualAcuity -> {
                            ShortDistanceVisualAcuityTestContent(
                                toResultScreen = {
                                    navController.navigate(Constants.ROUTE_TEST_RESULT)
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
                                    navController.navigate(Constants.ROUTE_TEST_RESULT)
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
                                    navController.navigate(Constants.ROUTE_TEST_RESULT)
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
                                    navController.navigate(Constants.ROUTE_TEST_RESULT)
                                    viewModel.amslerGridTestResult = it
                                },
                            )
                        }

                        TestType.MChart -> {
                            MChartTestContent(
                                toResultScreen = {
                                    navController.navigate(Constants.ROUTE_TEST_RESULT)
                                    viewModel.mChartTestResult = it
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

        /**
         * 검사 결과 화면
         */
        composable(
            route = Constants.ROUTE_TEST_RESULT,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            when (viewModel.selectedTestType.collectAsState().value) {
                TestType.Presbyopia -> viewModel.updateIsPresbyopiaTestDone(true)
                TestType.ShortDistanceVisualAcuity -> viewModel.updateIsShortVisualAcuityTestDone(true)
                TestType.AmslerGrid -> viewModel.updateIsAmslerGridTestDone(true)
                TestType.MChart -> viewModel.updateIsMChartTestDone(true)
                else -> {

                }
            }
            val surveyId = viewModel.surveyId.collectAsState().value
            TestResultScreen(
                surveyId = surveyId,
                testType = viewModel.selectedTestType.collectAsState().value,
                testResult = when (
                    viewModel.selectedTestType.collectAsState().value) {
                    TestType.Presbyopia -> viewModel.presbyopiaTestResult
                    TestType.ShortDistanceVisualAcuity -> viewModel.shortVisualAcuityTestResult
                    TestType.LongDistanceVisualAcuity -> viewModel.longVisualAcuityTestResult
                    TestType.ChildrenVisualAcuity -> viewModel.childrenVisualAcuityTestResult
                    TestType.AmslerGrid -> viewModel.amslerGridTestResult
                    TestType.MChart -> viewModel.mChartTestResult
                    TestType.None -> null
                },
                navController = navController,
            )
        }

        composable(
            route = Constants.ROUTE_SOFTWARE_INFO,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition },
            popEnterTransition = { AnimationProvider.popEnterTransition },
            popExitTransition = { AnimationProvider.popExitTransition }
        ) {
            SoftwareInfoScreen()
        }
    }
}