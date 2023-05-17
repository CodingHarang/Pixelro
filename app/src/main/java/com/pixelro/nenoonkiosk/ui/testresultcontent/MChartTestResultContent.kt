package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun MChartTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    val result = viewModel.savedResult.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "좌안",
                    fontSize = 30.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "수직검사",
                            fontSize = 30.sp
                        )
                        Text(
                            text = "${String.format("%.1f", result[0].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(60.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .height(40.dp)
                            .background(
                                color = Color(0xff000000)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .width(60.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "수평검사",
                            fontSize = 30.sp
                        )
                        Text(
                            text = "${String.format("%.1f", result[1].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .height(40.dp)
        )
        Row() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "우안",
                    fontSize = 30.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "수직검사",
                            fontSize = 30.sp
                        )
                        Text(
                            text = "${String.format("%.1f", result[2].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(60.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .height(40.dp)
                            .background(
                                color = Color(0xff000000)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .width(60.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "수평검사",
                            fontSize = 30.sp
                        )
                        Text(
                            text = "${String.format("%.1f", result[3].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    Text(
        text = "${result[2]}, ${result[3]}, ${result[0]}, ${result[1]}"
    )
}