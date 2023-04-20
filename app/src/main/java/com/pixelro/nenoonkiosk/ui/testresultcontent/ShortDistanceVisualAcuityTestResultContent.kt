package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.VisionDisorderType

@Composable
fun ShortDistanceVisualAcuityTestResultContent(
    viewModel: NenoonViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "시력 검사 결과",
            fontSize = 30.sp
        )
        Text(
            text = "측정 거리:",
            fontSize = 30.sp
        )
        Row() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "왼쪽",
                    color = Color(0xff0000ff),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(300.dp)
                        .background(
                            color = Color(0xffdddddd),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${viewModel.leftEyeSightValue.collectAsState().value}"
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(300.dp)
                        .background(
                            color = Color(0xffdddddd),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when(viewModel.leftEyeSightedValue.collectAsState().value) {
                            VisionDisorderType.Normal -> "정상"
                            VisionDisorderType.Myopia -> "근시"
                            VisionDisorderType.Hyperopia -> "원시"
                            VisionDisorderType.Astigmatism -> "난시"
                            else -> "오류"
                        }
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .width(12.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "오른쪽",
                    color = Color(0xff0000ff),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(300.dp)
                        .background(
                            color = Color(0xffdddddd),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${viewModel.rightEyeSightValue.collectAsState().value}"
                    )
                }
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(300.dp)
                        .background(
                            color = Color(0xffdddddd),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when(viewModel.rightEyeSightedValue.collectAsState().value) {
                            VisionDisorderType.Normal -> "정상"
                            VisionDisorderType.Myopia -> "근시"
                            VisionDisorderType.Hyperopia -> "원시"
                            VisionDisorderType.Astigmatism -> "난시"
                            else -> "오류"
                        },
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}