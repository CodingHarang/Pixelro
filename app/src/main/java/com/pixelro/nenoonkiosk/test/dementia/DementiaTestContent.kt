package com.pixelro.nenoonkiosk.test.dementia

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionViewModel
import com.pixelro.nenoonkiosk.facedetection.CoveredEyeCheckingContent
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionWithPreview
import com.pixelro.nenoonkiosk.facedetection.MeasuringDistanceContent
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridTestResult
import com.pixelro.nenoonkiosk.test.macular.amslergrid.AmslerGridViewModel
import com.pixelro.nenoonkiosk.test.macular.amslergrid.MacularDisorderType
import kotlin.math.tan

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
    val scrollState = rememberScrollState()
    LaunchedEffect(true) {
        dementiaViewModel.init()
    }
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
            //1번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question1),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[0]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                0,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[0]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[0]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                0,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[0]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //2번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question2),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[1]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                1,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[1]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[1]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                1,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[1]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //3번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question3),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[2]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                2,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[2]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[2]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                2,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[2]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //4번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question4),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[3]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                3,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[3]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[3]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                3,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[3]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //5번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question5),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[4]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                4,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[4]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[4]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                4,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[4]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //6번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question6),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[5]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                5,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[5]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[5]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                5,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[5]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //7번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question7),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[6]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                6,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[6]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[6]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                6,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[6]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //8번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question8),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[7]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                7,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[7]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[7]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                7,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[7]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //9번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question9),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[8]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                8,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[8]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[8]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                8,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[8]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //10번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question10),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[9]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                9,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[9]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[9]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                9,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[9]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //11번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question11),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[10]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                10,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[10]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[10]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                10,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[10]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //12번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question12),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[11]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                11,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[11]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[11]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                11,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[11]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //13번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question13),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[12]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                12,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[12]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[12]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                12,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[12]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            //14번 질문
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp),
                text = StringProvider.getString(R.string.dementia_survey_question14),
                color = Color(0xffffffff),
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium
            )
            Row() {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(50)
                        )
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[13]) {
                                    DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                13,
                                DementiaViewModel.DementiaAnswer.Yes
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.yes),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[13]) {
                            DementiaViewModel.DementiaAnswer.Yes -> Color(0xff1d71e1)
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
                        .width(350.dp)
                        .height(60.dp)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (dementiaViewModel.dementiaScores.collectAsState().value[13]) {
                                    DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                                    else -> Color(0xffc3c3c3)
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable {
                            dementiaViewModel.updateDementiaScore(
                                13,
                                DementiaViewModel.DementiaAnswer.No
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.no),
                        fontSize = 28.sp,
                        color = when (dementiaViewModel.dementiaScores.collectAsState().value[13]) {
                            DementiaViewModel.DementiaAnswer.No -> Color(0xff1d71e1)
                            else -> Color(0xffc3c3c3)
                        }
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(
                        color = Color(0x00000000)
                    )
            )

            //작성완료 BOX
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
                            if (!dementiaViewModel.checkDementiaIsDone()) return@clickable
                            toResultScreen(dementiaViewModel.getDementiaData())
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = StringProvider.getString(R.string.submit),
                        fontSize = 40.sp,
                        color = Color(0xffffffff),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}