package com.pixelro.nenoonkiosk.ui.testscreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.ui.testcontent.PresbyopiaTestContent

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PresbyopiaTestScreen(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    DisposableEffect(true) {
        viewModel.initializePresbyopiaTest()
        onDispose {

        }
    }
    FaceDetection(
        viewModel = viewModel
    )
    PresbyopiaTestContent(
        toResultScreen = toResultScreen,
        viewModel = viewModel
    )
}