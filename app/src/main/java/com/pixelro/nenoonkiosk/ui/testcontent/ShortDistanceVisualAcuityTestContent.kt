package com.pixelro.nenoonkiosk.ui.testcontent

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun ShortDistanceVisualAcuityTestContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    Column(
        modifier = Modifier
            .background(Color(0xff000000)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val measuringDistanceContentVisibleState = remember { MutableTransitionState(true) }
        val coveredEyeCheckingContentVisibleState = remember { MutableTransitionState(false) }
        val visualAcuityTestCommonContentVisibleState = remember { MutableTransitionState(false) }
        val fourthVisibleState = remember { MutableTransitionState(false) }

        Text(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp),
            text = "근거리 시력 검사",
            color = Color(0xffffffff),
            fontSize = 40.sp
        )
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            MeasuringDistanceContent(
                viewModel = viewModel,
                measuringDistanceContentVisibleState = measuringDistanceContentVisibleState,
                nextVisibleState = coveredEyeCheckingContentVisibleState
            )
            CoveredEyeCheckingContent(
                coveredEyeCheckingContentVisibleState = coveredEyeCheckingContentVisibleState,
                nextVisibleState = visualAcuityTestCommonContentVisibleState
            )
            VisualAcuityTestCommonContent(
                toResultScreen = toResultScreen,
                viewModel = viewModel,
                visualAcuityTestCommonContentVisibleState = visualAcuityTestCommonContentVisibleState,
                fourthVisibleState = fourthVisibleState
            )
        }

    }
}