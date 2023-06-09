package com.pixelro.nenoonkiosk.ui.testresultcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.StringProvider

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
        Text(
            modifier = Modifier
                .padding(start = 40.dp, top = 40.dp)
                .fillMaxWidth(),
            text = StringProvider.getString(R.string.test_result_normal_case),
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xff1d71e1)
        )
        Text(
            modifier = Modifier
                .padding(start = 40.dp)
                .fillMaxWidth(),
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
                text = StringProvider.getString(R.string.left_and_right),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .background(
                        color = Color(0xffdcebff),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                text = StringProvider.getString(R.string.vertical),
                color = Color(0xff1d71e1),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(
                        color = Color(0xfffff8de),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                text = StringProvider.getString(R.string.horizontal),
                color = Color(0xffffb800),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = "0.0°",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            modifier = Modifier
                .padding(start = 40.dp, top = 40.dp)
                .fillMaxWidth(),
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
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = Color(0xffdcebff),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                        text = StringProvider.getString(R.string.vertical),
                        color = Color(0xff1d71e1),
                        fontSize = 20.sp
                    )
                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 100.dp),
                            text = "${String.format("%.1f", result[0].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = Color(0xfffff8de),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                        text = StringProvider.getString(R.string.horizontal),
                        color = Color(0xffffb800),
                        fontSize = 20.sp
                    )
                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 100.dp),
                            text = "${String.format("%.1f", result[1].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
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
                    text = StringProvider.getString(R.string.right),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = Color(0xffdcebff),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                        text = StringProvider.getString(R.string.vertical),
                        color = Color(0xff1d71e1),
                        fontSize = 20.sp
                    )
                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 100.dp),
                            text = "${String.format("%.1f", result[2].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = Color(0xfffff8de),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp),
                        text = StringProvider.getString(R.string.horizontal),
                        color = Color(0xffffb800),
                        fontSize = 20.sp
                    )
                    Box(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 100.dp),
                            text = "${String.format("%.1f", result[3].toFloat() / 10)}°",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}