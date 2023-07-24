package com.pixelro.nenoonkiosk.test.dementia

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider

@Composable
fun DementiaTestContent(
    toBackScreen: () -> Unit,
    toResultScreen: (DementiaTestResult) -> Unit,
    dementiaViewModel: DementiaViewModel = hiltViewModel()
) {
    BackHandler(enabled = true) {
        Log.e("backhandler", "backhandler")
        toBackScreen()
    }
    val dementiapage1 = remember { mutableStateOf(true) }
    val dementiapage2 = remember { mutableStateOf(false) }
    val dementiapage3 = remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    LaunchedEffect(true) {
        dementiaViewModel.init()
    }
    when {
        dementiapage1.value -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xff000000)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 40.dp, top = 20.dp, end = 40.dp)
                        .fillMaxWidth()
                        .weight(10f)
                        .verticalScroll(scrollState)
                ) {
                    for (i in 0..4) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .height(100.dp),
                            text = when (i) {
                                0 -> StringProvider.getString(R.string.dementia_survey_question0)
                                1 -> StringProvider.getString(R.string.dementia_survey_question0)
                                2 -> StringProvider.getString(R.string.dementia_survey_question1)
                                3 -> StringProvider.getString(R.string.dementia_survey_question2)
                                4 -> StringProvider.getString(R.string.dementia_survey_question3)
                                else -> ""
                            },
                            color = Color(0xffffffff),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Spacer(Modifier.weight(1f))
                            questions(
                                questionIndex = i,
                                answer = DementiaViewModel.DementiaAnswer.Yes
                            )
                            questions(
                                questionIndex = i,
                                answer = DementiaViewModel.DementiaAnswer.No
                            )
                        }
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 40.dp,
                                    bottom = 40.dp
                                )
                                .width(160.dp)
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0xff1d71e1),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {
                                    dementiapage1.value = false
                                    dementiapage2.value = true
                                }
                                .align(alignment = Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "다음",
                                modifier = Modifier
                                    .padding(5.dp),
                                fontSize = 40.sp,
                                color = Color(0xffffffff),
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }

        dementiapage2.value -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xff000000)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 40.dp, top = 20.dp, end = 40.dp)
                        .fillMaxWidth()
                        .weight(10f)
                        .verticalScroll(scrollState)
                ) {
                    for (i in 5..9) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .height(100.dp),
                            text = when (i) {
                                5 -> StringProvider.getString(R.string.dementia_survey_question4)
                                6 -> StringProvider.getString(R.string.dementia_survey_question5)
                                7 -> StringProvider.getString(R.string.dementia_survey_question6)
                                8 -> StringProvider.getString(R.string.dementia_survey_question7)
                                9 -> StringProvider.getString(R.string.dementia_survey_question8)
                                else -> ""
                            },
                            color = Color(0xffffffff),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Spacer(Modifier.weight(1f))
                            questions(
                                questionIndex = i,
                                answer = DementiaViewModel.DementiaAnswer.Yes
                            )
                            questions(
                                questionIndex = i,
                                answer = DementiaViewModel.DementiaAnswer.No
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 40.dp,
                                    bottom = 40.dp
                                )
                                .width(160.dp)
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0xff1d71e1),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {
                                    dementiapage2.value = false
                                    dementiapage1.value = true
                                }
                                .align(alignment = Alignment.BottomStart),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "이전",
                                modifier = Modifier
                                    .padding(5.dp),
                                fontSize = 40.sp,
                                color = Color(0xffffffff),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 40.dp,
                                    bottom = 40.dp
                                )
                                .width(160.dp)
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0xff1d71e1),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {
                                    dementiapage2.value = false
                                    dementiapage3.value = true
                                }
                                .align(alignment = Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "다음",
                                modifier = Modifier
                                    .padding(5.dp),
                                fontSize = 40.sp,
                                color = Color(0xffffffff),
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }

        dementiapage3.value -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xff000000)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 40.dp, top = 20.dp, end = 40.dp)
                        .fillMaxWidth()
                        .weight(10f)
                        .verticalScroll(scrollState)
                ) {
                    for (i in 10..13) {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .height(100.dp),
                            text = when (i) {
                                10 -> StringProvider.getString(R.string.dementia_survey_question9)
                                11 -> StringProvider.getString(R.string.dementia_survey_question10)
                                12 -> StringProvider.getString(R.string.dementia_survey_question11)
                                13 -> StringProvider.getString(R.string.dementia_survey_question12)
                                else -> ""
                            },
                            color = Color(0xffffffff),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Spacer(Modifier.weight(1f))
                            questions(
                                questionIndex = i,
                                answer = DementiaViewModel.DementiaAnswer.Yes
                            )
                            questions(
                                questionIndex = i,
                                answer = DementiaViewModel.DementiaAnswer.No
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 40.dp,
                                    bottom = 40.dp
                                )
                                .width(160.dp)
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0xff1d71e1),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {
                                    dementiapage3.value = false
                                    dementiapage2.value = true
                                }
                                .align(alignment = Alignment.BottomStart),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "이전",
                                modifier = Modifier
                                    .padding(5.dp),
                                fontSize = 40.sp,
                                color = Color(0xffffffff),
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 40.dp,
                                    end = 20.dp,
                                    bottom = 40.dp
                                )
                                .width(260.dp)
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(
                                    color = Color(0xff1d71e1),
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .clickable {

                                    if (!dementiaViewModel.checkDementiaIsDone()) return@clickable
                                    toResultScreen(dementiaViewModel.getDementiaData())
                                }
                                .align(alignment = Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "작성완료",
                                modifier = Modifier
                                    .padding(5.dp),
                                fontSize = 40.sp,
                                color = Color(0xffffffff),
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }
        else -> ""
    }
}


@Composable
fun questions(
    dementiaViewModel: DementiaViewModel = hiltViewModel(),
    questionIndex: Int,
    answer: DementiaViewModel.DementiaAnswer
) {

    Box(
        modifier = Modifier
            .padding(start = if (answer == DementiaViewModel.DementiaAnswer.Yes) 0.dp else 20.dp)
            .clip(
                shape = RoundedCornerShape(50)
            )
            .width(100.dp)
            .height(60.dp)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = when (dementiaViewModel.dementiaScores.collectAsState().value[questionIndex]) {
                        answer -> Color(0xff1d71e1)
                        else -> Color(0xffc3c3c3)
                    }
                ),
                shape = RoundedCornerShape(50)
            )
            .clickable {
                dementiaViewModel.updateDementiaScore(
                    questionIndex,
                    answer
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = StringProvider.getString(if (answer == DementiaViewModel.DementiaAnswer.Yes) R.string.yes else R.string.no),
            fontSize = 28.sp,
            color = when (dementiaViewModel.dementiaScores.collectAsState().value[questionIndex]) {
                answer -> Color(0xff1d71e1)
                else -> Color(0xffc3c3c3)
            }
        )
    }
}