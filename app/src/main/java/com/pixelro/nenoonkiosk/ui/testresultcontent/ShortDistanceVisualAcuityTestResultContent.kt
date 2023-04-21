package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.VisionDisorderType

@Composable
fun ShortDistanceVisualAcuityTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Text(
            text = "시력 검사 결과",
            fontSize = 30.sp,
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        Text(
            text = "측정 거리:",
            fontSize = 30.sp
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
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
                        text = "${viewModel.leftEyeSightValue.collectAsState().value}",
                        fontSize = 30.sp,
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
                        },
                        fontSize = 30.sp,
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
                        text = "${viewModel.rightEyeSightValue.collectAsState().value}",
                        fontSize = 30.sp,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(20.dp)
                .background(
                    color = Color(0xff0000ff),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "돌아가기",
                fontSize = 30.sp,
                color = Color(0xffffffff),
                textAlign = TextAlign.Center
            )
        }
    }
}