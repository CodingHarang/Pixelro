package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import kotlin.math.tan

@Composable
fun AmslerGridTestContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    val measuringDistanceContentVisibleState = remember { MutableTransitionState(true) }
    val coveredEyeCheckingContentVisibleState = remember { MutableTransitionState(false) }
    val amslerGridContentVisibleState = remember { MutableTransitionState(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xff000000)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp),
            text = "암슬러 차트 검사",
            color = Color(0xffffffff),
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MeasuringDistanceContent(
                viewModel = viewModel,
                measuringDistanceContentVisibleState = measuringDistanceContentVisibleState,
                nextVisibleState = coveredEyeCheckingContentVisibleState
            )
            CoveredEyeCheckingContent(
                viewModel = viewModel,
                coveredEyeCheckingContentVisibleState = coveredEyeCheckingContentVisibleState,
                nextVisibleState = amslerGridContentVisibleState
            )
            AmslerGridContent(
                viewModel = viewModel,
                amslerGridContentVisibleState = amslerGridContentVisibleState
            )
        }
    }
}

@Composable
fun AmslerGridContent(
    viewModel: NenoonViewModel,
    amslerGridContentVisibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = amslerGridContentVisibleState,
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
            modifier = Modifier
                .width(600.dp)
        ) {
            val touchPosition = viewModel.touchPosition.collectAsState().value
            val width = viewModel.widthSize.collectAsState().value
            val height = viewModel.heightSize.collectAsState().value
            val ovalColor = viewModel.color.collectAsState().value
            val rotX = viewModel.rotX.collectAsState().value
            val rotY = viewModel.rotY.collectAsState().value

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(40.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .width(520.dp)
                        .height(520.dp)
                        .pointerInput(this) {
                            detectTapGestures(
                                onPress = {
                                    viewModel.updateColor(Color(0x550000ff))
                                    viewModel.updateTouchPosition(it)
                                    Log.e("", "$it")
                                }
                            )
                        }
                ) {
                    drawCircle(
                        color = Color(0xff00ff00),
                        radius = 50f,
                        center = Offset(390f, 390f)
                    )
                    drawCircle(
                        color = Color(0xffff0000),
                        radius = 20f,
                        center = Offset(390f - (400f * tan(rotY * 0.0174533)).toFloat(), 390f - (400f * tan((rotX + 5) * 0.0174533)).toFloat())
                    )
                    drawOval(
                        color = ovalColor,
                        topLeft = Offset(touchPosition.x - (width / 2), touchPosition.y - (height / 2)),
                        size = Size(width, height)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.amsler_grid),
                    contentDescription = ""
                )
            }
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Slider(
                value = width,
                onValueChange = {
                    viewModel.updateWidthSize(it)
                },
                valueRange = 1f..200f
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Slider(
                value = height,
                onValueChange = {
                    viewModel.updateHeightSize(it)
                },
                valueRange = 1f..200f
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Image(
                modifier = Modifier
                    .height(256.dp)
                    .clickable {

                    },
                painter = painterResource(id = R.drawable.baseline_check_circle_48),
                contentDescription = ""
            )
        }
    }
}