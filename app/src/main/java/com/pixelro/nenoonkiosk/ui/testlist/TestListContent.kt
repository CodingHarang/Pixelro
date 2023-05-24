package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TestListContent(
    toPreDescriptionScreen: () -> Unit,
    navController: NavHostController,
    viewModel: NenoonViewModel
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = GlobalConstants.ROUTE_MAIN_TEST_LIST,
        contentAlignment = Alignment.TopCenter
    ) {
        // 메인 검사 리스트
        composable(
            route = GlobalConstants.ROUTE_MAIN_TEST_LIST,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition }
        ) {
            MainTestListContent(
                toPreDescriptionScreen = {
                    viewModel.updateSelectedTestType(TestType.Presbyopia)
                    toPreDescriptionScreen()
                },
                toVisualAcuityTestList = { navController.navigate(GlobalConstants.ROUTE_VISUAL_ACUITY_TEST_LIST) },
                toMacularDegenerationTestList = { navController.navigate(GlobalConstants.ROUTE_MACULAR_DEGENERATION_TEST_LIST) }
            )
        }

        // 시력 검사 리스트
        composable(
            GlobalConstants.ROUTE_VISUAL_ACUITY_TEST_LIST,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition }
        ) {
            VisualAcuityTestListContent(
                toShortDistanceVisualAcuityTest = {
                    viewModel.updateSelectedTestType(TestType.ShortDistanceVisualAcuity)
                },
                toLongDistanceVisualAcuityTest = {
                    viewModel.updateSelectedTestType(TestType.LongDistanceVisualAcuity)
                },
                toChildrenVisualAcuityTest = {
                    viewModel.updateSelectedTestType(TestType.ChildrenVisualAcuity)
                },
                toPreDescriptionScreen = toPreDescriptionScreen
            )
        }

        // 황반 변성 검사 리스트
        composable(
            GlobalConstants.ROUTE_MACULAR_DEGENERATION_TEST_LIST,
            enterTransition = { AnimationProvider.enterTransition },
            exitTransition = { AnimationProvider.exitTransition }
        ) {
            MacularTestListContent(
                toAmslerGridTest = {
                    viewModel.updateSelectedTestType(TestType.AmslerGrid)
                },
                toMChartTest = {
                    viewModel.updateSelectedTestType(TestType.MChart)
                },
                toPreDescriptionScreen = toPreDescriptionScreen
            )
        }
    }
}