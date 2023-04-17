package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import java.util.*
import kotlin.math.roundToInt

@Composable
fun VisualAcuityTestCommonContent(
    viewModel: NenoonViewModel,
    thirdVisibleState: MutableTransitionState<Boolean>,
    fourthVisibleState: MutableTransitionState<Boolean>
) {
    Log.e("VisualAcuityTestCommonContent", "VisualAcuityTestCommonContent")
    val sightValue = viewModel.sightValue.collectAsState().value
    val randomList = viewModel.randomList.collectAsState().value
    val ansNum = viewModel.ansNum.collectAsState().value

    AnimatedVisibility(
        visibleState = thirdVisibleState,
        enter = slideIn(
            animationSpec = tween(durationMillis = 500),
            initialOffset = { IntOffset(it.width, 0) }
        ) + fadeIn(),
        exit = slideOut(
            animationSpec = tween(durationMillis = 500),
            targetOffset = { IntOffset(-it.width, 0) }
        ) + fadeOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            Text(
                text = "측정 거리: ${viewModel.testDistance.collectAsState().value / 10}",
                fontSize = 30.sp,
                color = Color(0xffffffff)
            )
            Text(
                modifier = Modifier
                    .padding(top = 20.dp),
                text = "현재 거리: ${(viewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
                fontSize = 30.sp,
                color = Color(0xffffffff)
            )
            Text(
                text = "보이는 것을 선택해주세요.",
                fontSize = 30.sp,
                color = Color(0xffffffff)
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
                                if(ansNum == randomList[0]) {
                                    viewModel.updateSightValue(sightValue + 0.1f)
                                } else {
                                    viewModel.updateSightValue(sightValue - 0.1f)
                                }
                                viewModel.updateRandomList()
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
                            ),
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
                            ),
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
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                }
            }
        }
    }
}