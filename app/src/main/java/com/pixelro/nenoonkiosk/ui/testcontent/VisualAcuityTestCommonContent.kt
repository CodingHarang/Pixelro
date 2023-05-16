package com.pixelro.nenoonkiosk.ui.testcontent

import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.VisionDisorderType
import java.util.*
import kotlin.math.roundToInt

@Composable
fun VisualAcuityTestCommonContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel,
    visualAcuityTestCommonContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>
) {
    Log.e("VisualAcuityTestCommonContent", "VisualAcuityTestCommonContent")
    val randomList = viewModel.randomList.collectAsState().value
    val ansNum = viewModel.ansNum.collectAsState().value
    val isLeftEye = viewModel.isLeftEye.collectAsState().value

    Box() {

        AnimatedVisibility(
            visibleState = visualAcuityTestCommonContentVisibleState,
            enter = slideIn(
                animationSpec = tween(durationMillis = 500),
                initialOffset = { IntOffset(100, 0) }
            ) + fadeIn(),
            exit = slideOut(
                animationSpec = tween(durationMillis = 500),
                targetOffset = { IntOffset(-100, 0) }
            ) + fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                    Text(
//                        text = "시력: ${viewModel.sightLevel.collectAsState().value.toFloat() / 10}",
//                        color = Color(0xffffffff),
//                        fontSize = 40.sp
//                    )
                Box(
                    modifier = Modifier
                        .height(400.dp)
                        .width(400.dp)
                        .background(
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id =
                            when(ansNum) {
                                2 -> when(sightLevel) {
                                    1 -> R.drawable._50cm_2_1
                                    2 -> R.drawable._50cm_2_2
                                    3 -> R.drawable._50cm_2_3
                                    4 -> R.drawable._50cm_2_4
                                    5 -> R.drawable._50cm_2_5
                                    6 -> R.drawable._50cm_2_6
                                    7 -> R.drawable._50cm_2_7
                                    8 -> R.drawable._50cm_2_8
                                    9 -> R.drawable._50cm_2_9
                                    else -> R.drawable._50cm_2_10
                                }
                                3 -> when(sightLevel) {
                                    1 -> R.drawable._50cm_3_1
                                    2 -> R.drawable._50cm_3_2
                                    3 -> R.drawable._50cm_3_3
                                    4 -> R.drawable._50cm_3_4
                                    5 -> R.drawable._50cm_3_5
                                    6 -> R.drawable._50cm_3_6
                                    7 -> R.drawable._50cm_3_7
                                    8 -> R.drawable._50cm_3_8
                                    9 -> R.drawable._50cm_3_9
                                    else -> R.drawable._50cm_3_10
                                }
                                4 -> when(sightLevel) {
                                    1 -> R.drawable._50cm_4_1
                                    2 -> R.drawable._50cm_4_2
                                    3 -> R.drawable._50cm_4_3
                                    4 -> R.drawable._50cm_4_4
                                    5 -> R.drawable._50cm_4_5
                                    6 -> R.drawable._50cm_4_6
                                    7 -> R.drawable._50cm_4_7
                                    8 -> R.drawable._50cm_4_8
                                    9 -> R.drawable._50cm_4_9
                                    else -> R.drawable._50cm_4_10
                                }
                                5 -> when(sightLevel) {
                                    1 -> R.drawable._50cm_5_1
                                    2 -> R.drawable._50cm_5_2
                                    3 -> R.drawable._50cm_5_3
                                    4 -> R.drawable._50cm_5_4
                                    5 -> R.drawable._50cm_5_5
                                    6 -> R.drawable._50cm_5_6
                                    7 -> R.drawable._50cm_5_7
                                    8 -> R.drawable._50cm_5_8
                                    9 -> R.drawable._50cm_5_9
                                    else -> R.drawable._50cm_5_10
                                }
                                6 -> when(sightLevel) {
                                    1 -> R.drawable._50cm_6_1
                                    2 -> R.drawable._50cm_6_2
                                    3 -> R.drawable._50cm_6_3
                                    4 -> R.drawable._50cm_6_4
                                    5 -> R.drawable._50cm_6_5
                                    6 -> R.drawable._50cm_6_6
                                    7 -> R.drawable._50cm_6_7
                                    8 -> R.drawable._50cm_6_8
                                    9 -> R.drawable._50cm_6_9
                                    else -> R.drawable._50cm_6_10
                                }
                                else -> when(sightLevel) {
                                    1 -> R.drawable._50cm_7_1
                                    2 -> R.drawable._50cm_7_2
                                    3 -> R.drawable._50cm_7_3
                                    4 -> R.drawable._50cm_7_4
                                    5 -> R.drawable._50cm_7_5
                                    6 -> R.drawable._50cm_7_6
                                    7 -> R.drawable._50cm_7_7
                                    8 -> R.drawable._50cm_7_8
                                    9 -> R.drawable._50cm_7_9
                                    else -> R.drawable._50cm_7_10
                                }
                            }
                        ),
                        contentDescription = ""
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Text(
                    text = "측정 거리: ${viewModel.testDistance.collectAsState().value / 10}cm",
                    fontSize = 30.sp,
                    color = Color(0xffffffff)
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Text(
                    text = "현재 거리: ${(viewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
                    fontSize = 30.sp,
                    color = Color(0xffffffff),
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Text(
                    text = "보이는 것을 선택해주세요.",
                    fontSize = 40.sp,
                    color = Color(0xffffffff),
                    fontWeight = FontWeight.Bold
                )
                Row() {
                    // 1
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                                .background(
                                    color = Color(0xffffffff),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    viewModel.processAnswerSelected(0)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(100.dp),
                                imageVector = ImageVector.vectorResource(id =
                                when(randomList[0]) {
                                    2 -> R.drawable.two
                                    3 -> R.drawable.three
                                    4 -> R.drawable.four
                                    5 -> R.drawable.five
                                    6 -> R.drawable.six
                                    else -> R.drawable.seven
                                }
                                ),
                                contentDescription = ""
                            )
                        }
                    }
                    // 2
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                                .background(
                                    color = Color(0xffffffff),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    viewModel.processAnswerSelected(1)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(100.dp),
                                imageVector = ImageVector.vectorResource(id =
                                when(randomList[1]) {
                                    2 -> R.drawable.two
                                    3 -> R.drawable.three
                                    4 -> R.drawable.four
                                    5 -> R.drawable.five
                                    6 -> R.drawable.six
                                    else -> R.drawable.seven
                                }
                                ),
                                contentDescription = ""
                            )
                        }
                    }
                    // 3
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                                .background(
                                    color = Color(0xffffffff),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    viewModel.processAnswerSelected(2)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(100.dp),
                                imageVector = ImageVector.vectorResource(id =
                                when(randomList[2]) {
                                    2 -> R.drawable.two
                                    3 -> R.drawable.three
                                    4 -> R.drawable.four
                                    5 -> R.drawable.five
                                    6 -> R.drawable.six
                                    else -> R.drawable.seven
                                }
                                ),
                                contentDescription = ""
                            )
                        }
                    }
                    // 4
                    Box(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(150.dp)
                                .width(150.dp)
                                .background(
                                    color = Color(0xffffffff),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    viewModel.processAnswerSelected(3)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .height(150.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.question_mark),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SightednessTestContent(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel,
    visualAcuityTestCommonContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>
) {
    val isSightednessTesting = viewModel.isSightednessTesting.collectAsState().value
    val isLeftEye = viewModel.isLeftEye.collectAsState().value
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideIn(
            animationSpec = tween(durationMillis = 500),
            initialOffset = { IntOffset(100, 0) }
        ) + fadeIn(),
        exit = slideOut(
            animationSpec = tween(durationMillis = 500),
            targetOffset = { IntOffset(-100, 0) }
        ) + fadeOut()
    ) {

    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(500.dp)
                        .width(250.dp)
                        .background(
                            color = Color(0xff00ff00),
                            shape = RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 20.dp
                            )
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        text = "초록",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
                Box(
                    modifier = Modifier
                        .height(500.dp)
                        .width(250.dp)
                        .background(
                            color = Color(0xffff0000),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 20.dp,
                                bottomEnd = 20.dp,
                                bottomStart = 0.dp
                            )
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        text = "빨강",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Text(
            text = "글자가 더 잘 보이는 쪽을 고르세요.",
            color = Color(0xffffffff),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp)
                    .height(80.dp)
                    .background(
                        color = Color(0xff00ff00),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (isLeftEye) {
                            viewModel.updateLeftEyeSightedValue(VisionDisorderType.Hyperopia)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(false)
                            visualAcuityTestCommonContentVisibleState.targetState = false
                            toResultScreen()
                        } else {
                            viewModel.updateRightEyeSightedValue(VisionDisorderType.Hyperopia)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(true)
                            nextVisibleState.targetState = true
                            visualAcuityTestCommonContentVisibleState.targetState = false
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "초록",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp)
                    .height(80.dp)
                    .background(
                        color = Color(0xffff0000),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (isLeftEye) {
                            viewModel.updateLeftEyeSightedValue(VisionDisorderType.Myopia)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(false)
                            visualAcuityTestCommonContentVisibleState.targetState = false
                            toResultScreen()
                        } else {
                            nextVisibleState.targetState = true
                            viewModel.updateRightEyeSightedValue(VisionDisorderType.Myopia)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(true)
                            visualAcuityTestCommonContentVisibleState.targetState = false
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "빨강",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp)
                    .height(80.dp)
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (isLeftEye) {
                            viewModel.updateLeftEyeSightedValue(VisionDisorderType.Normal)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(false)
                            visualAcuityTestCommonContentVisibleState.targetState = false
                            toResultScreen()
                        } else {
                            nextVisibleState.targetState = true
                            viewModel.updateRightEyeSightedValue(VisionDisorderType.Normal)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(true)
                            visualAcuityTestCommonContentVisibleState.targetState = false
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "둘다 잘 보인다",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .width(200.dp)
                    .height(80.dp)
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        if (isLeftEye) {
                            viewModel.updateLeftEyeSightedValue(VisionDisorderType.Astigmatism)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(false)
                            visualAcuityTestCommonContentVisibleState.targetState = false
                            toResultScreen()
                        } else {
                            nextVisibleState.targetState = true
                            viewModel.updateRightEyeSightedValue(VisionDisorderType.Astigmatism)
                            viewModel.updateIsSightednessTesting(false)
                            viewModel.updateIsLeftEye(true)
                            visualAcuityTestCommonContentVisibleState.targetState = false
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "둘 다 뿌옇다",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}