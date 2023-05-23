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
import com.pixelro.nenoonkiosk.data.StringProvider
import kotlin.math.roundToInt

@Composable
fun MeasuringDistanceContent(
    viewModel: NenoonViewModel,
    measuringDistanceContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = measuringDistanceContentVisibleState,
        enter = viewModel.enterTransition,
        exit = viewModel.exitTransition
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
                text = StringProvider.getString(R.string.measuring_distance_content_description1),
                color = Color(0xffffffff),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Text(
                text = StringProvider.getString(R.string.measuring_distance_content_description2),
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
                        append(StringProvider.getString(R.string.test_screen_current_distance))
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xff0055ff),
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold
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