package com.pixelro.nenoonkiosk.test.macular.mchart

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.TTS
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.MeasuringDistanceContent
import com.pixelro.nenoonkiosk.test.presbyopia.PresbyopiaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Composable
fun MChartTestContent(
    toResultScreen: (MChartTestResult) -> Unit,
    mChartViewModel: MChartViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        mChartViewModel.init()
    }
    DisposableEffect(true) {
        onDispose {
            mChartViewModel.exoPlayer.release()
        }
    }
    val measuringDistanceContentVisibleState = remember { MutableTransitionState(true) }
    measuringDistanceContentVisibleState.targetState = mChartViewModel.isMeasuringDistanceContentVisible.collectAsState().value
    val mChartContentVisibleState = remember { MutableTransitionState(false) }
    mChartContentVisibleState.targetState = mChartViewModel.isMChartContentVisible.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xff000000)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MeasuringDistanceContent(
                measuringDistanceContentVisibleState = measuringDistanceContentVisibleState,
                toNextContent = {
                    mChartViewModel.updateIsMeasuringDistanceContentVisible(false)
                    mChartViewModel.updateIsMChartContentVisible(true)
                },
                selectedTestType = TestType.MChart,
                isLeftEye = mChartViewModel.isLeftEye.collectAsState().value
            )
            MChartContent(
                mChartContentVisibleState = mChartContentVisibleState,
                toResultScreen = toResultScreen
            )
        }
    }
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MChartContent(
    mChartContentVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: (MChartTestResult) -> Unit,
    mChartViewModel: MChartViewModel = hiltViewModel()
) {
    AnimatedVisibility(
        visibleState = mChartContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {

        val context = LocalContext.current
        val exoPlayer = mChartViewModel.exoPlayer
        val coroutineScope = rememberCoroutineScope()
        val isTTSSpeaking = mChartViewModel.isTTSSpeaking.collectAsState().value
        val isTesting = mChartViewModel.isTesting.collectAsState().value

        LaunchedEffect(true) {TTS.setOnDoneListener {
            mChartViewModel.updateIsTTSSpeaking(false)
            mChartViewModel.updateIsTesting(true)
        }
            TTS.speechTTS("아래의 직선을 바라보고, 고든 선으로 보이는지 휘어진 선으로 보이는지 선택해주세요.", TextToSpeech.QUEUE_ADD)
            TTS.speechTTS("검사를 시작하겠습니다.", TextToSpeech.QUEUE_ADD)
        }
        DisposableEffect(true) {
            onDispose {
                TTS.clearOnDoneListener()
            }
        }
        FaceDetection()
        val isLeftEye = mChartViewModel.isLeftEye.collectAsState().value
        val isVertical = mChartViewModel.isVertical.collectAsState().value
        val currentLevel = mChartViewModel.currentLevel.collectAsState().value
        val imageId = mChartViewModel.mChartImageId.collectAsState().value
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 40.dp),
                text = StringProvider.getString(R.string.mchart_test_description),
                fontSize = 32.sp,
                color = Color(0xffffffff),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier
                    .width(700.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                ) {
                    if (isTTSSpeaking && !isTesting) {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxSize(),
                            factory = {
                                PlayerView(context).apply {
                                    useController = false
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                    player = exoPlayer
                                    exoPlayer.setMediaItem(MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.mchart_video_2)))
                                    exoPlayer.prepare()
                                    exoPlayer.pause()
                                    coroutineScope.launch {
                                        delay(4000)
                                        exoPlayer.play()
                                    }
                                }
                            }
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .padding(40.dp)
                                .fillMaxSize()
                                .rotate(
                                    when (isVertical) {
                                        true -> 0f
                                        else -> 90f
                                    }
                                ),
                            painter = painterResource(id = imageId),
                            contentDescription = ""
                        )
                    }
                }
            }
            if (!isTTSSpeaking) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 40.dp,
                                    bottom = 20.dp
                                )
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0xff1d71e1),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {
                                    if (isVertical && isLeftEye) {
                                        mChartViewModel.updateLeftVerticalValue()
                                        mChartViewModel.updateCurrentLevel(0)
                                        mChartViewModel.updateIsVertical(false)
                                    } else if (!isVertical && isLeftEye) {
                                        mChartViewModel.updateLeftHorizontalValue()
                                        mChartViewModel.toNextMChartTest()
                                        mChartViewModel.updateIsMChartContentVisible(false)
                                        mChartViewModel.updateIsMeasuringDistanceContentVisible(true)
                                    } else if (isVertical) {
                                        mChartViewModel.updateRightVerticalValue()
                                        mChartViewModel.updateCurrentLevel(0)
                                        mChartViewModel.updateIsVertical(false)
                                    } else {
                                        TTS.speechTTS(
                                            "검사가 완료되었습니다. 결과가 나올 때 까지 잠시 기다려주세요.",
                                            TextToSpeech.QUEUE_ADD
                                        )
                                        mChartViewModel.updateRightHorizontalValue()
                                        toResultScreen(mChartViewModel.getMChartTestResult())
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 4.dp),
                                text = StringProvider.getString(R.string.mchart_test_content_straight),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xffffffff)
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
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {
                                    if (currentLevel >= 19) {
                                        if (isVertical && isLeftEye) {
                                            mChartViewModel.updateLeftVerticalValue()
                                            mChartViewModel.updateCurrentLevel(0)
                                            mChartViewModel.updateIsVertical(false)
                                        } else if (!isVertical && isLeftEye) {
                                            mChartViewModel.updateLeftHorizontalValue()
                                            mChartViewModel.toNextMChartTest()
                                            mChartViewModel.updateIsMChartContentVisible(false)
                                            mChartViewModel.updateIsMeasuringDistanceContentVisible(true)
                                        } else if (isVertical) {
                                            mChartViewModel.updateRightVerticalValue()
                                            mChartViewModel.updateCurrentLevel(0)
                                            mChartViewModel.updateIsVertical(false)
                                        } else {
                                            TTS.speechTTS(
                                                "검사가 완료되었습니다. 결과가 나올 때 까지 잠시 기다려주세요.",
                                                TextToSpeech.QUEUE_ADD
                                            )
                                            mChartViewModel.updateRightHorizontalValue()
                                            toResultScreen(mChartViewModel.getMChartTestResult())
                                        }
                                    } else {
                                        mChartViewModel.updateCurrentLevel(currentLevel + 1)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(bottom = 4.dp),
                                text = StringProvider.getString(R.string.mchart_test_content_bent),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xffffffff)
                            )
                        }
                    }
                }
            }
        }
    }
}