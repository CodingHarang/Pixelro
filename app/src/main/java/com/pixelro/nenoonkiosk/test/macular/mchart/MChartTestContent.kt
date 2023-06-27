package com.pixelro.nenoonkiosk.test.macular.mchart

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.CoveredEyeCheckingContent
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionWithPreview
import com.pixelro.nenoonkiosk.facedetection.MeasuringDistanceContent

@Composable
fun MChartTestContent(
    toResultScreen: (MChartTestResult) -> Unit,
    mChartViewModel: MChartViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        mChartViewModel.init()
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
                        .padding(40.dp)
                ) {
//                Text(
//                    text = "$currentLevel",
//                    fontSize = 30.sp
//                )
                    Image(
                        modifier = Modifier
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
                            .padding(start = 40.dp, end = 40.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = Color(0x40ffffff),
                                shape = RoundedCornerShape(8.dp)
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
                                    mChartViewModel.updateRightHorizontalValue()
                                    toResultScreen(mChartViewModel.getMChartTestResult())
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(20.dp),
                            text = StringProvider.getString(R.string.mchart_test_content_straight),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xffffffff)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 40.dp,
                                end = 40.dp,
                                bottom = GlobalValue.navigationBarPadding.dp
                            )
                            .fillMaxWidth()
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = Color(0x40ffffff),
                                shape = RoundedCornerShape(8.dp)
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
                                .padding(20.dp),
                            text = StringProvider.getString(R.string.mchart_test_content_bent),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xffffffff)
                        )
                    }
                }
            }
        }
    }
}