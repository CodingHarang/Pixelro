package com.pixelro.nenoonkiosk.test.visualacuity.shortdistance

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.SharedPreferencesManager
import hilt_aggregated_deps._com_pixelro_nenoonkiosk_di_SharedPreferencesDataSourceModule

@Composable
fun ShortDistanceVisualAcuityTestResultContent(
    testResult: ShortVisualAcuityTestResult,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(start = 40.dp, top = 40.dp),
            text = StringProvider.getString(R.string.test_result_normal_case),
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xff1d71e1)
        )
        Text(
            modifier = Modifier
                .padding(start = 40.dp),
            text = StringProvider.getString(R.string.test_result_normal_bottom),
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
                text = StringProvider.getString(R.string.left_and_right_0_7_or_higher),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
//            Text(
//                modifier = Modifier
//                    .padding(start = 12.dp)
//                    .background(
//                        color = Color(0xffffecec),
//                        shape = RoundedCornerShape(4.dp)
//                    )
//                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                text = StringProvider.getString(R.string.red),
//                color = Color(0xffd23d3d),
//                fontSize = 20.sp
//            )
//            Text(
//                modifier = Modifier
//                    .padding(start = 8.dp)
//                    .background(
//                        color = Color(0xffdafcda),
//                        shape = RoundedCornerShape(4.dp)
//                    )
//                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                text = StringProvider.getString(R.string.green),
//                color = Color(0xff28bd29),
//                fontSize = 20.sp
//            )
//            Text(
//                modifier = Modifier
//                    .padding(start = 12.dp),
//                text = StringProvider.getString(R.string.test_result_all_seems_clear),
//                color = Color(0xff878787),
//                fontSize = 20.sp
//            )
        }
        Text(
            modifier = Modifier
                .padding(start = 40.dp, top = 40.dp),
            text = StringProvider.getString(R.string.test_result_my_result),
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
                    text = StringProvider.getString(R.string.left),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = when(SharedPreferencesManager.getString("language")) {
                        "ko" -> "${testResult.leftEye.toFloat() / 10}"
                        "en" -> "20/" + "${(200 / testResult.leftEye).toInt()}"
                        "zh" -> "${4 + (testResult.leftEye.toFloat() / 10)}"
                        "ja" -> "${testResult.leftEye.toFloat() / 10}"
                        else -> "${testResult.leftEye.toFloat() / 10}"
                    },
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    when(viewModel.leftEyeSightedValue.collectAsState().value) {
//                        VisionDisorderType.Hyperopia -> {
//                            Text(
//                                modifier = Modifier
//                                    .background(
//                                        color = Color(0xffdafcda),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.green),
//                                color = Color(0xff28bd29),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 12.dp),
//                                text = StringProvider.getString(R.string.test_result_seems_more_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                        VisionDisorderType.Myopia -> {
//                            Text(
//                                modifier = Modifier
//                                    .background(
//                                        color = Color(0xffffecec),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.red),
//                                color = Color(0xffd23d3d),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 12.dp),
//                                text = StringProvider.getString(R.string.test_result_seems_more_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                        VisionDisorderType.Normal -> {
//                            Text(
//                                modifier = Modifier
//                                    .background(
//                                        color = Color(0xffffecec),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.red),
//                                color = Color(0xffd23d3d),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 8.dp)
//                                    .background(
//                                        color = Color(0xffdafcda),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.green),
//                                color = Color(0xff28bd29),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 12.dp),
//                                text = StringProvider.getString(R.string.test_result_all_seems_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                        VisionDisorderType.Astigmatism -> {
//                            Text(
//                                text = StringProvider.getString(R.string.test_result_seems_no_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                    }
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
                    text = StringProvider.getString(R.string.right),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = when(SharedPreferencesManager.getString("language")) {
                        "ko" -> "${testResult.rightEye.toFloat() / 10}"
                        "en" -> "20/" + "${(200 / testResult.rightEye).toInt()}"
                        "zh" -> "${4 + (testResult.rightEye.toFloat() / 10)}"
                        "ja" -> "${testResult.rightEye.toFloat() / 10}"
                        else -> "${testResult.rightEye.toFloat() / 10}"
                    },
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    when(viewModel.rightEyeSightedValue.collectAsState().value) {
//                        VisionDisorderType.Hyperopia -> {
//                            Text(
//                                modifier = Modifier
//                                    .background(
//                                        color = Color(0xffdafcda),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.green),
//                                color = Color(0xff28bd29),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 12.dp),
//                                text = StringProvider.getString(R.string.test_result_seems_more_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                        VisionDisorderType.Myopia -> {
//                            Text(
//                                modifier = Modifier
//                                    .background(
//                                        color = Color(0xffffecec),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.red),
//                                color = Color(0xffd23d3d),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 12.dp),
//                                text = StringProvider.getString(R.string.test_result_seems_more_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                        VisionDisorderType.Normal -> {
//                            Text(
//                                modifier = Modifier
//                                    .background(
//                                        color = Color(0xffffecec),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.red),
//                                color = Color(0xffd23d3d),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 8.dp)
//                                    .background(
//                                        color = Color(0xffdafcda),
//                                        shape = RoundedCornerShape(4.dp)
//                                    )
//                                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
//                                text = StringProvider.getString(R.string.green),
//                                color = Color(0xff28bd29),
//                                fontSize = 20.sp
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .padding(start = 12.dp),
//                                text = StringProvider.getString(R.string.test_result_all_seems_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                        VisionDisorderType.Astigmatism -> {
//                            Text(
//                                text = StringProvider.getString(R.string.test_result_seems_no_clear),
//                                color = Color(0xff878787),
//                                fontSize = 20.sp
//                            )
//                        }
//                    }
                }
            }
        }
    }
}