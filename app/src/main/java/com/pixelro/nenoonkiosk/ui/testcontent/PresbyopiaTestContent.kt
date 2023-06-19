package com.pixelro.nenoonkiosk.ui.testcontent

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.ui.screen.textAsBitmap
import java.util.*
import kotlin.math.roundToInt

@Composable
fun PresbyopiaTestContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    val firstVisibleState = viewModel.firstItemVisibleState
    val secondVisibleState = viewModel.secondItemVisibleState
    val thirdVisibleState = viewModel.thirdItemVisibleState
    FaceDetection(
        viewModel = viewModel,
        visibleState = viewModel.measuringDistanceContentVisibleState
    )
    Text(
        modifier = Modifier
            .padding(start = 40.dp, top = 120.dp, end = 40.dp)
            .fillMaxWidth(),
        text = StringProvider.getString(R.string.presbyopia_test_description),
        color = Color(0xffffffff),
        fontSize = 28.sp
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 120.dp)
            .height(400.dp)
            .width(400.dp)
            .background(
                color = Color(0xffffffff),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        PresbyopiaFirstPage(
            visibleState = firstVisibleState,
            enterAnimation = AnimationProvider.enterTransition,
            exitAnimation = AnimationProvider.exitTransition
        )
        PresbyopiaSecondPage(
            visibleState = secondVisibleState,
            enterAnimation = AnimationProvider.enterTransition,
            exitAnimation = AnimationProvider.exitTransition
        )
        PresbyopiaThirdPage(
            visibleState = thirdVisibleState,
            enterAnimation = AnimationProvider.enterTransition,
            exitAnimation = AnimationProvider.exitTransition
        )
    }
    Text(
        modifier = Modifier
            .padding(top = 80.dp),
        text = StringProvider.getString(R.string.test_screen_current_distance),
        fontSize = 24.sp,
        color = Color(0xffffffff)
    )
    Text(
        text = "${(viewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
        fontSize = 68.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xffffffff)
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp, end = 40.dp, bottom = (viewModel.navigationBarPadding.collectAsState().value).dp)
                .fillMaxWidth()
                .background(
                    color = Color(0xff1d71e1),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    if (firstVisibleState.currentState) {
                        viewModel.updateFirstDistance()

                    } else if (secondVisibleState.currentState) {
                        viewModel.updateSecondDistance()
                    } else {
                        viewModel.updateThirdDistance()
                        toResultScreen()
                    }
                }
                .padding(20.dp),
            text = StringProvider.getString(R.string.next),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            color = Color(0xffffffff)
        )
    }
}

@Composable
fun PresbyopiaFirstPage(
    visibleState: MutableTransitionState<Boolean>,
    enterAnimation: EnterTransition,
    exitAnimation: ExitTransition
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = enterAnimation,
        exit = exitAnimation
    ) {
        val context = LocalContext.current
        lateinit var tts: TextToSpeech
        tts = remember {
            TextToSpeech(context) {
                if(it == TextToSpeech.SUCCESS) {
                    tts.stop()
                    tts.language = Locale.KOREAN
                    tts.setSpeechRate(1.0f)
                    tts.speak("조절력 검사를 시작하겠습니다. 화면을 눈과 평행하게 놓고, 50cm 지점에서 멈추세요. 다음으로, 조금씩 가까이 오다가 시표가 흐릿해지는 지점에서 멈추고, 아래의 다음 버튼을 누르세요. 각 3개의 시표에 대해 같은 과정을 반복하세요.", TextToSpeech.QUEUE_FLUSH, null, null)
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
                painter = painterResource(id = R.drawable.img_test_presbyopia_1),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun PresbyopiaSecondPage(
    visibleState: MutableTransitionState<Boolean>,
    enterAnimation: EnterTransition,
    exitAnimation: ExitTransition
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = enterAnimation,
        exit = exitAnimation
    ) {
        val context = LocalContext.current
        lateinit var tts: TextToSpeech
        tts = remember {
            TextToSpeech(context) {
                if(it == TextToSpeech.SUCCESS) {
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
    visibleState: MutableTransitionState<Boolean>,
    enterAnimation: EnterTransition,
    exitAnimation: ExitTransition
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = enterAnimation,
        exit = exitAnimation
    ) {
        val context = LocalContext.current
        lateinit var tts: TextToSpeech
        tts = remember {
            TextToSpeech(context) {
                if(it == TextToSpeech.SUCCESS) {
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