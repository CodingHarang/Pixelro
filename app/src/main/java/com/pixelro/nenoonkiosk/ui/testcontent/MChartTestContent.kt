package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MChartTestContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    val measuringDistanceContentVisibleState = viewModel.measuringDistanceContentVisibleState
    val coveredEyeCheckingContentVisibleState = viewModel.coveredEyeCheckingContentVisibleState
    val mChartContentVisibleState = viewModel.mChartContentVisibleState

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
                viewModel = viewModel,
                measuringDistanceContentVisibleState = measuringDistanceContentVisibleState,
                nextVisibleState = coveredEyeCheckingContentVisibleState
            )
            CoveredEyeCheckingContent(
                viewModel = viewModel,
                coveredEyeCheckingContentVisibleState = coveredEyeCheckingContentVisibleState,
                nextVisibleState = mChartContentVisibleState
            )
            MChartContent(
                viewModel = viewModel,
                mChartContentVisibleState = mChartContentVisibleState,
                nextVisibleState = coveredEyeCheckingContentVisibleState,
                toResultScreen = toResultScreen
            )
        }
    }
}

@Composable
fun MChartContent(
    viewModel: NenoonViewModel,
    mChartContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: () -> Unit
) {
    AnimatedVisibility(
        visibleState = mChartContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        FaceDetection(
            viewModel = viewModel
        )
        val isLeftEye = viewModel.isLeftEye.collectAsState().value
        val isVertical = viewModel.isVertical.collectAsState().value
        val currentLevel = viewModel.currentLevel.collectAsState().value
        val imageId = viewModel.mChartImageId.collectAsState().value
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
                                viewModel.updateMChartResult(currentLevel)
                                if (isVertical && isLeftEye) {
                                    viewModel.updateCurrentLevel(0)
                                    viewModel.updateIsVertical(false)
                                } else if (!isVertical && isLeftEye) {
                                    viewModel.toNextMChartTest()
                                    mChartContentVisibleState.targetState = false
                                    nextVisibleState.targetState = true
                                } else if (isVertical) {
                                    viewModel.updateCurrentLevel(0)
                                    viewModel.updateIsVertical(false)
                                } else {
                                    viewModel.updateSavedResult()
                                    toResultScreen()
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
                                bottom = (viewModel.navigationBarPadding.collectAsState().value).dp
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
                                    viewModel.updateMChartResult(currentLevel)
                                    viewModel.updateCurrentLevel(0)
                                    if (isVertical && isLeftEye) {
                                        viewModel.updateIsVertical(false)
                                    } else if (!isVertical && isLeftEye) {
                                        viewModel.toNextMChartTest()
                                        mChartContentVisibleState.targetState = false
                                        nextVisibleState.targetState = true
                                    } else if (isVertical) {
                                        viewModel.updateIsVertical(false)
                                    } else {
                                        viewModel.updateSavedResult()
                                        toResultScreen()
                                    }
                                } else {
                                    viewModel.updateCurrentLevel(currentLevel + 1)
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