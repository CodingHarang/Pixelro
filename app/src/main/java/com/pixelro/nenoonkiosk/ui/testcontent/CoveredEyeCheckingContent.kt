package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.StringProvider

@Composable
fun CoveredEyeCheckingContent(
    viewModel: NenoonViewModel,
    coveredEyeCheckingContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = coveredEyeCheckingContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        DisposableEffect(true) {
            Log.e("DisposableEffect", "${viewModel.coveredEyeCheckingContentVisibleState.targetState}")
            viewModel.initializeCoveredEyeChecking()
            viewModel.checkCoveredEye()
            onDispose {}
        }
        val isLeftEye = viewModel.isLeftEye.collectAsState().value
//        val isCoveredEyeCheckingDone = viewModel.isCoveredEyeCheckingDone.collectAsState().value
//        Log.e("CoveredEyeCheckingContent", "insideAnimatedVisibility")
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 100.dp, bottom = 100.dp),
                text = when(isLeftEye) {
                    true -> StringProvider.getString(R.string.covered_eye_checking_left_description)
                    else -> StringProvider.getString(R.string.covered_eye_checking_right_description)
                },
                color = Color(0xffffffff),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Image(
                modifier = Modifier
                .graphicsLayer {
                    this.rotationY = when(isLeftEye) {
                        true -> 180f
                        else -> 0f
                    }
                }
                .padding(bottom = 100.dp),
                painter = painterResource(id = R.drawable.img_right_eye_covered),
                contentDescription = ""
            )
            Text(
                text = "곧 검사가 시작됩니다",
                color = Color(0xffffffff),
                fontSize = 20.sp
            )
            if(viewModel.isTimerShowing.collectAsState().value) {
                Text(
                    text = "${viewModel.leftTime.collectAsState().value.toInt()}",
                    color = Color(0xffffffff),
                    fontSize = 60.sp,
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