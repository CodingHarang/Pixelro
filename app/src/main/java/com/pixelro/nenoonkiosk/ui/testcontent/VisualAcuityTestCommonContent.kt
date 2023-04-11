package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import java.util.*
import kotlin.math.roundToInt

@Composable
fun VisualAcuityTestCommonContent(
    viewModel: NenoonViewModel,
    thirdVisibleState: MutableTransitionState<Boolean>,
    fourthVisibleState: MutableTransitionState<Boolean>,
    testDistance: MutableState<Int>
) {
    val sightValue = remember { mutableStateOf(0.1) }
    val randomList = listOf {
        val list = mutableListOf<Int>()
        var ranNum = (2..7).random()
        for(i in 1..3) {
            while(ranNum in list) {
                ranNum = (2..7).random()
                list.add(ranNum)
            }
        }
        list
    }
    Log.e("list", "${}")

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
        var eyesightValue = remember { mutableStateOf(0.1) }
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

            }
            Text(
                text = "측정 거리: ${testDistance.value / 10}",
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
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .background(
                                color = Color(0xffffffff),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                }
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .background(
                                color = Color(0xffffffff),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                }
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                            .background(
                                color = Color(0xffffffff),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                    }
                }
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
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