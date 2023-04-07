import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.testlist.MacularTestListContent
import com.pixelro.nenoonkiosk.ui.testlist.MainTestListContent
import com.pixelro.nenoonkiosk.ui.testlist.VisualAcuityTestListContent
import com.pixelro.nenoonkiosk.ui.testscreen.ChildrenVisualAcuityTestScreen
import com.pixelro.nenoonkiosk.ui.testscreen.LongDistanceVisualAcuityTestScreen
import com.pixelro.nenoonkiosk.ui.testscreen.ShortDistanceVisualAcuityTestScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun  TestListContent(
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
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Right,
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
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Left,
                    animationSpec = TweenSpec(durationMillis = 500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Right,
                    animationSpec = TweenSpec(durationMillis = 500)
                )
            }
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
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Left,
                    animationSpec = TweenSpec(durationMillis = 500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Right,
                    animationSpec = TweenSpec(durationMillis = 500)
                )
            }
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