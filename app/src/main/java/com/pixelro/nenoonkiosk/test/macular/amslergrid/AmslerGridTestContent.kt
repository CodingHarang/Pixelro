package com.pixelro.nenoonkiosk.test.macular.amslergrid

import android.app.Activity
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionViewModel
import com.pixelro.nenoonkiosk.facedetection.CoveredEyeCheckingContent
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionWithPreview
import com.pixelro.nenoonkiosk.facedetection.MeasuringDistanceContent
import kotlin.math.tan

@Composable
fun AmslerGridTestContent(
    toResultScreen: (AmslerGridTestResult) -> Unit,
    amslerGridViewModel: AmslerGridViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        amslerGridViewModel.init()
    }
    val measuringDistanceContentVisibleState = remember { MutableTransitionState(true) }
    measuringDistanceContentVisibleState.targetState = amslerGridViewModel.isMeasuringDistanceContentVisible.collectAsState().value
    val coveredEyeCheckingContentVisibleState = remember { MutableTransitionState(false) }
    coveredEyeCheckingContentVisibleState.targetState = amslerGridViewModel.isCoveredEyeCheckingContentVisible.collectAsState().value
    val amslerGridContentVisibleState = remember { MutableTransitionState(false) }
    amslerGridContentVisibleState.targetState = amslerGridViewModel.isAmslerGridContentVisible.collectAsState().value
    val macularDegenerationTypeVisibleState = remember { MutableTransitionState(false) }
    macularDegenerationTypeVisibleState.targetState = amslerGridViewModel.isMacularDegenerationTypeVisible.collectAsState().value
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
                    amslerGridViewModel.updateIsMeasuringDistanceContentVisible(false)
                    amslerGridViewModel.updateIsCoveredEyeCheckingContentVisible(true)
                },
                selectedTestType = TestType.AmslerGrid,
                isLeftEye = amslerGridViewModel.isLeftEye.collectAsState().value
            )
            CoveredEyeCheckingContent(
                coveredEyeCheckingContentVisibleState = coveredEyeCheckingContentVisibleState,
                toNextContent = {
                    amslerGridViewModel.updateIsCoveredEyeCheckingContentVisible(false)
                    amslerGridViewModel.updateIsAmslerGridContentVisible(true)
                    amslerGridViewModel.updateIsMacularDegenerationTypeVisible(false)
                },
                isLeftEye = amslerGridViewModel.isLeftEye.collectAsState().value
            )
            AmslerGridContent(
                amslerGridContentVisibleState = amslerGridContentVisibleState,
                macularDegenerationTypeVisibleState = macularDegenerationTypeVisibleState,
                toResultScreen = toResultScreen
            )
        }
    }
}

@Composable
fun AmslerGridContent(
    amslerGridContentVisibleState: MutableTransitionState<Boolean>,
    macularDegenerationTypeVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: (AmslerGridTestResult) -> Unit,
    amslerGridViewModel: AmslerGridViewModel = hiltViewModel(),
    faceDetectionViewModel: FaceDetectionViewModel = hiltViewModel()
) {
    AnimatedVisibility(
        visibleState = amslerGridContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        FaceDetection()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val rotX = faceDetectionViewModel.rotX.collectAsState().value
            val rotY = faceDetectionViewModel.rotY.collectAsState().value
            val currentSelectedArea = amslerGridViewModel.currentSelectedArea.collectAsState().value
            val isLeft = amslerGridViewModel.isLeftEye.collectAsState().value

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
                                    amslerGridViewModel.updateCurrentSelectedPosition(it)
                                }
                            )
                        }
                ) {
                    for(i in 0..2) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            for(j in (i * 3)..(i * 3 + 2)) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(
                                            color = when (currentSelectedArea[j]) {
                                                MacularDisorderType.Normal -> Color(0x00000000)
                                                else -> Color(0x550000ff)
                                            }
                                        )
                                )
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visibleState = macularDegenerationTypeVisibleState,
                enter = AnimationProvider.enterTransitionUp,
                exit = AnimationProvider.exitTransitionDown
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for(i in 0..2) {
                        Image(
                            modifier = Modifier
                                .padding(end = (if (i == 2) 0.dp else 40.dp))
                                .width(200.dp)
                                .height(200.dp)
                                .clickable {
                                    amslerGridViewModel.updateIsMacularDegenerationTypeVisible(false)
                                    amslerGridViewModel.updateCurrentSelectedArea(i)
                                },
                            painter = painterResource(id = when(i) {
                                    0 -> R.drawable.macular_distorted
                                    1 -> R.drawable.macular_blacked
                                    else -> R.drawable.macular_whited
                                }
                            ),
                            contentDescription = ""
                        )
                    }
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
                            bottom = GlobalValue.navigationBarPadding.dp
                        )
                        .fillMaxWidth()
                        .background(
                            color = Color(0xff1d71e1),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (isLeft) {
                                amslerGridViewModel.updateIsLeftEye(false)
                                amslerGridViewModel.updateLeftSelectedArea()
                                amslerGridViewModel.updateIsCoveredEyeCheckingContentVisible(true)
                                amslerGridViewModel.updateIsAmslerGridContentVisible(false)
                            } else {
                                amslerGridViewModel.updateRightSelectedArea()
                                amslerGridViewModel.updateIsAmslerGridContentVisible(false)
                                toResultScreen(amslerGridViewModel.getAmslerGridTestResult())
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