package com.pixelro.nenoonkiosk.ui.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.MeasuringDistanceDialog
import com.pixelro.nenoonkiosk.ui.testlist.TestListContent
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun TestListScreen(
    checkIsTestDone: (TestType) -> Boolean,
    toTestScreen: (TestType) -> Unit,
    toIntroScreen: () -> Unit,
    isPresbyopiaDone: Boolean,
    isShortVisualAcuityDone: Boolean,
    isAmslerGridDone: Boolean,
    isMChartDone: Boolean,
) {
    val pagerState = rememberPagerState(
        initialPage = 50000
    )
    LaunchedEffect(true) {
        while(true) {
            delay(8000)
            pagerState.animateScrollToPage(
                page = (pagerState.currentPage + 1),
                animationSpec = tween(1000)
            )
        }
    }
    var isDialogShowing by remember { mutableStateOf(false) }
    var selectedTest by remember { mutableStateOf(TestType.None) }
    val transition = rememberInfiniteTransition()
    val shiftVal by transition.animateFloat(
        initialValue = 0f, targetValue = 40f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
            },
            repeatMode = RepeatMode.Reverse
        )
    )
    if(isDialogShowing) {
        SurveyRecommendationDialog(
            onDismissRequest = {
                isDialogShowing = false
            },
            toTestScreen = toTestScreen,
            toIntroScreen = toIntroScreen,
            selectedTest = selectedTest
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xffffffff)
            )
    ) {
        Box(
            modifier = Modifier
                .padding(
                    start = 40.dp,
                    top = (GlobalValue.statusBarPadding + 20).dp,
                    end = 40.dp,
                    bottom = 20.dp
                )
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable {
                        toIntroScreen()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .width(28.dp),
                    painter = painterResource(id = R.drawable.icon_back_black),
                    contentDescription = ""
                )
                Text(
                    text = "문진하러 가기",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = StringProvider.getString(R.string.test_list),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffebebeb)
                )
        )

        Box(
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                modifier = Modifier
                    .offset(x = 0.dp, y = shiftVal.dp),
                text = "아래에서 원하는 검사 항목을 눌러주세요",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        }

//        HorizontalPager(
//            contentPadding = PaddingValues(start = 40.dp, top = 20.dp, end = 40.dp, bottom = 20.dp),
//            pageSpacing = 40.dp,
//            state = pagerState,
//            pageCount = 100000,
//        ) {
//            Advertisement(it)
//        }
        Box() {
            TestListContent(
                checkIsTestDone = {
                    selectedTest = it
                    checkIsTestDone(it)
                },
                showSurveyRecommendationDialog = { isDialogShowing = true },
                toTestScreen = toTestScreen,
                isPresbyopiaDone = isPresbyopiaDone,
                isShortVisualAcuityDone = isShortVisualAcuityDone,
                isAmslerGridDone = isAmslerGridDone,
                isMChartDone = isMChartDone,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .padding(
                            start = 40.dp,
                            bottom = (GlobalValue.navigationBarPadding + 40).dp
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .width(44.dp),
                        painter = painterResource(id = R.drawable.icon_warning),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xff999999),
                                    fontSize = 16.sp
                                )
                            ) {
                                append(StringProvider.getString(R.string.test_list_screen_warning1))
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xffff0000),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(StringProvider.getString(R.string.test_list_screen_warning2))
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xff999999),
                                    fontSize = 16.sp
                                )
                            ) {
                                append(StringProvider.getString(R.string.test_list_screen_warning3))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Advertisement(
    idx: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffffff)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    ),
                painter = painterResource(id = when(idx % 3) {
                    0 -> R.drawable.ad_1
                    1 -> R.drawable.ad_2
                    else -> R.drawable.ad_3
                }),
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )
        }
    }
}

@Composable
fun SurveyRecommendationDialog(
    onDismissRequest: () -> Unit,
    toTestScreen: (TestType) -> Unit,
    toIntroScreen: () -> Unit,
    selectedTest: TestType
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        Column(
            modifier = Modifier
                .width(800.dp)
                .height(1000.dp)
                .background(
                    color = Color(0xffffffff)
                )
        ) {
            Text(
                modifier = Modifier
                    .padding(20.dp),
                text = buildAnnotatedString {
                    append("해당 검사를 이미 완료했습니다.\n아직 문진을 하지 않으셨다면 정확한 검사를 위해 아래의 ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xff1d71e1),
                        )
                    ) {
                        append("\'문진하러 가기\'")
                    }
                    append("를 선택해주세요\n\n검사를 다시 진행하고 싶으시면 아래의 ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xff1d71e1),
                        )
                    ) {
                        append("\'검사 다시하기\'")
                    }
                    append("를 선택해주세요")
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = Color(0xff999999)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0x00000000),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    onDismissRequest()
                                    toIntroScreen()
                                }
                                .padding(top = 4.dp),
                            text = "문진하러 가기",
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(
                            modifier = Modifier
                                .width(20.dp)
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 20.dp, end = 20.dp, bottom = 20.dp)
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = Color(0xff999999)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0x00000000),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    onDismissRequest()
                                    toTestScreen(selectedTest)
                                }
                                .padding(top = 4.dp),
                            text = "검사 다시하기",
                            textAlign = TextAlign.Center,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = Color(0xff999999)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = Color(0x00000000),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onDismissRequest()
                            }
                            .padding(top = 4.dp),
                        text = "취소",
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}