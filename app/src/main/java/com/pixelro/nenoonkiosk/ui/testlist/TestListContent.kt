package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TestListContent(
    checkIsTestDone: (TestType) -> Boolean,
    showSurveyRecommendationDialog: () -> Unit,
    toTestScreen: (TestType) -> Unit,
    isPresbyopiaDone: Boolean,
    isShortVisualAcuityDone: Boolean,
    isAmslerGridDone: Boolean,
    isMChartDone: Boolean,
) {
//    val isPresbyopiaExpanded = remember { mutableStateOf(false) }
//    val isShortVisualAcuityExpanded = remember { mutableStateOf(false) }
//    val isAmslerChartExpanded = remember { mutableStateOf(false) }
//    val isMChartExpanded = remember { mutableStateOf(false) }
    EyeTestSelectableBox(
        title = StringProvider.getString(R.string.presbyopia_name1),
        description = StringProvider.getString(R.string.presbyopia_short_description),
        onClickMethod = {
            if (checkIsTestDone(TestType.Presbyopia)) {
                showSurveyRecommendationDialog()
            } else {
                toTestScreen(TestType.Presbyopia)
            }
//            isPresbyopiaExpanded.value = !isPresbyopiaExpanded.value
        },
        isDone = isPresbyopiaDone,
//        expanded = isPresbyopiaExpanded.value
    )
    Spacer(
        modifier = Modifier
            .height(20.dp)
    )
    EyeTestSelectableBox(
        title = StringProvider.getString(R.string.short_visual_acuity_name),
        description = StringProvider.getString(R.string.short_visual_acuity_short_description),
        onClickMethod = {
            if (checkIsTestDone(TestType.ShortDistanceVisualAcuity)) {
                showSurveyRecommendationDialog()
            } else {
                toTestScreen(TestType.ShortDistanceVisualAcuity)
            }
        },
        isDone = isShortVisualAcuityDone
//        expanded = isShortVisualAcuityExpanded.value
    )
    Spacer(
        modifier = Modifier
            .height(20.dp)
    )
    EyeTestSelectableBox(
        title = StringProvider.getString(R.string.amsler_grid_name),
        description = StringProvider.getString(R.string.amsler_grid_short_description),
        onClickMethod = {
            if (checkIsTestDone(TestType.AmslerGrid)) {
                showSurveyRecommendationDialog()
            } else {
                toTestScreen(TestType.AmslerGrid)
            }
        },
        isDone = isAmslerGridDone
//        expanded = isAmslerChartExpanded.value
    )
    Spacer(
        modifier = Modifier
            .height(20.dp)
    )
    EyeTestSelectableBox(
        title = StringProvider.getString(R.string.mchart_name),
        description = StringProvider.getString(R.string.mchart_short_description),
        onClickMethod = {
            if (checkIsTestDone(TestType.MChart)) {
                showSurveyRecommendationDialog()
            } else {
                toTestScreen(TestType.MChart)
            }
        },
        isDone = isMChartDone
//        expanded = isMChartExpanded.value
    )

//    AnimatedNavHost(
//        navController = navController,
//        startDestination = GlobalConstants.ROUTE_MAIN_TEST_LIST,
//        contentAlignment = Alignment.TopCenter
//    ) {
//        // 메인 검사 리스트
//        composable(
//            route = GlobalConstants.ROUTE_MAIN_TEST_LIST,
//            enterTransition = { AnimationProvider.enterTransition },
//            exitTransition = { AnimationProvider.exitTransition }
//        ) {
//            MainTestListContent(
//                toTestScreen = {
//                    viewModel.updateSelectedTestType(TestType.Presbyopia)
////                    viewModel.initializePresbyopiaTest()
//                    toTestScreen(it)
//                },
//                toVisualAcuityTestList = { navController.navigate(GlobalConstants.ROUTE_VISUAL_ACUITY_TEST_LIST) },
//                toMacularDegenerationTestList = { navController.navigate(GlobalConstants.ROUTE_MACULAR_DEGENERATION_TEST_LIST) }
//            )
//        }
//
//        // 시력 검사 리스트
//        composable(
//            GlobalConstants.ROUTE_VISUAL_ACUITY_TEST_LIST,
//            enterTransition = { AnimationProvider.enterTransition },
//            exitTransition = { AnimationProvider.exitTransition }
//        ) {
//            VisualAcuityTestListContent(
//                toTestScreen = {
//                    toTestScreen(it)
//                }
//            )
//        }
//
//        // 황반 변성 검사 리스트
//        composable(
//            GlobalConstants.ROUTE_MACULAR_DEGENERATION_TEST_LIST,
//            enterTransition = { AnimationProvider.enterTransition },
//            exitTransition = { AnimationProvider.exitTransition }
//        ) {
//            MacularTestListContent(
//                toTestScreen = {
//                    toTestScreen(it)
//                }
//            )
//        }
//    }
}