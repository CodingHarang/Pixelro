package com.pixelro.nenoonkiosk.ui.testscreen

import androidx.compose.runtime.Composable
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.ui.testcontent.MChartTestContent

@Composable
fun MChartTestScreen(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    viewModel.initializeMChartTest()
    FaceDetection(
        viewModel = viewModel
    )
    MChartTestContent(
        toResultScreen = toResultScreen,
        viewModel = viewModel
    )
}