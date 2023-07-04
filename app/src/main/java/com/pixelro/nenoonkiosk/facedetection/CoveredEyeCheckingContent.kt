package com.pixelro.nenoonkiosk.facedetection

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionViewModel

@Composable
fun CoveredEyeCheckingContent(
    coveredEyeCheckingContentVisibleState: MutableTransitionState<Boolean>,
    toNextContent: () -> Unit,
    isLeftEye: Boolean,
    faceDetectionViewModel: FaceDetectionViewModel = hiltViewModel()
) {
//    AnimatedVisibility(
//        visibleState = coveredEyeCheckingContentVisibleState,
//        enter = AnimationProvider.enterTransition,
//        exit = AnimationProvider.exitTransition
//    ) {
//        FaceDetection()
//        LaunchedEffect(true) {
//            faceDetectionViewModel.initializeCoveredEyeChecking(isLeftEye) { toNextContent() }
//        }
//        Column(
//            modifier = Modifier,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                modifier = Modifier
//                    .padding(top = 100.dp, bottom = 100.dp),
//                text = when(isLeftEye) {
//                    true -> StringProvider.getString(R.string.covered_eye_checking_left_description)
//                    else -> StringProvider.getString(R.string.covered_eye_checking_right_description)
//                },
//                color = Color(0xffffffff),
//                fontSize = 40.sp,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//            Image(
//                modifier = Modifier
//                    .graphicsLayer {
//                        this.rotationY = when (isLeftEye) {
//                            true -> 0f
//                            else -> 180f
//                        }
//                    }
//                    .width(600.dp)
//                    .height(600.dp)
//                    .padding(bottom = 40.dp),
//                painter = painterResource(id = R.drawable.img_right_eye_covered),
//                contentDescription = ""
//            )
//            Text(
//                text = StringProvider.getString(R.string.covered_eye_checking_description),
//                color = Color(0xffffffff),
//                fontSize = 20.sp
//            )
//            if(faceDetectionViewModel.isTimerShowing.collectAsState().value) {
//                Text(
//                    text = "${faceDetectionViewModel.leftTime.collectAsState().value.toInt()}",
//                    color = Color(0xffffffff),
//                    fontSize = 80.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
}