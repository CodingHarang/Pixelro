package com.pixelro.nenoonkiosk.test.macular.amslergrid

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.TTS
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionViewModel
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
    val measuringDistanceContentVisibleState = remember { MutableTransitionState(false) }
    measuringDistanceContentVisibleState.targetState = amslerGridViewModel.isMeasuringDistanceContentVisible.collectAsState().value
    val amslerGridContentVisibleState = remember { MutableTransitionState(true) }
//    amslerGridContentVisibleState.targetState = amslerGridViewModel.isAmslerGridContentVisible.collectAsState().value

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
//            MeasuringDistanceContent(
//                measuringDistanceContentVisibleState = measuringDistanceContentVisibleState,
//                toNextContent = {
//                    amslerGridViewModel.updateIsMeasuringDistanceContentVisible(false)
//                    amslerGridViewModel.updateIsAmslerGridContentVisible(true)
////                    amslerGridViewModel.updateIsMacularDegenerationTypeVisible(false)
//                },
//                selectedTestType = TestType.AmslerGrid,
//                isLeftEye = amslerGridViewModel.isLeftEye.collectAsState().value
//            )
            AmslerGridContent(
                amslerGridContentVisibleState = amslerGridContentVisibleState,
//                macularDegenerationTypeVisibleState = macularDegenerationTypeVisibleState,
                toResultScreen = toResultScreen
            )
        }
    }
}

@Composable
fun AmslerGridContent(
    amslerGridContentVisibleState: MutableTransitionState<Boolean>,
//    macularDegenerationTypeVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: (AmslerGridTestResult) -> Unit,
    amslerGridViewModel: AmslerGridViewModel = hiltViewModel(),
    faceDetectionViewModel: FaceDetectionViewModel = hiltViewModel()
) {
    val distance = faceDetectionViewModel.screenToFaceDistance.collectAsState().value
    AnimatedVisibility(
        visibleState = amslerGridContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        LaunchedEffect(true) {
            amslerGridViewModel.startBlinking()
            amslerGridViewModel.updateIsLookAtTheDotTTSDone(false)
            amslerGridViewModel.updateIsSelectTTSDone(false)
        }
        val isBlinkingDone = amslerGridViewModel.isBlinkingDone.collectAsState().value
        val isDotShowing = amslerGridViewModel.isDotShowing.collectAsState().value
        val isFaceCenter = amslerGridViewModel.isFaceCenter.collectAsState().value
        if (!amslerGridViewModel.isLookAtTheDotTTSDone.collectAsState().value) {
            amslerGridViewModel.updateIsLookAtTheDotTTSDone(true)
            TTS.speechTTS("검사를 시작하겠습니다. 격자 가운데의 깜빡이는 점을 봐주세요", TextToSpeech.QUEUE_ADD)
        }
        if (
            amslerGridViewModel.isLookAtTheDotTTSDone.collectAsState().value
            && isFaceCenter
            && !amslerGridViewModel.isSelectTTSDone.collectAsState().value
        ) {
            amslerGridViewModel.updateIsSelectTTSDone(true)
            TTS.speechTTS("이상하게 보이거나 왜곡되어 보이는 부분을, 손으로 눌러 선택해주세요. 선택을 완료했거나 이상한 부분이 없다면 아래의 완료 버튼을 눌러주세요.", TextToSpeech.QUEUE_ADD)
        }
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
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp)
                    .height(160.dp),
                text = when (!isBlinkingDone) {
                    true -> "아래의 깜빡이는 점을 봐주세요"
                    false -> when (isFaceCenter) {
                        true -> "이상하게 보이거나 왜곡되어 보이는 부분을 손으로 눌러 선택해주세요. 선택을 완료했거나 이상한 부분이 없다면, 아래의 완료 버튼을 눌러주세요."
                        false -> "가운데의 검은 점을 봐주세요"
                    }
                },
                fontSize = when (!isBlinkingDone) {
                    true -> 40.sp
                    false -> when (isFaceCenter) {
                        true -> 32.sp
                        false -> 40.sp
                    }
                },
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
                    if (isDotShowing) {
                        drawCircle(
                            color = Color(0xff000000),
                            radius = 46f,
                            center = Offset(450f, 450f)
                        )
                    }
                    if (true) {
                        drawCircle(
                            color = Color(0xff0000ff),
                            radius = 20f,
                            center = Offset(450f - (400f * tan(rotY * 0.0174533)).toFloat(), 450f - (400f * tan((rotX + 10) * 0.0174533)).toFloat())
                        )
                    }
                    if (450f - (400f * tan(rotY * 0.0174533)).toFloat() > 400f && 450f - (400f * tan(rotY * 0.0174533)).toFloat() < 500f
                            && 450f - (400f * tan((rotX + 10) * 0.0174533)).toFloat() > 400f && 450f - (400f * tan((rotX + 10) * 0.0174533)).toFloat() < 500f) {
                        amslerGridViewModel.updateIsFaceCenter(true)
                    }
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
//            AnimatedVisibility(
//                visibleState = macularDegenerationTypeVisibleState,
//                enter = AnimationProvider.enterTransitionUp,
//                exit = AnimationProvider.exitTransitionDown
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(top = 20.dp)
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    for(i in 0..2) {
//                        Image(
//                            modifier = Modifier
//                                .padding(end = (if (i == 2) 0.dp else 40.dp))
//                                .width(200.dp)
//                                .height(200.dp)
//                                .clickable {
//                                    amslerGridViewModel.updateIsMacularDegenerationTypeVisible(false)
//                                    amslerGridViewModel.updateCurrentSelectedArea(i)
//                                },
//                            painter = painterResource(id = when(i) {
//                                    0 -> R.drawable.macular_distorted
//                                    1 -> R.drawable.macular_blacked
//                                    else -> R.drawable.macular_whited
//                                }
//                            ),
//                            contentDescription = ""
//                        )
//                    }
//                }
//            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            start = 40.dp,
                            end = 40.dp,
                            bottom = 40.dp
                        )
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0xff1d71e1),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        text = String.format("%.1f", rotY),
                        fontSize = 40.sp,
                        color = Color(0xffffffff),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(
                            start = 40.dp,
                            end = 40.dp,
                            bottom = 40.dp
                        )
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0xff1d71e1),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 4.dp),
                        text = String.format("%.0f", distance / 10),
                        fontSize = 40.sp,
                        color = Color(0xffffffff),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}