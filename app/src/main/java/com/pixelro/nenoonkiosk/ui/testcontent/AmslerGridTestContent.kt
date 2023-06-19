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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.MacularDisorderType
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import kotlin.math.tan

@Composable
fun AmslerGridTestContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    val measuringDistanceContentVisibleState = viewModel.measuringDistanceContentVisibleState
    val coveredEyeCheckingContentVisibleState = viewModel.coveredEyeCheckingContentVisibleState
    val amslerGridContentVisibleState = viewModel.amslerGridContentVisibleState
    val macularDistortedTypeVisibleState = viewModel.macularDistortedTypeVisibleState

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
                macularDistortedTypeVisibleState = macularDistortedTypeVisibleState,
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
    macularDistortedTypeVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: () -> Unit
) {
    AnimatedVisibility(
        visibleState = amslerGridContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        FaceDetection(
            viewModel = viewModel,
            visibleState = viewModel.measuringDistanceContentVisibleState
        )
        DisposableEffect(true) {
            onDispose() {
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val rotX = viewModel.rotX.collectAsState().value
            val rotY = viewModel.rotY.collectAsState().value
            val currentSelectedArea = viewModel.currentSelectedArea.collectAsState().value
            val isLeft = viewModel.isLeftEye.collectAsState().value

            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                text = StringProvider.getString(R.string.amsler_grid_test_description),
                fontSize = 28.sp,
                color = Color(0xffffffff),
                fontWeight = FontWeight.Medium
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .width(700.dp)
                    .height(700.dp)
                    .padding(40.dp),
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
                ) {
                    drawCircle(
                        color = Color(0xff000000),
                        radius = 50f,
                        center = Offset(450f, 450f)
                    )
//                    for(areaNum in 0..8) {
//                        if(currentSelectedArea[areaNum]) {
//                            drawRect(
//                                color = Color(0x550000ff),
//                                topLeft = when(areaNum) {
//                                    0 -> Offset(0f, 0f)
//                                    1 -> Offset(300f, 0f)
//                                    2 -> Offset(600f, 0f)
//                                    3 -> Offset(0f, 300f)
//                                    4 -> Offset(300f, 300f)
//                                    5 -> Offset(600f, 300f)
//                                    6 -> Offset(0f, 600f)
//                                    7 -> Offset(300f, 600f)
//                                    else -> Offset(600f, 600f)
//                                },
//                                size = Size(300f, 300f)
//                            )
//                        }
//                    }
                    drawCircle(
                        color = Color(0xff0000ff),
                        radius = 20f,
                        center = Offset(450f - (400f * tan(rotY * 0.0174533)).toFloat(), 450f - (400f * tan((rotX + 10) * 0.0174533)).toFloat())
                    )
                }
                Column(
                    modifier = Modifier
                        .width(600.dp)
                        .height(600.dp)
                        .pointerInput(this) {
                            detectTapGestures(
                                onPress = {
                                    viewModel.updateCurrentSelectedPosition(it)
                                }
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[0]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[1]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[2]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[3]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[4]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[5]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[6]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[7]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = when (currentSelectedArea[8]) {
                                        MacularDisorderType.Normal -> Color(0x00000000)
                                        else -> Color(0x550000ff)
                                    }
                                )
                        )
                    }
                }
            }
            AnimatedVisibility(
                visibleState = macularDistortedTypeVisibleState,
                enter = AnimationProvider.enterTransitionUp,
                exit = AnimationProvider.exitTransitionDown
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 40.dp)
                            .width(200.dp)
                            .height(200.dp)
                            .clickable {
                                macularDistortedTypeVisibleState.targetState = false
                                viewModel.updateCurrentSelectedArea(0)
                            },
                        painter = painterResource(id = R.drawable.macular_distorted),
                        contentDescription = ""
                    )
                    Image(
                        modifier = Modifier
                            .padding(end = 40.dp)
                            .width(200.dp)
                            .height(200.dp)
                            .clickable {
                                macularDistortedTypeVisibleState.targetState = false
                                viewModel.updateCurrentSelectedArea(1)
                            },
                        painter = painterResource(id = R.drawable.macular_blacked),
                        contentDescription = ""
                    )
                    Image(
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp)
                            .clickable {
                                macularDistortedTypeVisibleState.targetState = false
                                viewModel.updateCurrentSelectedArea(2)
                            },
                        painter = painterResource(id = R.drawable.macular_whited),
                        contentDescription = ""
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 40.dp,
                            end = 40.dp,
                            bottom = (viewModel.navigationBarPadding.collectAsState().value).dp
                        )
                        .fillMaxWidth()
                        .background(
                            color = Color(0xff1d71e1),
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
                        }
                        .padding(20.dp),
                    text = StringProvider.getString(R.string.amsler_grid_test_content_done),
                    fontSize = 24.sp,
                    color = Color(0xffffffff),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}