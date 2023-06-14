package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.AnimationProvider
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.FaceDetectionWithPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
        var hide = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                modifier = Modifier
//                    .padding(start = 40.dp, end = 40.dp)
//                    .fillMaxWidth(),
//                text = StringProvider.getString(R.string.measuring_distance_content_description2),
//                color = Color(0xffffffff),
//                fontSize = 20.sp
//            )
//            Image(
//                modifier = Modifier
//                    .padding(start = 40.dp, top = 60.dp, end = 40.dp, bottom = 20.dp),
//                painter = painterResource(id = R.drawable.img_eyetest_maneyes),
//                contentDescription = ""
//            )
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
                    FaceDetectionWithPreview(
                        viewModel = viewModel,
                        visibleState = measuringDistanceContentVisibleState
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(
                                color = Color(0xff000000)
                            )
                    )
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
                        text = StringProvider.getString(R.string.measuring_distance_content_description1),
                        color = Color(0xffffffff),
                        fontSize = 30.sp
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
                            .padding(bottom = (viewModel.navigationBarPadding.collectAsState().value + 344).dp),
                        text = StringProvider.getString(R.string.test_screen_current_distance),
                        color = Color(0xffffffff),
                        fontSize = 24.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(bottom = (viewModel.navigationBarPadding.collectAsState().value + 240).dp),
                        color = when(viewModel.selectedTestType.collectAsState().value) {
                            TestType.ShortDistanceVisualAcuity -> {
                                when(viewModel.screenToFaceDistance.collectAsState().value) {
                                    in 370.0..430.0 -> Color(0xffffffff)
                                    else -> Color(0xFF6D6D6D)
                                }
                            }
                            else -> {
                                when(viewModel.screenToFaceDistance.collectAsState().value) {
                                    in 270.0..330.0 -> Color(0xffffffff)
                                    else -> Color(0xFF6D6D6D)
                                }
                            }
                        },
                        text = "${(viewModel.screenToFaceDistance.collectAsState().value / 10).roundToInt()}cm",
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .padding(
                                start = 40.dp,
                                end = 40.dp,
                                bottom = (viewModel.navigationBarPadding.collectAsState().value + 160).dp
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
                                TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.adjust_distance_40cm)
                                else -> StringProvider.getString(R.string.adjust_distance_30cm)
                            },
                            fontSize = 24.sp,
                            color = Color(0xffffffff),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if(viewModel.screenToFaceDistance.collectAsState().value in when(viewModel.selectedTestType.collectAsState().value) {
                        TestType.ShortDistanceVisualAcuity -> (370.0..430.0)
                        else -> (270.0..330.0)
                    }) {
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
                                hide.value = true
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
            }
        }
    }
}