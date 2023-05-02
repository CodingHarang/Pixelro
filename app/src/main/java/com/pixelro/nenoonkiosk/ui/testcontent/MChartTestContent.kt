package com.pixelro.nenoonkiosk.ui.testcontent

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun MChartTestContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    val measuringDistanceContentVisibleState = viewModel.measuringDistanceContentVisibleState
    val coveredEyeCheckingContentVisibleState = viewModel.coveredEyeCheckingContentVisibleState
    val mChartContentVisibleState = viewModel.mChartContentVisibleState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff000000)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp),
            text = "엠식 변형시 검사",
            color = Color(0xffffffff),
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MeasuringDistanceContent(
                viewModel = viewModel,
                measuringDistanceContentVisibleState = measuringDistanceContentVisibleState,
                nextVisibleState = coveredEyeCheckingContentVisibleState
            )
            CoveredEyeCheckingContent(
                viewModel = viewModel,
                coveredEyeCheckingContentVisibleState = coveredEyeCheckingContentVisibleState,
                nextVisibleState = mChartContentVisibleState
            )
            MChartContent(
                viewModel = viewModel,
                mChartContentVisibleState = mChartContentVisibleState,
                nextVisibleState = coveredEyeCheckingContentVisibleState,
                toResultScreen = toResultScreen
            )
        }
    }
}

@Composable
fun MChartContent(
    viewModel: NenoonViewModel,
    mChartContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: () -> Unit
) {

}