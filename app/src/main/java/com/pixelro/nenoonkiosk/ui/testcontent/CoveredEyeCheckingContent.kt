package com.pixelro.nenoonkiosk.ui.testcontent

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.pixelro.nenoonkiosk.R

@Composable
fun CoveredEyeCheckingContent(
    secondVisibleState: MutableTransitionState<Boolean>,
    thirdVisibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = secondVisibleState,
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
                painter = painterResource(id = R.drawable.covered_eye_image),
                contentDescription = ""
            )
            Text(
                text = "왼쪽 눈을 가리고\n오른쪽 눈을 검사하겠습니다.",
                color = Color(0xffffffff)
            )
            Image(
                modifier = Modifier
                    .height(256.dp)
                    .clickable {
                        secondVisibleState.targetState = false
                        thirdVisibleState.targetState = true
                    },
                painter = painterResource(id = R.drawable.baseline_check_circle_48),
                contentDescription = ""
            )
        }
    }
}