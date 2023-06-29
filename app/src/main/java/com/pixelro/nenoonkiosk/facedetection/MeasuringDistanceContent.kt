package com.pixelro.nenoonkiosk.facedetection

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalValue
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import kotlin.math.roundToInt

@Composable
fun MeasuringDistanceContent(
    measuringDistanceContentVisibleState: MutableTransitionState<Boolean>,
    toNextContent: () -> Unit,
    selectedTestType: TestType,
    isLeftEye: Boolean,
    faceDetectionViewModel: FaceDetectionViewModel = hiltViewModel()
) {
    AnimatedVisibility(
        visibleState = measuringDistanceContentVisibleState,
        enter = AnimationProvider.enterTransition,
        exit = AnimationProvider.exitTransition
    ) {
        val transition = rememberInfiniteTransition()
        val shiftVal by transition.animateFloat(
            initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    delayMillis = 500
                },
                repeatMode = RepeatMode.Reverse
            )
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .height(740.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(740.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
//                Image(
//                    bitmap = viewModel.bitmap.collectAsState().value.asImageBitmap(),
//                    contentDescription = ""
//                )
                    if (measuringDistanceContentVisibleState.targetState) {
                        FaceDetectionWithPreview()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(
                                color = Color(0xff000000)
                            )
                    )
                    // eye tracking red dot
//                    CustomShape()
                }
                Box(
                    modifier = Modifier
                        .padding(top = 80.dp)
                        .fillMaxWidth()
                        .height(600.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.face_frame),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color(0xff1d71e1))
                    )
//                    Image(
//                        modifier = Modifier
//                            .offset(
//                                x = (((faceDetectionViewModel.textBox.collectAsState().value?.right ?: 0) + (faceDetectionViewModel.textBox.collectAsState().value?.left ?: 0)) / 2).dp,
//                                y = (((faceDetectionViewModel.textBox.collectAsState().value?.bottom ?: 0) + (faceDetectionViewModel.textBox.collectAsState().value?.top ?: 0)) / 2).dp
//                            ),
//                        painter = painterResource(id = R.drawable.eyecover),
//                        contentDescription = null,
//                    )
                    if (faceDetectionViewModel.isFaceDetected.collectAsState().value) {
                        if (!isLeftEye) {
                            Image(
                                modifier = Modifier
                                    .width((200 * 300 / faceDetectionViewModel.screenToFaceDistance.collectAsState().value).dp)
                                    .height((400 * 300 / faceDetectionViewModel.screenToFaceDistance.collectAsState().value).dp)
                                    .offset(
                                        x = (430 - (faceDetectionViewModel.rightEyePosition.collectAsState().value.x / 2.25f)).dp,
                                        y = (faceDetectionViewModel.rightEyePosition.collectAsState().value.y / 2.25f - 500).dp
                                    )
                                    .alpha(shiftVal),
                                painter = painterResource(id = R.drawable.eyecover),
                                contentDescription = null,
                            )
                        } else {
                            Image(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(400.dp)
                                    .offset(
                                        x = (430 - (faceDetectionViewModel.leftEyePosition.collectAsState().value.x / 2.25f)).dp,
                                        y = (faceDetectionViewModel.leftEyePosition.collectAsState().value.y / 2.25f - 500).dp
                                    )
                                    .alpha(shiftVal),
                                painter = painterResource(id = R.drawable.eyecover),
                                contentDescription = null,
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            color = Color(0xff000000)
                        ),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 40.dp, bottom = 20.dp),
                        text = when(faceDetectionViewModel.isFaceDetected.collectAsState().value) {
                            true -> when(isLeftEye) {
                                true -> when(faceDetectionViewModel.isLeftEyeCovered.collectAsState().value && faceDetectionViewModel.isNenoonTextDetected.collectAsState().value) {
                                    true -> StringProvider.getString(R.string.measuring_distance_content_description4)
                                    false -> StringProvider.getString(R.string.measuring_distance_content_description2)
                                }
                                false -> when(faceDetectionViewModel.isRightEyeCovered.collectAsState().value && faceDetectionViewModel.isNenoonTextDetected.collectAsState().value) {
                                    true -> StringProvider.getString(R.string.measuring_distance_content_description4)
                                    false -> StringProvider.getString(R.string.measuring_distance_content_description3)
                                }
                            }
                            false -> StringProvider.getString(R.string.measuring_distance_content_description1)
                        },
                            color = Color(0xffffffff),
                            fontSize = 36.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = (GlobalValue.navigationBarPadding + 324).dp),
                        text = StringProvider.getString(R.string.test_screen_current_distance),
                        color = Color(0xffffffff),
                        fontSize = 24.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(bottom = (GlobalValue.navigationBarPadding + 220).dp),
                        color = when (selectedTestType) {
                            TestType.ShortDistanceVisualAcuity -> {
                                when (faceDetectionViewModel.screenToFaceDistance.collectAsState().value) {
                                    in 370.0..430.0 -> Color(0xffffffff)
                                    else -> Color(0xFF6D6D6D)
                                }
                            }

                            else -> {
                                when (faceDetectionViewModel.screenToFaceDistance.collectAsState().value) {
                                    in 270.0..330.0 -> Color(0xffffffff)
                                    else -> Color(0xFF6D6D6D)
                                }
                            }
                        },
                        text = "${(faceDetectionViewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 40.dp,
                                end = 40.dp,
                                bottom = (GlobalValue.navigationBarPadding + 140).dp
                            )
                            .border(
                                border = BorderStroke(1.dp, Color(0xffffffff)),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (selectedTestType) {
                                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.adjust_distance_40cm)
                                else -> StringProvider.getString(R.string.adjust_distance_30cm)
                            },
                            fontSize = 24.sp,
                            color = Color(0xffffffff),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (faceDetectionViewModel.screenToFaceDistance.collectAsState().value in when (selectedTestType) {
                        TestType.ShortDistanceVisualAcuity -> (370.0..430.0)
                        else -> (270.0..330.0)
//                        TestType.ShortDistanceVisualAcuity -> (-100.0..100.0)
//                        else -> (-100.0..100.0)
                    } && faceDetectionViewModel.isNenoonTextDetected.collectAsState().value
                ) {
                    faceDetectionViewModel.updateIsDistanceOK(true)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 40.dp,
                                end = 40.dp,
                                bottom = GlobalValue.navigationBarPadding.dp
                            )
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = Color(0xff1d71e1),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .clickable {
                                toNextContent()
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
                } else {
                    faceDetectionViewModel.updateIsDistanceOK(false)
                }
            }
        }
    }
}