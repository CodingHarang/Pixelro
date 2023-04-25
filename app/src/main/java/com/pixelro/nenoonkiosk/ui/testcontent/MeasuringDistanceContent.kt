package com.pixelro.nenoonkiosk.ui.testcontent

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import kotlin.math.roundToInt

@Composable
fun MeasuringDistanceContent(
    viewModel: NenoonViewModel,
    measuringDistanceContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = measuringDistanceContentVisibleState,
        enter = slideIn(
            animationSpec = tween(durationMillis = 500),
            initialOffset = { IntOffset(it.width, 0) }
        ) + fadeIn(),
        exit = slideOut(
            animationSpec = tween(durationMillis = 500),
            targetOffset = { IntOffset(-it.width, 0) }
        ) + fadeOut()
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(start = 40.dp, end = 40.dp),
                painter = painterResource(id = R.drawable.img_eyetest_maneyes),
                contentDescription = ""
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Text(
                text = "시작하기를 누른 다음에는\n기기와 사용자의 거리를 고정해주세요.",
                color = Color(0xffffffff),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Text(
                text = "테스트 도중 측정 거리가 달라지면 시력 검사 값이 달라질 수 있습니다.",
                color = Color(0xffffffff),
                fontSize = 20.sp
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xffffffff),
                            fontSize = 40.sp
                        )
                    ) {
                        append("현재 거리: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xff0055ff),
                            fontSize = 50.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append("${(viewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm")
                    }
                },
                modifier = Modifier
                    .padding(20.dp)
            )
//            if(viewModel.screenToFaceDistance.collectAsState().value in (400.0..500.0)) {
            if(viewModel.screenToFaceDistance.collectAsState().value in (-500.0..500.0)) {
                Image(
                    modifier = Modifier
                        .height(256.dp)
                        .clickable {
                            viewModel.updateTestDistance()
                            measuringDistanceContentVisibleState.targetState = false
                            nextVisibleState.targetState = true
                        },
                    painter = painterResource(id = R.drawable.baseline_check_circle_48),
                    contentDescription = ""
                )
            }
        }
    }
}