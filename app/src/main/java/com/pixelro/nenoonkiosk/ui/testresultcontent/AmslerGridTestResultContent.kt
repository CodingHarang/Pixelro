package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun AmslerGridTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    val leftSelectedArea = viewModel.leftSelectedArea.collectAsState().value
    val rightSelectedArea = viewModel.rightSelectedArea.collectAsState().value
//    Text(
//        text = "${leftSelectedArea}\n ${rightSelectedArea}"
//    )
    Row(
        modifier = Modifier
            .width(650.dp)
            .height(380.dp)
            .padding(top = 80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        ) {
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .weight(1f)
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[0]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[1]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[2]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
            }
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .weight(1f)
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[3]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[4]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[5]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
            }
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .weight(1f)
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[6]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[7]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(leftSelectedArea[8]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
            }
        }
        Spacer(
            modifier = Modifier
                .width(50.dp)
        )
        Column(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
        ) {
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .weight(1f)
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[0]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[1]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[2]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
            }
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .weight(1f)
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[3]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[4]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[5]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
            }
            Row(
                modifier = Modifier
                    .width(300.dp)
                    .weight(1f)
                    .height(100.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[6]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[7]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .background(
                            color = when(rightSelectedArea[8]) {
                                true -> Color(0xffff0000)
                                else -> Color(0xffffffff)
                            }
                        )
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {

                }
            }
        }
    }
}