package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import kotlin.math.roundToInt

@Composable
fun MeasuringDistanceContent(
    viewModel: NenoonViewModel,
    measuringDistanceContentVisibleState: MutableTransitionState<Boolean>,
    nextVisibleState: MutableTransitionState<Boolean>
) {
    AnimatedVisibility(
        visibleState = measuringDistanceContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 40.dp, top = 120.dp, end = 40.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                text = StringProvider.getString(R.string.measuring_distance_content_description1),
                color = Color(0xffffffff),
                fontSize = 30.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 40.dp, end = 40.dp)
                    .fillMaxWidth(),
                text = StringProvider.getString(R.string.measuring_distance_content_description2),
                color = Color(0xffffffff),
                fontSize = 20.sp
            )
            Image(
                modifier = Modifier
                    .padding(start = 40.dp, top = 60.dp, end = 40.dp, bottom = 20.dp),
                painter = painterResource(id = R.drawable.img_eyetest_maneyes),
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .padding(top = 40.dp),
                text = StringProvider.getString(R.string.test_screen_current_distance),
                color = Color(0xffffffff),
                fontSize = 24.sp
            )
            Text(
                color = Color(0xffffffff),
                text = "${(viewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
                fontSize = 68.sp,
                fontWeight = FontWeight.Bold
            )
            if(viewModel.screenToFaceDistance.collectAsState().value in when(viewModel.selectedTestType.collectAsState().value) {
                    TestType.ShortDistanceVisualAcuity -> (350.0..450.0)
                    else -> (250.0..350.0)
                }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 40.dp,
                                end = 40.dp,
                                bottom = (viewModel.navigationBarPadding.collectAsState().value).dp
                            )
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = Color(0xff1d71e1),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .clickable {
                                viewModel.updateTestDistance()
                            }
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = StringProvider.getString(R.string.test_predescription_screen_start),
                            fontSize = 24.sp,
                            color = Color(0xffffffff),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 40.dp,
                                end = 40.dp,
                                bottom = (viewModel.navigationBarPadding.collectAsState().value + 120).dp
                            )
                            .border(
                                border = BorderStroke(1.dp, Color(0xffffffff)),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when(viewModel.selectedTestType.collectAsState().value) {
                                TestType.ShortDistanceVisualAcuity -> "40cm로 조정해주세요"
                                else -> "30cm로 조정해주세요"
                            },
                            fontSize = 24.sp,
                            color = Color(0xffffffff),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}