package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.survey.SurveyAge
import com.pixelro.nenoonkiosk.survey.SurveyData
import com.pixelro.nenoonkiosk.survey.SurveyDiabetes
import com.pixelro.nenoonkiosk.survey.SurveyGlass
import com.pixelro.nenoonkiosk.survey.SurveySex
import com.pixelro.nenoonkiosk.survey.SurveySurgery
import com.pixelro.nenoonkiosk.survey.SurveyViewModel

@Composable
fun SurveyScreen(
    toTestListScreen: (SurveyData) -> Unit,
    toPrimaryScreen: () -> Unit,
    surveyViewModel: SurveyViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        surveyViewModel.initSurvey()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = (GlobalValue.statusBarPadding + 20).dp,
                    bottom = 20.dp
                )
                .fillMaxWidth()
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            //Add home button
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .clickable {
                            toPrimaryScreen()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .padding(top = 4.dp, start = 20.dp, end = 4.dp)
                            .width(28.dp),
                        painter = painterResource(id = R.drawable.home_button),
                        contentDescription = ""
                    )
                    Text(
                        text = StringProvider.getString(R.string.home),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

            Text(
                textAlign = TextAlign.Center,
                text = StringProvider.getString(R.string.survey_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffdddddd)
                )
        )

        // 나이 질문
        Column(
            modifier = Modifier
                .padding(start = 40.dp, top = 20.dp, end = 40.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = "나이를 알려주세요",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Column() {
                Row {
                    for (idx in 1..4) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .weight(1f)
                                .height(60.dp)
                                .border(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                            1 to SurveyAge.First,
                                            2 to SurveyAge.Second,
                                            3 to SurveyAge.Third,
                                            4 to SurveyAge.Fourth -> Color(0xff1d71e1)

                                            else -> Color(0xffc3c3c3)
                                        }
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    surveyViewModel.updateSurveyAge(
                                        when (idx) {
                                            1 -> SurveyAge.First
                                            2 -> SurveyAge.Second
                                            3 -> SurveyAge.Third
                                            else -> SurveyAge.Fourth
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (idx) {
                                    1 -> "9세 이하"
                                    2 -> "10세~19세"
                                    3 -> "20세~29세"
                                    else -> "30세~39세"
                                },
                                fontSize = 28.sp,
                                color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                    1 to SurveyAge.First,
                                    2 to SurveyAge.Second,
                                    3 to SurveyAge.Third,
                                    4 to SurveyAge.Fourth -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            )
                        }
                        when (idx < 4) {
                            true -> Spacer(
                                modifier = Modifier
                                    .width(20.dp)
                            )
                            false -> {}
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) {
                    for (idx in 5..8) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .weight(1f)
                                .height(60.dp)
                                .border(
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                            5 to SurveyAge.Fifth,
                                            6 to SurveyAge.Sixth,
                                            7 to SurveyAge.Seventh,
                                            8 to SurveyAge.Eighth -> Color(0xff1d71e1)

                                            else -> Color(0xffc3c3c3)
                                        }
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    surveyViewModel.updateSurveyAge(
                                        when (idx) {
                                            5 -> SurveyAge.Fifth
                                            6 -> SurveyAge.Sixth
                                            7 -> SurveyAge.Seventh
                                            else -> SurveyAge.Eighth
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (idx) {
                                    5 -> "40세~49세"
                                    6 -> "50세~59세"
                                    7 -> "60세~69세"
                                    else -> "70세 이상"
                                },
                                fontSize = 28.sp,
                                color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                    5 to SurveyAge.Fifth,
                                    6 to SurveyAge.Sixth,
                                    7 to SurveyAge.Seventh,
                                    8 to SurveyAge.Eighth -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            )
                        }
                        when (idx < 8) {
                            true -> Spacer(
                                modifier = Modifier
                                    .width(20.dp)
                            )
                            false -> {}
                        }
                    }
                }
            }

            // 성별 질문
            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                text = "성별은 무엇인가요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                for (idx in 1..2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (idx to surveyViewModel.surveySex.collectAsState().value) {
                                        1 to SurveySex.Man,
                                        2 to SurveySex.Woman -> Color(0xff1d71e1)

                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                surveyViewModel.updateSurveySex(
                                    when (idx) {
                                        1 -> SurveySex.Man
                                        else -> SurveySex.Woman
                                    }
                                )
                            }
                            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = when (idx) {
                                1 -> "남자"
                                else -> "여자"
                            },
                            fontSize = 28.sp,
                            color = when(idx to surveyViewModel.surveySex.collectAsState().value) {
                                1 to SurveySex.Man,
                                2 to SurveySex.Woman -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    when (idx < 2) {
                        true -> Spacer(
                            modifier = Modifier
                                .width(20.dp)
                        )
                        false -> {}
                    }
                }
            }

            // 안경 질문
            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                text = "안경을 착용하시나요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                for (idx in 1..2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (idx to surveyViewModel.surveyGlass.collectAsState().value) {
                                        1 to SurveyGlass.Yes,
                                        2 to SurveyGlass.No -> Color(0xff1d71e1)

                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyGlass(
                                    when (idx) {
                                        1 -> SurveyGlass.Yes
                                        else -> SurveyGlass.No
                                    }
                                )
                            }
                            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = when (idx) {
                                1 -> "네"
                                else -> "아니요"
                            },
                            fontSize = 28.sp,
                            color = when(idx to surveyViewModel.surveyGlass.collectAsState().value) {
                                1 to SurveyGlass.Yes,
                                2 to SurveyGlass.No-> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    when (idx < 2) {
                        true -> Spacer(
                            modifier = Modifier
                                .width(20.dp)
                        )
                        false -> {}
                    }
                }
            }

            // 수술 질문
            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                text = "눈을 수술하신 경험이 있나요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                for (idx in 1..4) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                        1 to SurveySurgery.Normal,
                                        2 to SurveySurgery.LASIK,
                                        3 to SurveySurgery.Cataract,
                                        4 to SurveySurgery.Etc -> Color(0xff1d71e1)

                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                surveyViewModel.updateSurveySurgery(
                                    when (idx) {
                                        1 -> SurveySurgery.Normal
                                        2 -> SurveySurgery.LASIK
                                        3 -> SurveySurgery.Cataract
                                        4 -> SurveySurgery.Etc
                                        else -> SurveySurgery.None
                                    }
                                )
                            }
                            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = when (idx) {
                                1 -> "없음"
                                2 -> "라식/라섹"
                                3 -> "백내장"
                                else -> "기타"
                            },
                            fontSize = 28.sp,
                            color = when(idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                1 to SurveySurgery.Normal,
                                2 to SurveySurgery.LASIK,
                                3 to SurveySurgery.Cataract,
                                4 to SurveySurgery.Etc -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    when (idx < 4) {
                        true -> Spacer(
                            modifier = Modifier
                                .width(20.dp)
                        )
                        false -> {}
                    }
                }
            }

            // 당뇨 질문
            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                text = "당뇨가 있으신가요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                for (idx in 1..2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (idx to surveyViewModel.surveyDiabetes.collectAsState().value) {
                                        1 to SurveyDiabetes.Yes,
                                        2 to SurveyDiabetes.No -> Color(0xff1d71e1)

                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyDiabetes(
                                    when (idx) {
                                        1 -> SurveyDiabetes.Yes
                                        else -> SurveyDiabetes.No
                                    }
                                )
                            }
                            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = when(idx) {
                                1 -> "네"
                                else -> "아니오"
                            },
                            fontSize = 28.sp,
                            color = when(idx to surveyViewModel.surveyDiabetes.collectAsState().value) {
                                1 to SurveyDiabetes.Yes,
                                2 to SurveyDiabetes.No -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    when (idx < 2) {
                        true -> Spacer(
                            modifier = Modifier
                                .width(20.dp)
                        )
                        false -> {}
                    }
                }
            }
        }

        // 작성 완료 버튼
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
                        bottom = 40.dp
                    )
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color(0xff1d71e1),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clickable {
                        if (!surveyViewModel.checkSurveyIsDone()) return@clickable
                        toTestListScreen(surveyViewModel.getSurveyData())
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "작성 완료",
                    fontSize = 40.sp,
                    color = Color(0xffffffff),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}