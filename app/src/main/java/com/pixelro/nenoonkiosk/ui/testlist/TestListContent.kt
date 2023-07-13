package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Column
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
    Column(
        modifier = Modifier
            .padding(bottom = 120.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
        EyeTestSelectableBox(
            modifier = modifier,
            title = "노안 조절력 검사\n(안구 나이 검사)",
            onClickMethod = {
                if (checkIsTestDone(TestType.Presbyopia)) {
                    showSurveyRecommendationDialog()
                } else {
                    toTestScreen(TestType.Presbyopia)
                }
            },
            isDone = isPresbyopiaDone,
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            modifier = modifier,
            title = StringProvider.getString(R.string.short_visual_acuity_name),
            onClickMethod = {
                if (checkIsTestDone(TestType.ShortDistanceVisualAcuity)) {
                    showSurveyRecommendationDialog()
                } else {
                    toTestScreen(TestType.ShortDistanceVisualAcuity)
                }
            },
            isDone = isShortVisualAcuityDone
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            modifier = modifier,
            title = StringProvider.getString(R.string.amsler_grid_name),
            onClickMethod = {
                if (checkIsTestDone(TestType.AmslerGrid)) {
                    showSurveyRecommendationDialog()
                } else {
                    toTestScreen(TestType.AmslerGrid)
                }
            },
            isDone = isAmslerGridDone
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            modifier = modifier,
            title = StringProvider.getString(R.string.mchart_name),
            onClickMethod = {
                if (checkIsTestDone(TestType.MChart)) {
                    showSurveyRecommendationDialog()
                } else {
                    toTestScreen(TestType.MChart)
                }
            },
            isDone = isMChartDone
        )
    }
}