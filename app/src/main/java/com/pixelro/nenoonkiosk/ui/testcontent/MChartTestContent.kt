package com.pixelro.nenoonkiosk.ui.testcontent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R

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
        Text(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp),
            text = "엠식 변형시 검사",
            color = Color(0xffffffff),
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold
        )
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
        enter = slideIn(
            animationSpec = tween(durationMillis = 500),
            initialOffset = { IntOffset(it.width, 0) }
        ) + fadeIn(),
        exit = slideOut(
            animationSpec = tween(durationMillis = 500),
            targetOffset = { IntOffset(-it.width, 0) }
        ) + fadeOut()
    ) {
        val isLeftEye = viewModel.isLeftEye.collectAsState().value
        val isVertical = viewModel.isVertical.collectAsState().value
        val currentLevel = viewModel.currentLevel.collectAsState().value
        val imageId = viewModel.mChartImageId.collectAsState().value
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
                        .rotate(90f),
                    painter = painterResource(id = imageId),
                    contentDescription = ""
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        viewModel.updateMChartResult(currentLevel)
                        viewModel.updateCurrentLevel(0)
                        if(isVertical && !isLeftEye) {
                            viewModel.updateIsVertical(false)
                        } else if(!isVertical && !isLeftEye) {
                            viewModel.updateIsVertical(true)
                            viewModel.updateIsLeftEye(true)
                        } else if(isVertical && isLeftEye) {
                            viewModel.updateIsVertical(false)
                        } else {
                            viewModel.updateIsVertical(true)
                        }
                    }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "곧은 선",
                    fontSize = 30.sp
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if(currentLevel >= 20) {
                            viewModel.updateMChartResult(currentLevel)
                            viewModel.updateCurrentLevel(0)
                            if(isVertical && !isLeftEye) {
                                viewModel.updateIsVertical(false)
                            } else if(!isVertical && !isLeftEye) {
                                viewModel.updateIsVertical(true)
                                viewModel.updateIsLeftEye(true)
                            } else if(isVertical && isLeftEye) {
                                viewModel.updateIsVertical(false)
                            } else {
                                viewModel.updateIsVertical(true)
                            }
                        } else {
                            viewModel.updateCurrentLevel(currentLevel + 1)
                        }
                    }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "휘어진 선",
                    fontSize = 30.sp
                )
            }
        }
    }
}