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
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.VisionDisorderType
import com.pixelro.nenoonkiosk.R

@Composable
fun ShortDistanceVisualAcuityTestResultContent(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    viewModel.updateSightTestResult()
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp, top = 40.dp),
            text = "이상 없을 경우",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = "눈에 이상이 없을 경우 아래와 같은 결과가 나옵니다",
            fontSize = 16.sp,
            color = Color(0xff878787)
        )
        Row(
            modifier = Modifier
                .padding(start = 40.dp, top = 20.dp, end = 40.dp)
                .fillMaxWidth()
                .background(
                    color = Color(0xfff7f9f9),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "오른쪽 · 왼쪽 0.7 이상",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .background(
                        color = Color(0xffffecec),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                text = "빨강",
                color = Color(0xffd23d3d),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(
                        color = Color(0xffdafcda),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                text = "초록",
                color = Color(0xff28bd29),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "바탕의 글이 모두 잘 보임",
                color = Color(0xff878787),
                fontSize = 20.sp
            )
        }
        Text(
            modifier = Modifier
                .padding(start = 40.dp, top = 40.dp),
            text = "내 결과",
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier
                .padding(start = 40.dp, top = 20.dp, end = 40.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp)
                    .background(
                        color = Color(0xfff7f9f9),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(20.dp)
            ) {
                Text(
                    text = "왼쪽",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${viewModel.leftEyeSightValue.collectAsState().value.toFloat() / 10}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when(viewModel.leftEyeSightedValue.collectAsState().value) {
                        VisionDisorderType.Hyperopia -> {
                            Text(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xffdafcda),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "초록",
                                color = Color(0xff28bd29),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp),
                                text = "바탕의 글이 선명하게 보임",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                        VisionDisorderType.Myopia -> {
                            Text(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xffffecec),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "빨강",
                                color = Color(0xffd23d3d),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp),
                                text = "바탕의 글이 선명하게 보임",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                        VisionDisorderType.Normal -> {
                            Text(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xffffecec),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "빨강",
                                color = Color(0xffd23d3d),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .background(
                                        color = Color(0xffdafcda),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "초록",
                                color = Color(0xff28bd29),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp),
                                text = "바탕의 글이 모두 선명하게 보임",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                        VisionDisorderType.Astigmatism -> {
                            Text(
                                text = "글이 모두 선명하게 보이지 않음",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier
                .width(20.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp)
                    .background(
                        color = Color(0xfff7f9f9),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(20.dp)
            ) {
                Text(
                    text = "오른쪽",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${viewModel.rightEyeSightValue.collectAsState().value.toFloat() / 10}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when(viewModel.rightEyeSightedValue.collectAsState().value) {
                        VisionDisorderType.Hyperopia -> {
                            Text(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xffdafcda),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "초록",
                                color = Color(0xff28bd29),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp),
                                text = "바탕의 글이 선명하게 보임",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                        VisionDisorderType.Myopia -> {
                            Text(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xffffecec),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "빨강",
                                color = Color(0xffd23d3d),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp),
                                text = "바탕의 글이 선명하게 보임",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                        VisionDisorderType.Normal -> {
                            Text(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xffffecec),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "빨강",
                                color = Color(0xffd23d3d),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .background(
                                        color = Color(0xffdafcda),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                                text = "초록",
                                color = Color(0xff28bd29),
                                fontSize = 20.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 12.dp),
                                text = "바탕의 글이 모두 선명하게 보임",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                        VisionDisorderType.Astigmatism -> {
                            Text(
                                text = "글이 모두 선명하게 보이지 않음",
                                color = Color(0xff878787),
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(100.dp)
//                .padding(20.dp)
//                .background(
//                    color = Color(0xff0000ff),
//                    shape = RoundedCornerShape(8.dp)
//                )
//                .clickable {
//                    navController.popBackStack(GlobalConstants.ROUTE_TEST_LIST, false)
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                text = "돌아가기",
//                fontSize = 30.sp,
//                color = Color(0xffffffff),
//                textAlign = TextAlign.Center
//            )
//        }
    }
}