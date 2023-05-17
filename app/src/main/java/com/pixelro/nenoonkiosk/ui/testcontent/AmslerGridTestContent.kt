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
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
    Log.e("AmslerGridTestContent", "AmslerGridTestContent")
    val measuringDistanceContentVisibleState = viewModel.measuringDistanceContentVisibleState
    val coveredEyeCheckingContentVisibleState = viewModel.coveredEyeCheckingContentVisibleState
    val amslerGridContentVisibleState = viewModel.amslerGridContentVisibleState

    Log.e("AmslerGridTestContent", "${measuringDistanceContentVisibleState.currentState}, ${coveredEyeCheckingContentVisibleState.currentState}, ${amslerGridContentVisibleState.currentState}")

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
                amslerGridContentVisibleState = amslerGridContentVisibleState,
                nextVisibleState = coveredEyeCheckingContentVisibleState,
                toResultScreen = toResultScreen
            )
        }
    }
}

@Composable
fun AmslerGridContent(
    viewModel: NenoonViewModel,
    amslerGridContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: () -> Unit
) {
    Log.e("AmslerGridContent", "AmslerGridContent")
    AnimatedVisibility(
        visibleState = amslerGridContentVisibleState,
        enter = viewModel.enterTransition,
        exit = viewModel.exitTransition
    ) {
        DisposableEffect(true) {
            onDispose() {
            }
        }
        Column(
            modifier = Modifier
                .width(700.dp)
        ) {
            val width = viewModel.widthSize.collectAsState().value
            val height = viewModel.heightSize.collectAsState().value
            val rotX = viewModel.rotX.collectAsState().value
            val rotY = viewModel.rotY.collectAsState().value
            val currentSelectedArea = viewModel.currentSelectedArea.collectAsState().value
            val isLeft = viewModel.isLeftEye.collectAsState().value

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .width(700.dp)
                    .height(700.dp)
                    .padding(40.dp)
            ) {
                Image(
                    modifier = Modifier
                        .width(600.dp)
                        .height(600.dp),
                    painter = painterResource(id = R.drawable.amsler_grid),
                    contentDescription = ""
                )
                Canvas(
                    modifier = Modifier
                        .width(600.dp)
                        .height(600.dp)
                        .pointerInput(this) {
                            detectTapGestures(
                                onPress = {
                                    viewModel.updateCurrentSelectedArea(it)
                                    Log.e("", "$it")
                                }
                            )
                        }
                ) {
                    drawCircle(
                        color = Color(0xff00ff00),
                        radius = 50f,
                        center = Offset(450f, 450f)
                    )
                    for(areaNum in 0..8) {
                        if(currentSelectedArea[areaNum]) {
                            drawRect(
                                color = Color(0x550000ff),
                                topLeft = when(areaNum) {
                                    0 -> Offset(0f, 0f)
                                    1 -> Offset(300f, 0f)
                                    2 -> Offset(600f, 0f)
                                    3 -> Offset(0f, 300f)
                                    4 -> Offset(300f, 300f)
                                    5 -> Offset(600f, 300f)
                                    6 -> Offset(0f, 600f)
                                    7 -> Offset(300f, 600f)
                                    else -> Offset(600f, 600f)
                                },
                                size = Size(300f, 300f)
                            )
                        }
                    }
                    drawCircle(
                        color = Color(0xffff0000),
                        radius = 20f,
                        center = Offset(450f - (400f * tan(rotY * 0.0174533)).toFloat(), 450f - (400f * tan((rotX + 10) * 0.0174533)).toFloat())
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(100.dp)
                        .background(
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (isLeft) {
                                viewModel.updateIsLeftEye(false)
                                viewModel.updateLeftSelectedArea()
                                amslerGridContentVisibleState.targetState = false
                                nextVisibleState.targetState = true
                            } else {
                                viewModel.updateRightSelectedArea()
//                                viewModel.updateIsCoveredEyeCheckingDone(false)
                                toResultScreen()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "완료",
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}