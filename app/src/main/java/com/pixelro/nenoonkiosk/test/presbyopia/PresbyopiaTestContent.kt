package com.pixelro.nenoonkiosk.test.presbyopia

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PresbyopiaTestContent(
    toResultScreen: (PresbyopiaTestResult) -> Unit,
    presbyopiaViewModel: PresbyopiaViewModel = hiltViewModel(),
    faceDetectionViewModel: FaceDetectionViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        presbyopiaViewModel.init()
        TTS.speechTTS("조절력 검사를 시작하겠습니다. 화면으로부터 40~60cm 사이로 거리를 조정해주세요.", TextToSpeech.QUEUE_ADD)
    }
    FaceDetection()
    val distance = faceDetectionViewModel.screenToFaceDistance.collectAsState().value
//    val isFirstItemVisible = presbyopiaViewModel.isFirstItemVisible.collectAsState().value
//    val isSecondItemVisible = presbyopiaViewModel.isSecondItemVisible.collectAsState().value
//    val isThirdItemVisible = presbyopiaViewModel.isThirdItemVisible.collectAsState().value
//    val firstItemVisibleState = remember { MutableTransitionState(true) }
//    firstItemVisibleState.targetState = isFirstItemVisible
//    val secondItemVisibleState = remember { MutableTransitionState(false) }
//    secondItemVisibleState.targetState = isSecondItemVisible
//    val thirdItemVisibleState = remember { MutableTransitionState(false) }
//    thirdItemVisibleState.targetState = isThirdItemVisible
    val isMovedTo40cm = presbyopiaViewModel.isMovedTo40cm.collectAsState().value
    val isUnder25cm = presbyopiaViewModel.isUnder25cm.collectAsState().value
    val isBlinkingDone = presbyopiaViewModel.isBlinkingDone.collectAsState().value
    val tryCount = presbyopiaViewModel.tryCount.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    if (distance > 400f && !isMovedTo40cm && !TTS.tts.isSpeaking) {
        presbyopiaViewModel.blinkNumber()
        presbyopiaViewModel.updateIsMovedTo40cm(true)
    }
    if (distance < 250f && !isUnder25cm && isMovedTo40cm) {
        when (tryCount) {
            0 -> TTS.speechTTS("다음 측정을 실시합니다. 아래의 다음 버튼을 눌러주세요", TextToSpeech.QUEUE_ADD)
            1 -> TTS.speechTTS("다음 측정을 실시합니다. 아래의 다음 버튼을 눌러주세요", TextToSpeech.QUEUE_ADD)
            else -> TTS.speechTTS("검사를 모두 완료했습니다. 아래의 다음 버튼을 눌러주세요", TextToSpeech.QUEUE_ADD)
        }
        presbyopiaViewModel.updateIsUnder25cm(true)
    }
    Box(
        modifier = Modifier
            .padding(start = 40.dp, top = 20.dp, end = 40.dp)
            .fillMaxWidth()
            .height(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (!isMovedTo40cm) {
                true -> "화면으로부터 40~60cm 사이로 거리를 조정해주세요"
                false -> when(isBlinkingDone) {
                    true -> "조금씩 앞으로 오다가 숫자가 흐릿해지는\n지점에서 멈추고 아래의 '다음'을 눌러주세요"
                    false -> "아래의 깜빡이는 숫자를 봐주세요"
                }
            },
            color = Color(0xffffffff),
            fontSize = when (!isMovedTo40cm) {
                true -> 48.sp
                false -> when(isBlinkingDone) {
                    true -> 36.sp
                    false -> 48.sp
                }
            },
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

    LinearProgressIndicator(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .width(600.dp)
            .height(20.dp),
        progress = animatedProgress,
        color = Color(0xff1d71e1),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 20.dp)
            .height(600.dp)
            .width(600.dp)
            .background(
                color = Color(0xffffffff),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (presbyopiaViewModel.isNumberShowing.collectAsState().value) {
                if (isUnder25cm) {
                    Text(
                        text = when (tryCount) {
                            0 -> "두 번째 측정을 실시합니다\n아래의 '다음'을 눌러주세요"
                            1 -> "세 번째 측정을 실시합니다\n아래의 '다음'을 눌러주세요"
                            else -> "검사가 완료되었습니다\n아래의 '다음'을 눌러주세요"
                        },
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
//                    Row() {
//                        Image(
//                            painter = painterResource(id = R.drawable.accommodation_1),
//                            contentDescription = null
//                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.accommodation_2),
//                            contentDescription = null
//                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.accommodation_3),
//                            contentDescription = null
//                        )
//                    }
//                    Row() {
//                        Image(
//                            painter = painterResource(id = R.drawable.accommodation_4),
//                            contentDescription = null
//                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.accommodation_5),
//                            contentDescription = null
//                        )
//                        Image(
//                            painter = painterResource(id = R.drawable.accommodation_6),
//                            contentDescription = null
//                        )
//                    }
                    Text(
                        text = "2  3  4  5",
                        fontWeight = FontWeight.Bold,
                        fontSize = 60.sp
                    )
                    Text(
                        text = "6  7  8  9",
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            modifier = Modifier
                .padding(top = 40.dp),
            text = StringProvider.getString(R.string.test_screen_current_distance),
            fontSize = 24.sp,
            color = Color(0xffffffff)
        )
        Text(
            text = "${(faceDetectionViewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
            fontSize = 68.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xffffffff)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (isMovedTo40cm && isBlinkingDone) {
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
                            RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(0xff1d71e1),
                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        )
                        .clickable {
                            presbyopiaViewModel.moveToNextStep(
                                distance,
                                handleProgress = {
                                    progress = it
                                }
                            ) {
                                toResultScreen(
                                    presbyopiaViewModel.getPresbyopiaTestResult()
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.next),
                        fontSize = 40.sp,
                        color = Color(0xffffffff),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PresbyopiaFirstPage(
    visibleState: MutableTransitionState<Boolean>,
    presbyopiaViewModel: PresbyopiaViewModel = hiltViewModel()
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        val context = LocalContext.current
        lateinit var tts: TextToSpeech
        tts = remember {
            TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    tts.stop()
                    tts.language = Locale.KOREAN
                    tts.setSpeechRate(1.0f)
                    tts.speak(
                        "조절력 검사를 시작하겠습니다. 화면을 눈과 평행하게 놓고, 50cm 지점에서 멈추세요. 다음으로, 조금씩 가까이 오다가 시표가 흐릿해지는 지점에서 멈추고, 아래의 다음 버튼을 누르세요. 각 3개의 시표에 대해 같은 과정을 반복하세요.",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                }
            }
        }
        DisposableEffect(true) {
            onDispose {
                tts.stop()
                tts.shutdown()
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.img_test_presbyopia_1),
//                contentDescription = ""
//            )
            if (!presbyopiaViewModel.isNumberShowing.collectAsState().value) {
                Text(
                    text = "2  3  4  5",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 120.sp
                )
                Text(
                    text = "6  7  8  9",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 80.sp
                )
            }
        }
    }
}

@Composable
fun PresbyopiaSecondPage(
    visibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        val context = LocalContext.current
        lateinit var tts: TextToSpeech
        tts = remember {
            TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    tts.stop()
                    tts.language = Locale.KOREAN
                    tts.setSpeechRate(1.0f)
                    tts.speak("다음 시표에 대해 검사를 시작하겠습니다.", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
        DisposableEffect(true) {
            onDispose {
                tts.stop()
                tts.shutdown()
            }
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_test_presbyopia_2),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun PresbyopiaThirdPage(
    visibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        val context = LocalContext.current
        lateinit var tts: TextToSpeech
        tts = remember {
            TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    tts.stop()
                    tts.language = Locale.KOREAN
                    tts.setSpeechRate(1.0f)
                    tts.speak("마지막 시표에 대해 검사를 시작하겠습니다.", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
        DisposableEffect(true) {
            onDispose {
                tts.stop()
                tts.shutdown()
            }
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_test_presbyopia_3),
                contentDescription = ""
            )
        }
    }
}