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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
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

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(400.dp)
            .width(400.dp)
            .background(
                color = Color(0xffffffff),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        PresbyopiaFirstPage(
            visibleState = firstVisibleState,
            enterAnimation = viewModel.enterTransition,
            exitAnimation = viewModel.exitTransition
        )
        PresbyopiaSecondPage(
            visibleState = secondVisibleState,
            enterAnimation = viewModel.enterTransition,
            exitAnimation = viewModel.exitTransition
        )
        PresbyopiaThirdPage(
            visibleState = thirdVisibleState,
            enterAnimation = viewModel.enterTransition,
            exitAnimation = viewModel.exitTransition
        )
    }
    Text(
        modifier = Modifier
            .padding(top = 20.dp),
        text = "현재 거리: ${(viewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
        fontSize = 30.sp,
        color = Color(0xffffffff)
    )
    Image(
        modifier = Modifier
            .height(256.dp)
            .clickable {
                if (firstVisibleState.currentState) {
                    viewModel.updateFirstDistance()

                } else if (secondVisibleState.currentState) {
                    viewModel.updateSecondDistance()
                } else {
                    viewModel.updateThirdDistance()
                    toResultScreen()
                }
            },
        painter = painterResource(id = R.drawable.baseline_check_circle_48),
        contentDescription = ""
    )
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
        Log.e("PresbyopiaFirstPage", "PresbyopiaFirstPage")
        val context = LocalContext.current
        lateinit var tts: TextToSpeech
        tts = remember {
            TextToSpeech(context) {
                if(it == TextToSpeech.SUCCESS) {
                    tts.stop()
                    tts.language = Locale.KOREAN
                    tts.setSpeechRate(1.0f)
                    tts.speak("안구 나이 검사를 시작하겠습니다. 기기를 눈과 평행하도록 놓고, 시표가 선명하게 보이는 지점에서 멈추세요. 다음으로, 조금씩 가까이 오다가 시표가 흐릿해지는 지점에서 멈추고, 버튼을 누르세요. 각, 시표에 대해 같은 과정을 반복합니다.", TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
        DisposableEffect(true) {
            onDispose {
                Log.e("first", "onDispose")
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
        Log.e("PresbyopiaSecondPage", "PresbyopiaSecondPage")
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
                Log.e("second", "onDispose")
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
        Log.e("PresbyopiaThirdPage", "PresbyopiaThirdPage")
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
                Log.e("third", "onDispose")
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