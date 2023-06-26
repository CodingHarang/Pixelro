package com.pixelro.nenoonkiosk.test.visualacuity.shortdistance

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.CoveredEyeCheckingContent
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionWithPreview
import com.pixelro.nenoonkiosk.facedetection.MeasuringDistanceContent
import com.pixelro.nenoonkiosk.test.visualacuity.VisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.VisualAcuityViewModel
import com.pixelro.nenoonkiosk.ui.testcontent.VisualAcuityTestCommonContent

@Composable
fun ShortDistanceVisualAcuityTestContent(
    toResultScreen: (VisualAcuityTestResult) -> Unit,
    visualAcuityViewModel: VisualAcuityViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        visualAcuityViewModel.init()
    }
    val measuringDistanceContentVisibleState = remember { MutableTransitionState(true) }
    measuringDistanceContentVisibleState.targetState = visualAcuityViewModel.isMeasuringDistanceContentVisible.collectAsState().value
    val coveredEyeCheckingContentVisibleState = remember { MutableTransitionState(false) }
    coveredEyeCheckingContentVisibleState.targetState = visualAcuityViewModel.isCoveredEyeCheckingContentVisible.collectAsState().value
    val visualAcuityContentVisibleState = remember { MutableTransitionState(false) }
    visualAcuityContentVisibleState.targetState = visualAcuityViewModel.isVisualAcuityContentVisible.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xff000000)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            MeasuringDistanceContent(
                measuringDistanceContentVisibleState = measuringDistanceContentVisibleState,
                toNextContent = {
                    visualAcuityViewModel.updateIsMeasuringDistanceContentVisible(false)
                    visualAcuityViewModel.updateIsCoveredEyeCheckingContentVisible(true)
                },
                selectedTestType = TestType.ShortDistanceVisualAcuity
            )
            CoveredEyeCheckingContent(
                coveredEyeCheckingContentVisibleState = coveredEyeCheckingContentVisibleState,
                toNextContent = {
                    visualAcuityViewModel.updateIsCoveredEyeCheckingContentVisible(false)
                    visualAcuityViewModel.updateIsVisualAcuityContentVisible(true)
                },
                isLeftEye = visualAcuityViewModel.isLeftEye.collectAsState().value
            )
            VisualAcuityTestCommonContent(
                visualAcuityTestCommonContentVisibleState = visualAcuityContentVisibleState,
                toResultScreen = toResultScreen,
            )
        }

    }
}