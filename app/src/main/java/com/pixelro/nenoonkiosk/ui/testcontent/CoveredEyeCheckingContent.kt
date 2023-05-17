package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CoveredEyeCheckingContent(
    viewModel: NenoonViewModel,
    coveredEyeCheckingContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = coveredEyeCheckingContentVisibleState,
        enter = viewModel.enterTransition,
        exit = viewModel.exitTransition
    ) {
        DisposableEffect(true) {
            Log.e("DisposableEffect", "${viewModel.coveredEyeCheckingContentVisibleState.targetState}")
            viewModel.checkCoveredEye()
            onDispose {}
        }
        val isLeftEye = viewModel.isLeftEye.collectAsState().value
//        val isCoveredEyeCheckingDone = viewModel.isCoveredEyeCheckingDone.collectAsState().value
        Log.e("CoveredEyeCheckingContent", "insideAnimatedVisibility")
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                .graphicsLayer {
                    this.rotationY = when(isLeftEye) {
                        true -> 180f
                        else -> 0f
                    }
                },
                painter = painterResource(id = R.drawable.covered_eye_image),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Text(
                text = when(isLeftEye) {
                            true -> "오른쪽 눈을 가리고\n왼쪽 눈을 검사하겠습니다."
                            else -> "왼쪽 눈을 가리고\n오른쪽 눈을 검사하겠습니다."
                        },
                color = Color(0xffffffff),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            if(viewModel.isTimerShowing.collectAsState().value) {
                Text(
                    text = "${viewModel.leftTime.collectAsState().value.toInt()}",
                    color = Color(0xffffffff),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
//            Spacer(
//                modifier = Modifier
//                    .height(20.dp)
//            )
//            Image(
//                modifier = Modifier
//                    .height(256.dp)
//                    .clickable {
//                        coveredEyeCheckingContentVisibleState.targetState = false
//                        nextVisibleState.targetState = true
//                    },
//                painter = painterResource(id = R.drawable.baseline_check_circle_48),
//                contentDescription = ""
//            )
        }
    }
}