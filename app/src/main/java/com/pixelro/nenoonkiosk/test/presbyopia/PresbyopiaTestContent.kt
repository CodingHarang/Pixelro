package com.pixelro.nenoonkiosk.test.presbyopia

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.TTS
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun PresbyopiaTestContent(
    toResultScreen: (PresbyopiaTestResult) -> Unit,
    presbyopiaViewModel: PresbyopiaViewModel = hiltViewModel(),
    faceDetectionViewModel: FaceDetectionViewModel = hiltViewModel()
) {
    val distance = faceDetectionViewModel.screenToFaceDistance.collectAsState().value
    val testState = presbyopiaViewModel.testState.collectAsState().value
    val tryCount = presbyopiaViewModel.tryCount.collectAsState().value
    val isNumberShowing = presbyopiaViewModel.isTextShowing.collectAsState().value
    FaceDetection()

    presbyopiaViewModel.checkCondition(distance)

    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    Box(
        modifier = Modifier
            .padding(start = 40.dp, top = 20.dp, end = 40.dp)
            .fillMaxWidth()
            .height(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (testState) {
                PresbyopiaViewModel.TestState.Started,
                PresbyopiaViewModel.TestState.AdjustingDistance -> "화면으로부터 40~60cm 사이로 거리를 조정해주세요"
                PresbyopiaViewModel.TestState.TextBlinking -> "아래의 깜빡이는 숫자를 봐주세요"
                PresbyopiaViewModel.TestState.ComingCloser -> "조금씩 앞으로 오다가 숫자가 흐릿해보이는\n지점에서 멈추고 아래의 '다음'을 눌러주세요"
                PresbyopiaViewModel.TestState.NoPresbyopia -> {
                    when (tryCount) {
                        0 -> "첫 번째 측정에서 노안이 발견되지 않았습니다\n아래의 '다음'을 눌러주세요."
                        1 -> "두 번째 측정에서 노안이 발견되지 않았습니다\n아래의 '다음'을 눌러주세요."
                        else -> "마지막 측정에서 노안이 발견되지 않았습니다\n아래의 '다음'을 눌러주세요."
                    }
                }
            },
            color = Color(0xffffffff),
            fontSize = when (testState) {
                PresbyopiaViewModel.TestState.ComingCloser -> 36.sp
                else -> 48.sp
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
            if (isNumberShowing) {
                if (testState == PresbyopiaViewModel.TestState.NoPresbyopia) {
                    Text(
                        text = when (tryCount) {
                            0 -> "첫 번째 측정에서\n노안이 발견되지 않았습니다\n아래의 다음을 눌러주세요"
                            1 -> "두 번째 측정에서\n노안이 발견되지 않았습니다\n아래의 다음을 눌러주세요"
                            else -> "마지막 측정에서\n노안이 발견되지 않았습니다\n아래의 다음을 눌러주세요"
                        },
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
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
    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 304.dp),
            text = StringProvider.getString(R.string.test_screen_current_distance),
            fontSize = 24.sp,
            color = Color(0xffffffff)
        )
        Text(
            modifier = Modifier
                .padding(bottom = 120.dp),
            text = "${(faceDetectionViewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
            fontSize = 140.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xffffffff)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (testState == PresbyopiaViewModel.TestState.ComingCloser || testState == PresbyopiaViewModel.TestState.NoPresbyopia) {
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