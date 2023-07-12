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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
                Row() {
                    Box(
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.First -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.First)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "9세 이하",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.First -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.Second -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.Second)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "10 ~ 14세",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.Second -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.Third -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.Third)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "15 ~ 19세",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.Third -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.Fourth -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.Fourth)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "20 ~ 29세",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.Fourth -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.Fifth -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.Fifth)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "30 ~ 39세",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.Fifth -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.Sixth -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.Sixth)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "40 ~ 49세",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.Sixth -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.Seventh -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.Seventh)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "50 ~ 59세",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.Seventh -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clip(
                                shape = RoundedCornerShape(50)
                            )
                            .width(160.dp)
                            .height(60.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = when (surveyViewModel.surveyAge.collectAsState().value) {
                                        SurveyAge.Eighth -> Color(0xff1d71e1)
                                        else -> Color(0xffc3c3c3)
                                    }
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable {
                                surveyViewModel.updateSurveyAge(SurveyAge.Eighth)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "60세 이상",
                            fontSize = 28.sp,
                            color = when(surveyViewModel.surveyAge.collectAsState().value) {
                                SurveyAge.Eighth -> Color(0xff1d71e1)
                                else -> Color(0xffc3c3c3)
                            }
                        )
                    }
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp),
                text = "성별은 무엇인가요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveySex.collectAsState().value) {
                                    SurveySex.Man -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveySex(SurveySex.Man)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "남성",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveySex.collectAsState().value) {
                            SurveySex.Man -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveySex.collectAsState().value) {
                                    SurveySex.Woman -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveySex(SurveySex.Woman)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "여성",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveySex.collectAsState().value) {
                            SurveySex.Woman -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp),
                text = "안경을 착용하시나요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveyGlass.collectAsState().value) {
                                    SurveyGlass.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveyGlass(SurveyGlass.No)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "미착용",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveyGlass.collectAsState().value) {
                            SurveyGlass.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveyGlass.collectAsState().value) {
                                    SurveyGlass.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveyGlass(SurveyGlass.Yes)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "안경 또는 렌즈 착용",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveyGlass.collectAsState().value) {
                            SurveyGlass.Yes -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp),
                text = "눈을 수술하신 경험이 있나요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveySurgery.collectAsState().value) {
                                    SurveySurgery.Normal -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveySurgery(SurveySurgery.Normal)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "없음",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveySurgery.collectAsState().value) {
                            SurveySurgery.Normal -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveySurgery.collectAsState().value) {
                                    SurveySurgery.LASIK -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveySurgery(SurveySurgery.LASIK)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "라식/라섹",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveySurgery.collectAsState().value) {
                            SurveySurgery.LASIK -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveySurgery.collectAsState().value) {
                                    SurveySurgery.Cataract -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveySurgery(SurveySurgery.Cataract)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "백내장",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveySurgery.collectAsState().value) {
                            SurveySurgery.Cataract -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
                Spacer(
                        modifier = Modifier
                            .width(20.dp)
                        )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveySurgery.collectAsState().value) {
                                    SurveySurgery.Etc -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveySurgery(SurveySurgery.Etc)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "기타",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveySurgery.collectAsState().value) {
                            SurveySurgery.Etc -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp),
                text = "당뇨가 있으신가요?",
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveyDiabetes.collectAsState().value) {
                                    SurveyDiabetes.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveyDiabetes(SurveyDiabetes.Yes)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "네",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveyDiabetes.collectAsState().value) {
                            SurveyDiabetes.Yes -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (surveyViewModel.surveyDiabetes.collectAsState().value) {
                                    SurveyDiabetes.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            surveyViewModel.updateSurveyDiabetes(SurveyDiabetes.No)
                        }
                        .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "아니오",
                        fontSize = 28.sp,
                        color = when(surveyViewModel.surveyDiabetes.collectAsState().value) {
                            SurveyDiabetes.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
        }
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