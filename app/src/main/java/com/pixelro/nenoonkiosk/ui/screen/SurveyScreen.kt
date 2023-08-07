package com.pixelro.nenoonkiosk.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.survey.datatype.SurveyAge
import com.pixelro.nenoonkiosk.survey.datatype.SurveyData
import com.pixelro.nenoonkiosk.survey.datatype.SurveyDiabetes
import com.pixelro.nenoonkiosk.survey.datatype.SurveyGlass
import com.pixelro.nenoonkiosk.survey.datatype.SurveySex
import com.pixelro.nenoonkiosk.survey.datatype.SurveySurgery
import com.pixelro.nenoonkiosk.survey.SurveyViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

@Composable
fun SurveyScreen(
    toTestListScreen: (Long) -> Unit,
    surveyViewModel: SurveyViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        surveyViewModel.initSurveyData()
    }
    val coroutineScope = rememberCoroutineScope()
    val questionType = surveyViewModel.questionType.collectAsState().value

    var isPressed by remember { mutableStateOf(false) }
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed) Color(0xFF1D71E1) else Color.White,
        animationSpec = tween(durationMillis = 500)
    )
    val textColor by animateColorAsState(
        targetValue = if (isPressed) Color.White else Color(0xFF1D71E1),
        animationSpec = tween(durationMillis = 500)
    )
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 1f, targetValue = 0f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
            },
            repeatMode = RepeatMode.Restart
        )
    )
    val size by transition.animateFloat(
        initialValue = 60f, targetValue = 100f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
            },
            repeatMode = RepeatMode.Restart
        )
    )
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
            Text(
                textAlign = TextAlign.Center,
                text = StringProvider.getString(R.string.survey_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color(0xffdddddd)
                )
        )
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0..4) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (i == questionType.ordinal) {
                        Box(
                            modifier = Modifier
                                .width(size.dp)
                                .height(size.dp)
                                .alpha(alpha)
                                .border(
                                    border = BorderStroke(width = 10.dp, color = Color(0xFF1D71E1)),
                                    shape = CircleShape
                                )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .background(
                                color = when (i <= questionType.ordinal) {
                                    true -> Color(0xFF1D71E1)
                                    false -> Color(0xFFFFFFFF)
                                },
                                shape = CircleShape
                            )
                            .border(
                                border = BorderStroke(width = 2.dp, color = Color(0xFF1D71E1)),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (questionType) {
                // 나이 질문
                SurveyViewModel.QuestionType.Age -> {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        text = "나이를 알려주세요",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            for (idx in 1..4) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .clip(
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            border = BorderStroke(
                                                width = 4.dp,
                                                color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                                    1 to SurveyAge.First,
                                                    2 to SurveyAge.Third,
                                                    3 to SurveyAge.Fifth,
                                                    4 to SurveyAge.Seventh -> Color(0xff1d71e1)

                                                    else -> Color(0xff1d71e1)
                                                }
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                                1 to SurveyAge.First,
                                                2 to SurveyAge.Third,
                                                3 to SurveyAge.Fifth,
                                                4 to SurveyAge.Seventh -> buttonColor

                                                else -> Color(0xFFFFFFFF)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            surveyViewModel.updateSurveyAge(
                                                when (idx) {
                                                    1 -> SurveyAge.First
                                                    2 -> SurveyAge.Third
                                                    3 -> SurveyAge.Fifth
                                                    else -> SurveyAge.Seventh
                                                }
                                            )
                                            surveyViewModel.updateQuestionType(SurveyViewModel.QuestionType.Sex)
                                            coroutineScope.launch {
                                                isPressed = true
                                                delay(1000)
                                                isPressed = false
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (idx) {
                                            1 -> "9세 이하"
                                            2 -> "20세~29세"
                                            3 -> "40세~49세"
                                            else -> "60세~69세"
                                        },
                                        fontSize = 60.sp,
                                        color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                            1 to SurveyAge.First,
                                            2 to SurveyAge.Third,
                                            3 to SurveyAge.Fifth,
                                            4 to SurveyAge.Seventh -> textColor
                                            else -> Color(0xFF1D71E1)
                                        },
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                                when (idx < 4) {
                                    true -> Spacer(
                                        modifier = Modifier
                                            .height(20.dp)
                                    )
                                    false -> {}
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
                        ) {
                            for (idx in 5..8) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .clip(
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            border = BorderStroke(
                                                width = 4.dp,
                                                color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                                    5 to SurveyAge.Second,
                                                    6 to SurveyAge.Fourth,
                                                    7 to SurveyAge.Sixth,
                                                    8 to SurveyAge.Eighth -> Color(0xff1d71e1)

                                                    else -> Color(0xff1d71e1)
                                                }
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                                5 to SurveyAge.Second,
                                                6 to SurveyAge.Fourth,
                                                7 to SurveyAge.Sixth,
                                                8 to SurveyAge.Eighth -> buttonColor

                                                else -> Color(0xFFFFFFFF)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            surveyViewModel.updateSurveyAge(
                                                when (idx) {
                                                    5 -> SurveyAge.Second
                                                    6 -> SurveyAge.Fourth
                                                    7 -> SurveyAge.Sixth
                                                    else -> SurveyAge.Eighth
                                                }
                                            )
                                            surveyViewModel.updateQuestionType(SurveyViewModel.QuestionType.Sex)
                                            coroutineScope.launch {
                                                isPressed = true
                                                delay(1000)
                                                isPressed = false
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (idx) {
                                            5 -> "10세~19세"
                                            6 -> "30세~39세"
                                            7 -> "50세~59세"
                                            else -> "70세 이상"
                                        },
                                        fontSize = 60.sp,
                                        color = when (idx to surveyViewModel.surveyAge.collectAsState().value) {
                                            5 to SurveyAge.Second,
                                            6 to SurveyAge.Fourth,
                                            7 to SurveyAge.Sixth,
                                            8 to SurveyAge.Eighth -> textColor
                                            else -> Color(0xFF1D71E1)
                                        },
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                                when (idx < 8) {
                                    true -> Spacer(
                                        modifier = Modifier
                                            .height(20.dp)
                                    )
                                    false -> {}
                                }
                            }
                        }
                    }
                }
                SurveyViewModel.QuestionType.Sex -> {
                    // 성별 질문
                    Text(
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        text = "성별은 무엇인가요?",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (idx in 1..2) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(0.7f)
                                    .clip(
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        border = BorderStroke(
                                            width = 4.dp,
                                            color = when (idx to surveyViewModel.surveySex.collectAsState().value) {
                                                1 to SurveySex.Man,
                                                2 to SurveySex.Woman -> Color(0xff1d71e1)

                                                else -> Color(0xff1d71e1)
                                            }
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        color = when (idx to surveyViewModel.surveySex.collectAsState().value) {
                                            1 to SurveySex.Man,
                                            2 to SurveySex.Woman -> buttonColor

                                            else -> Color(0xFFFFFFFF)
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        surveyViewModel.updateSurveySex(
                                            when (idx) {
                                                1 -> SurveySex.Man
                                                else -> SurveySex.Woman
                                            }
                                        )
                                        surveyViewModel.updateQuestionType(SurveyViewModel.QuestionType.Glass)
                                        coroutineScope.launch {
                                            isPressed = true
                                            delay(1000)
                                            isPressed = false
                                        }
                                    }
                                    .padding(
                                        start = 20.dp,
                                        top = 16.dp,
                                        end = 20.dp,
                                        bottom = 16.dp
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (idx) {
                                        1 -> "남자"
                                        else -> "여자"
                                    },
                                    fontSize = 60.sp,
                                    color = when(idx to surveyViewModel.surveySex.collectAsState().value) {
                                        1 to SurveySex.Man,
                                        2 to SurveySex.Woman -> textColor
                                        else -> Color(0xFF1D71E1)
                                    },
                                    fontWeight = FontWeight.ExtraBold
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
                SurveyViewModel.QuestionType.Glass -> {
                    // 안경 질문
                    Text(
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        text = "안경을 착용하시나요?",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (idx in 1..2) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(0.7f)
                                    .clip(
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        border = BorderStroke(
                                            width = 4.dp,
                                            color = when (idx to surveyViewModel.surveyGlass.collectAsState().value) {
                                                1 to SurveyGlass.Yes,
                                                2 to SurveyGlass.No -> Color(0xFF1D71E1)

                                                else -> Color(0xFF1D71E1)
                                            }
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        color = when (idx to surveyViewModel.surveyGlass.collectAsState().value) {
                                            1 to SurveyGlass.Yes,
                                            2 to SurveyGlass.No -> buttonColor

                                            else -> Color(0xFFFFFFFF)
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        surveyViewModel.updateSurveyGlass(
                                            when (idx) {
                                                1 -> SurveyGlass.Yes
                                                else -> SurveyGlass.No
                                            }
                                        )
                                        surveyViewModel.updateQuestionType(SurveyViewModel.QuestionType.Surgery)
                                        coroutineScope.launch {
                                            isPressed = true
                                            delay(1000)
                                            isPressed = false
                                        }
                                    }
                                    .padding(
                                        start = 20.dp,
                                        top = 16.dp,
                                        end = 20.dp,
                                        bottom = 16.dp
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (idx) {
                                        1 -> "네"
                                        else -> "아니요"
                                    },
                                    fontSize = 60.sp,
                                    color = when(idx to surveyViewModel.surveyGlass.collectAsState().value) {
                                        1 to SurveyGlass.Yes,
                                        2 to SurveyGlass.No-> textColor
                                        else -> Color(0xFF1D71E1)
                                    },
                                    fontWeight = FontWeight.ExtraBold
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
                SurveyViewModel.QuestionType.Surgery -> {
                    // 수술 질문
                    Text(
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        text = "눈을 수술하신 경험이 있나요?",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Column() {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            for (idx in 1..2) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            border = BorderStroke(
                                                width = 4.dp,
                                                color = when (idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                                    1 to SurveySurgery.Normal,
                                                    2 to SurveySurgery.LASIK,
                                                    -> Color(0xFF1D71E1)

                                                    else -> Color(0xFF1D71E1)
                                                }
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            color = when (idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                                1 to SurveySurgery.Normal,
                                                2 to SurveySurgery.LASIK,
                                                -> buttonColor

                                                else -> Color(0xFFFFFFFF)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            surveyViewModel.updateSurveySurgery(
                                                when (idx) {
                                                    1 -> SurveySurgery.Normal
                                                    2 -> SurveySurgery.LASIK
                                                    else -> SurveySurgery.None
                                                }
                                            )
                                            surveyViewModel.updateQuestionType(SurveyViewModel.QuestionType.Diabetes)
                                            coroutineScope.launch {
                                                isPressed = true
                                                delay(1000)
                                                isPressed = false
                                            }
                                        }
                                        .padding(
                                            start = 20.dp,
                                            top = 16.dp,
                                            end = 20.dp,
                                            bottom = 16.dp
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (idx) {
                                            1 -> "없음"
                                            2 -> "라식/라섹"
                                            else -> "기타"
                                        },
                                        fontSize = 60.sp,
                                        color = when(idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                            1 to SurveySurgery.Normal,
                                            2 to SurveySurgery.LASIK,
                                            3 to SurveySurgery.Cataract,
                                            4 to SurveySurgery.Etc -> textColor
                                            else -> Color(0xFF1D71E1)
                                        },
                                        fontWeight = FontWeight.ExtraBold
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
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                        Row(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            for (idx in 1..2) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            border = BorderStroke(
                                                width = 4.dp,
                                                color = when (idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                                    1 to SurveySurgery.Cataract,
                                                    2 to SurveySurgery.Etc -> Color(0xff1d71e1)

                                                    else -> Color(0xFF1D71E1)
                                                }
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            color = when (idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                                1 to SurveySurgery.Cataract,
                                                2 to SurveySurgery.Etc -> buttonColor

                                                else -> Color(0xFFFFFFFF)
                                            }
                                        )
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            surveyViewModel.updateSurveySurgery(
                                                when (idx) {
                                                    1 -> SurveySurgery.Cataract
                                                    2 -> SurveySurgery.Etc
                                                    else -> SurveySurgery.None
                                                }
                                            )
                                            surveyViewModel.updateQuestionType(SurveyViewModel.QuestionType.Diabetes)
                                            coroutineScope.launch {
                                                isPressed = true
                                                delay(1000)
                                                isPressed = false
                                            }
                                        }
                                        .padding(
                                            start = 20.dp,
                                            top = 16.dp,
                                            end = 20.dp,
                                            bottom = 16.dp
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (idx) {
                                            1 -> "백내장"
                                            else -> "기타"
                                        },
                                        fontSize = 60.sp,
                                        color = when(idx to surveyViewModel.surveySurgery.collectAsState().value) {
                                            1 to SurveySurgery.Cataract,
                                            2 to SurveySurgery.Etc -> textColor
                                            else -> Color(0xFF1D71E1)
                                        },
                                        fontWeight = FontWeight.ExtraBold
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
                }
                SurveyViewModel.QuestionType.Diabetes -> {
                    // 당뇨 질문
                    Text(
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        text = "당뇨가 있으신가요?",
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (idx in 1..2) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(0.7f)
                                    .clip(
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        border = BorderStroke(
                                            width = 4.dp,
                                            color = when (idx to surveyViewModel.surveyDiabetes.collectAsState().value) {
                                                1 to SurveyDiabetes.Yes,
                                                2 to SurveyDiabetes.No -> Color(0xFF1D71E1)

                                                else -> Color(0xFF1D71E1)
                                            }
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(
                                        color = when (idx to surveyViewModel.surveyDiabetes.collectAsState().value) {
                                            1 to SurveyDiabetes.Yes,
                                            2 to SurveyDiabetes.No -> buttonColor

                                            else -> Color(0xFFFFFFFF)
                                        }
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        surveyViewModel.updateSurveyDiabetes(
                                            when (idx) {
                                                1 -> SurveyDiabetes.Yes
                                                else -> SurveyDiabetes.No
                                            }
                                        )
                                        coroutineScope.launch {
                                            isPressed = true
                                            delay(1000)
                                            isPressed = false
                                            surveyViewModel.getSurveyId(toTestListScreen)
                                        }
                                    }
                                    .padding(
                                        start = 20.dp,
                                        top = 16.dp,
                                        end = 20.dp,
                                        bottom = 16.dp
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when(idx) {
                                        1 -> "네"
                                        else -> "아니오"
                                    },
                                    fontSize = 60.sp,
                                    color = when(idx to surveyViewModel.surveyDiabetes.collectAsState().value) {
                                        1 to SurveyDiabetes.Yes,
                                        2 to SurveyDiabetes.No -> textColor
                                        else -> Color(0xFF1D71E1)
                                    },
                                    fontWeight = FontWeight.ExtraBold
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
            }
        }

//        // 작성 완료 버튼
//        Box(
//            modifier = Modifier
//                .fillMaxSize(),
//            contentAlignment = Alignment.BottomCenter
//        ) {
//            Box(
//                modifier = Modifier
//                    .padding(
//                        start = 40.dp,
//                        end = 40.dp,
//                        bottom = 40.dp
//                    )
//                    .fillMaxWidth()
//                    .height(80.dp)
//                    .clip(
//                        shape = RoundedCornerShape(8.dp)
//                    )
//                    .background(
//                        color = Color(0xff1d71e1),
//                        shape = RoundedCornerShape(8.dp),
//                    )
//                    .clickable {
//                        if (!surveyViewModel.checkSurveyIsDone()) return@clickable
//                        toTestListScreen(surveyViewModel.getSurveyData())
//                    },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "작성 완료",
//                    fontSize = 40.sp,
//                    color = Color(0xffffffff),
//                    fontWeight = FontWeight.Medium,
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
    }
}

object MyIndication: Indication {

    val animateWidth = Animatable(
        initialValue = 100f,
)
    private class DefaultDebugIndicationInstance(
        private val isPressed: State<Boolean>,
        private val isHovered: State<Boolean>,
        private val isFocused: State<Boolean>,
    ) : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
            if (isPressed.value) {
                drawRect(
                    color = Color.Red.copy(alpha = 1.0f),
                    size = size,
                )
            } else if (isHovered.value || isFocused.value) {
                drawRect(
                    color = Color.Red.copy(alpha = 0.1f),
                    size = size
                )
            }
        }
    }
    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val isPressed = interactionSource.collectIsPressedAsState()
        val isHovered = interactionSource.collectIsHoveredAsState()
        val isFocused = interactionSource.collectIsFocusedAsState()
        return remember(interactionSource) {
            DefaultDebugIndicationInstance(isPressed, isHovered, isFocused)
        }
    }

}