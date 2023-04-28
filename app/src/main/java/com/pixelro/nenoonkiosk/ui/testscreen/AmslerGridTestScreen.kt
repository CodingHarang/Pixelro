package com.pixelro.nenoonkiosk.ui.testscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.ui.testcontent.AmslerGridTestContent

@Composable
fun AmslerGridTestScreen(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    DisposableEffect(true) {
        viewModel.initializeAmslerGridTest()
        onDispose {

        }
    }
    FaceDetection(viewModel = viewModel)
    AmslerGridTestContent(
        toResultScreen = toResultScreen,
        viewModel = viewModel
    )
}