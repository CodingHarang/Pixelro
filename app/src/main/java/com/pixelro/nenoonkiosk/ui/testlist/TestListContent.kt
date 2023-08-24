package com.pixelro.nenoonkiosk.ui.testlist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pixelro.nenoonkiosk.R
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
            time = 3
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
            isDone = isShortVisualAcuityDone,
            time = 2
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            modifier = modifier,
            title = "암슬러 차트 검사\n(황반 변성 검사)",
            onClickMethod = {
                if (checkIsTestDone(TestType.AmslerGrid)) {
                    showSurveyRecommendationDialog()
                } else {
                    toTestScreen(TestType.AmslerGrid)
                }
            },
            isDone = isAmslerGridDone,
            time = 2
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        EyeTestSelectableBox(
            modifier = modifier,
            title = "엠식 변형시 검사\n(황반 변성 검사)",
            onClickMethod = {
                if (checkIsTestDone(TestType.MChart)) {
                    showSurveyRecommendationDialog()
                } else {
                    toTestScreen(TestType.MChart)
                }
            },
            isDone = isMChartDone,
            time = 2
        )
    }
}