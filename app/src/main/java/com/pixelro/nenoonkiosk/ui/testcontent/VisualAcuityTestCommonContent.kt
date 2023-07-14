package com.pixelro.nenoonkiosk.ui.testcontent

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.TTS
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.VisionDisorderType
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.test.visualacuity.VisualAcuityTestResult
import com.pixelro.nenoonkiosk.test.visualacuity.VisualAcuityViewModel
import java.util.*

@Composable
fun VisualAcuityTestCommonContent(
    visualAcuityTestCommonContentVisibleState: MutableTransitionState<Boolean>,
    toResultScreen: (VisualAcuityTestResult) -> Unit
) {
    AnimatedVisibility(
        visibleState = visualAcuityTestCommonContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        FaceDetection()
        VisualAcuityTestContent(
            toResultScreen = toResultScreen
        )
    }
}

@Composable
fun VisualAcuityTestContent(
    toResultScreen: (VisualAcuityTestResult) -> Unit,
    visualAcuityViewModel: VisualAcuityViewModel = hiltViewModel(),
) {
    val randomList = visualAcuityViewModel.randomList.collectAsState().value
    val ansNum = visualAcuityViewModel.ansNum.collectAsState().value
    val sightLevel = visualAcuityViewModel.sightLevel.collectAsState().value
    var progress by remember { mutableStateOf(0.1f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    LaunchedEffect(true) {
        TTS.speechTTS("검사를 시작하겠습니다. 아래의 세 시표 중, 위 시표와 동일하게 보이는 것을 선택해주세요. 시표가 잘 보이지 않는다면 아래의 안보임 버튼을 눌러주세요.", TextToSpeech.QUEUE_ADD)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 40.dp)
                .height(430.dp)
                .width(430.dp)
                .background(
                    color = Color(0xffffffff),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id =
                    when(ansNum) {
                        2 -> when(sightLevel) {
                            1 -> R.drawable._50cm_2_1
                            2 -> R.drawable._50cm_2_2
                            3 -> R.drawable._50cm_2_3
                            4 -> R.drawable._50cm_2_4
                            5 -> R.drawable._50cm_2_5
                            6 -> R.drawable._50cm_2_6
                            7 -> R.drawable._50cm_2_7
                            8 -> R.drawable._50cm_2_8
                            9 -> R.drawable._50cm_2_9
                            else -> R.drawable._50cm_2_10
                        }
                        3 -> when(sightLevel) {
                            1 -> R.drawable._50cm_3_1
                            2 -> R.drawable._50cm_3_2
                            3 -> R.drawable._50cm_3_3
                            4 -> R.drawable._50cm_3_4
                            5 -> R.drawable._50cm_3_5
                            6 -> R.drawable._50cm_3_6
                            7 -> R.drawable._50cm_3_7
                            8 -> R.drawable._50cm_3_8
                            9 -> R.drawable._50cm_3_9
                            else -> R.drawable._50cm_3_10
                        }
                        4 -> when(sightLevel) {
                            1 -> R.drawable._50cm_4_1
                            2 -> R.drawable._50cm_4_2
                            3 -> R.drawable._50cm_4_3
                            4 -> R.drawable._50cm_4_4
                            5 -> R.drawable._50cm_4_5
                            6 -> R.drawable._50cm_4_6
                            7 -> R.drawable._50cm_4_7
                            8 -> R.drawable._50cm_4_8
                            9 -> R.drawable._50cm_4_9
                            else -> R.drawable._50cm_4_10
                        }
                        5 -> when(sightLevel) {
                            1 -> R.drawable._50cm_5_1
                            2 -> R.drawable._50cm_5_2
                            3 -> R.drawable._50cm_5_3
                            4 -> R.drawable._50cm_5_4
                            5 -> R.drawable._50cm_5_5
                            6 -> R.drawable._50cm_5_6
                            7 -> R.drawable._50cm_5_7
                            8 -> R.drawable._50cm_5_8
                            9 -> R.drawable._50cm_5_9
                            else -> R.drawable._50cm_5_10
                        }
                        6 -> when(sightLevel) {
                            1 -> R.drawable._50cm_6_1
                            2 -> R.drawable._50cm_6_2
                            3 -> R.drawable._50cm_6_3
                            4 -> R.drawable._50cm_6_4
                            5 -> R.drawable._50cm_6_5
                            6 -> R.drawable._50cm_6_6
                            7 -> R.drawable._50cm_6_7
                            8 -> R.drawable._50cm_6_8
                            9 -> R.drawable._50cm_6_9
                            else -> R.drawable._50cm_6_10
                        }
                        else -> when(sightLevel) {
                            1 -> R.drawable._50cm_7_1
                            2 -> R.drawable._50cm_7_2
                            3 -> R.drawable._50cm_7_3
                            4 -> R.drawable._50cm_7_4
                            5 -> R.drawable._50cm_7_5
                            6 -> R.drawable._50cm_7_6
                            7 -> R.drawable._50cm_7_7
                            8 -> R.drawable._50cm_7_8
                            9 -> R.drawable._50cm_7_9
                            else -> R.drawable._50cm_7_10
                        }
                    }
                ),
                contentDescription = ""
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 40.dp),
            text = StringProvider.getString(R.string.visual_acuity_test_common_content_description),
            fontSize = 40.sp,
            color = Color(0xffffffff),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        LinearProgressIndicator(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .width(430.dp)
                .height(20.dp),
            progress = animatedProgress,
            color = Color(0xff1d71e1),
        )
        Row() {
            // 1
            Box(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(130.dp)
                        .width(130.dp)
                        .background(
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            visualAcuityViewModel.processAnswerSelected(
                                idx = 0,
                                handleWrong = {
                                    progress = it
                                }
                            ) {
                                toResultScreen(
                                    visualAcuityViewModel.getVisualAcuityTestResult()
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(100.dp),
                        imageVector = ImageVector.vectorResource(id =
                        when(randomList[0]) {
                            2 -> R.drawable.two
                            3 -> R.drawable.three
                            4 -> R.drawable.four
                            5 -> R.drawable.five
                            6 -> R.drawable.six
                            else -> R.drawable.seven
                        }
                        ),
                        contentDescription = ""
                    )
                }
            }
            // 2
            Box(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(130.dp)
                        .width(130.dp)
                        .background(
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            visualAcuityViewModel.processAnswerSelected(
                                1,
                                handleWrong = {
                                    progress = it
                                }
                            ) {
                                toResultScreen(
                                    visualAcuityViewModel.getVisualAcuityTestResult()
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(100.dp),
                        imageVector = ImageVector.vectorResource(id =
                        when(randomList[1]) {
                            2 -> R.drawable.two
                            3 -> R.drawable.three
                            4 -> R.drawable.four
                            5 -> R.drawable.five
                            6 -> R.drawable.six
                            else -> R.drawable.seven
                        }
                        ),
                        contentDescription = ""
                    )
                }
            }
            // 3
            Box(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(130.dp)
                        .width(130.dp)
                        .background(
                            color = Color(0xffffffff),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            visualAcuityViewModel.processAnswerSelected(
                                2,
                                handleWrong = {
                                    progress = it
                                }
                            ) {
                                toResultScreen(
                                    visualAcuityViewModel.getVisualAcuityTestResult()
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(100.dp),
                        imageVector = ImageVector.vectorResource(id =
                        when(randomList[2]) {
                            2 -> R.drawable.two
                            3 -> R.drawable.three
                            4 -> R.drawable.four
                            5 -> R.drawable.five
                            6 -> R.drawable.six
                            else -> R.drawable.seven
                        }
                        ),
                        contentDescription = ""
                    )
                }
            }
        }
        // 4
        Box(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(110.dp)
                    .width(430.dp)
                    .background(
                        color = Color(0xffffffff),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        visualAcuityViewModel.processAnswerSelected(
                            3,
                            handleWrong = {
                                progress = it
                            }
                        ) {
                            toResultScreen(
                                visualAcuityViewModel.getVisualAcuityTestResult()
                            )
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "안보임",
                    fontSize = 60.sp,
                )
//                Image(
//                    modifier = Modifier
//                        .padding(10.dp)
//                        .height(150.dp),
//                    imageVector = ImageVector.vectorResource(id = R.drawable.question_mark),
//                    contentDescription = ""
//                )
            }
        }
    }
}

//@Composable
//fun SightednessTestContent(
//    toResultScreen: () -> Unit,
//    viewModel: NenoonViewModel,
//    visualAcuityTestSightednessTestContentVisibleState: MutableTransitionState<Boolean>
//) {
//    val isLeftEye = viewModel.isLeftEye.collectAsState().value
//    AnimatedVisibility(
//        visibleState = visualAcuityTestSightednessTestContentVisibleState,
//        enter = AnimationProvider.enterTransition,
//        exit = AnimationProvider.exitTransition
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(top = 100.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = StringProvider.getString(R.string.visual_acuity_test_common_content_signtedness_description),
//                color = Color(0xffffffff),
//                fontSize = 40.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(
//                modifier = Modifier
//                    .height(20.dp)
//            )
//            Box() {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .height(500.dp)
//                            .width(250.dp)
//                            .background(
//                                color = Color(0xff00ff00),
//                                shape = RoundedCornerShape(
//                                    topStart = 8.dp,
//                                    topEnd = 0.dp,
//                                    bottomEnd = 0.dp,
//                                    bottomStart = 8.dp
//                                )
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            text = StringProvider.getString(R.string.visual_acuity_test_common_content_green),
//                            fontSize = 48.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .height(500.dp)
//                            .width(250.dp)
//                            .background(
//                                color = Color(0xffff0000),
//                                shape = RoundedCornerShape(
//                                    topStart = 0.dp,
//                                    topEnd = 8.dp,
//                                    bottomEnd = 8.dp,
//                                    bottomStart = 0.dp
//                                )
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            text = StringProvider.getString(R.string.visual_acuity_test_common_content_red),
//                            fontSize = 48.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                }
//            }
//            Spacer(
//                modifier = Modifier
//                    .height(20.dp)
//            )
//            Column(
//                modifier = Modifier
//                    .padding(bottom = GlobalValue.navigationBarPadding.dp)
//                    .fillMaxHeight(),
//                verticalArrangement = Arrangement.Bottom
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(start = 40.dp, end = 40.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .weight(1f)
//                            .height(80.dp)
//                            .background(
//                                color = Color(0xff00ff00),
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .clickable {
//                                if (isLeftEye) {
//                                    viewModel.updateLeftEyeSightedValue(VisionDisorderType.Hyperopia)
//                                } else {
//                                    viewModel.updateRightEyeSightedValue(VisionDisorderType.Hyperopia)
//                                    toResultScreen()
//                                }
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = StringProvider.getString(R.string.visual_acuity_test_common_content_green),
//                            fontSize = 30.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//
//                    Box(
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .weight(1f)
//                            .height(80.dp)
//                            .background(
//                                color = Color(0xffff0000),
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .clickable {
//                                if (isLeftEye) {
//                                    viewModel.updateLeftEyeSightedValue(VisionDisorderType.Myopia)
//                                } else {
//                                    viewModel.updateRightEyeSightedValue(VisionDisorderType.Myopia)
//                                    toResultScreen()
//                                }
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = StringProvider.getString(R.string.visual_acuity_test_common_content_red),
//                            fontSize = 30.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//                Row(
//                    modifier = Modifier
//                        .padding(start = 40.dp, end = 40.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .weight(1f)
//                            .height(80.dp)
//                            .background(
//                                color = Color(0xffffffff),
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .clickable {
//                                if (isLeftEye) {
//                                    viewModel.updateLeftEyeSightedValue(VisionDisorderType.Normal)
//                                } else {
//                                    viewModel.updateRightEyeSightedValue(VisionDisorderType.Normal)
//                                    toResultScreen()
//                                }
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = StringProvider.getString(R.string.visual_acuity_test_common_content_both_positive),
//                            fontSize = 30.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                    Box(
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .weight(1f)
//                            .height(80.dp)
//                            .background(
//                                color = Color(0xffffffff),
//                                shape = RoundedCornerShape(8.dp)
//                            )
//                            .clickable {
//                                if (isLeftEye) {
//                                    viewModel.updateLeftEyeSightedValue(VisionDisorderType.Astigmatism)
//                                } else {
//                                    viewModel.updateRightEyeSightedValue(VisionDisorderType.Astigmatism)
//                                    toResultScreen()
//                                }
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = StringProvider.getString(R.string.visual_acuity_test_common_content_both_negative),
//                            fontSize = 30.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//            }
//        }
//    }
//}