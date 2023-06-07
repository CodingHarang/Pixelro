package com.pixelro.nenoonkiosk.ui.screen

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.GlobalConstants
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.ui.testcontent.TestPreDescriptionScreenDialogContent

@Composable
fun TestPreDescriptionScreen(
    viewModel: NenoonViewModel,
    navController: NavHostController
) {
    Log.e("TestPreDescriptionScreen", "TestPreDescriptionScreen")
    val selectedTest = viewModel.selectedTestType.collectAsState().value
    var isDescriptionDialogShowing by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .paint(
//                painterResource(id = R.drawable.bg_eyetest_info),
//                contentScale = ContentScale.FillWidth
//            )
    ) {
        TestPreDescriptionBackground(
            viewModel = viewModel
        )
        TestPreDescriptionContent(
            viewModel = viewModel,
            navController = navController,
            selectedTest = selectedTest,
            changeDialogVisibility = {
                isDescriptionDialogShowing = true
            }
        )
        if(isDescriptionDialogShowing) {
            TestPreDescriptionDialog(
                viewModel = viewModel,
                selectedTest = selectedTest,
                onDismissRequest = {
                    isDescriptionDialogShowing = false
                }
            )
        }
    }
}

@Composable
fun TestPreDescriptionBackground(
    viewModel: NenoonViewModel
) {
    val context = LocalContext.current
    val heightDP = LocalConfiguration.current.screenHeightDp / 3
    Box(
        modifier = Modifier
            .padding(start = 40.dp, top = (viewModel.statusBarPadding.collectAsState().value + 20).dp, end = 40.dp, bottom = 20.dp)
            .fillMaxWidth()
            .height(40.dp),
        contentAlignment = Alignment.CenterStart
    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize(),
//            contentAlignment = Alignment.BottomCenter
//        ) {
//            Image(
//                modifier = Modifier
//                    .height(heightDP.dp),
//                contentScale = ContentScale.Fit,
//                painter = painterResource(id = R.drawable.img_test),
//                contentDescription = ""
//            )
//        }
        Image(
            modifier = Modifier
                .width(28.dp)
                .clickable {
                    (context as Activity).dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                    context.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                },
            painter = painterResource(id = R.drawable.icon_back_black),
            contentDescription = ""
        )
    }
}

@Composable
fun TestPreDescriptionContent(
    viewModel: NenoonViewModel,
    navController: NavHostController,
    selectedTest: TestType,
    changeDialogVisibility: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 40.dp, top = 200.dp, bottom = 40.dp),
                text = viewModel.predescriptionTitle.collectAsState().value,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 60.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 40.dp, end = 120.dp),
                text = viewModel.selectedTestDescription.collectAsState().value,
                fontSize = 24.sp,
                lineHeight = 35.sp
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 300.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Box(
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    modifier = Modifier
//                        .height(140.dp)
//                        .padding(bottom = 16.dp)
//                        .clickable(
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() }
//                        ) {
//                            changeDialogVisibility()
//                        },
//                    painter = painterResource(id = R.drawable.but_how_box),
//                    contentDescription = ""
//                )
//                Text(
//                    modifier = Modifier
//                        .offset(0.dp, (-20).dp),
//                    text = buildAnnotatedString {
//                        append(StringProvider.getString(R.string.test_predescription_speech_buttle_text1))
//                        withStyle(
//                            style = SpanStyle(
//                                textDecoration = TextDecoration.Underline
//                            )
//                        ) {
//                            append(StringProvider.getString(R.string.test_predescription_speech_buttle_text2))
//                        }
//                        append(StringProvider.getString(R.string.test_predescription_speech_buttle_text3))
//                    },
//                    fontSize = 30.sp,
//                    color = Color(0xff1d71e1),
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center,
//                    lineHeight = 35.sp
//                )
//            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp, bottom = 20.dp)
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color(0xfff7f7f7),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clickable
//                        (
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() }
//                    )
                    {
                        changeDialogVisibility()
                    }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
//                        modifier = Modifier
//                            .offset(0.dp, (-4).dp),
                    text = StringProvider.getString(R.string.test_predescription_screen_test_method),
                    fontSize = 24.sp,
                    color = Color(0xff1d71e1),
//                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp, bottom = (viewModel.navigationBarPadding.collectAsState().value).dp)
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color(0xff1d71e1),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clickable {
                        navController.navigate(GlobalConstants.ROUTE_TEST_CONTENT)
                    }
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
//                        modifier = Modifier
//                            .offset(0.dp, (-4).dp),
                    text = StringProvider.getString(R.string.test_predescription_screen_start),
                    fontSize = 24.sp,
                    color = Color(0xffffffff),
//                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TestPreDescriptionDialog(
    viewModel: NenoonViewModel,
    selectedTest: TestType,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(60f)
                .height(IntrinsicSize.Max)
                .background(
                    color = Color(0xffffffff),
                    shape = RoundedCornerShape(16.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Text(
//                text = viewModel.selectedTestName.collectAsState().value,
//                modifier = Modifier
//                    .padding(top = 20.dp),
//                fontSize = 40.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xff1d71e1),
//                textAlign = TextAlign.Center,
//                lineHeight = 50.sp
//            )
//            Spacer(
//                modifier = Modifier
//                    .height(20.dp)
//            )
            Text(
                text = StringProvider.getString(R.string.test_order_dialog_test_order),
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                fontSize = 25.sp,
                color = Color(0xff1d71e1),
                fontWeight = FontWeight.Bold
            )
            TestPreDescriptionScreenDialogContent(selectedTest)

//            Spacer(
//                modifier = Modifier
//                    .background(Color(0xffcccccc))
//                    .height(1.dp)
//                    .fillMaxWidth()
//            )
            Text(
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 20.dp)
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        border = BorderStroke(1.dp, Color(0xffc3c3c3)),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable
//                        (
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() })
                    {
                        onDismissRequest()
                    }
                    .padding(20.dp),
                text = "확인",
                fontSize = 30.sp,
                color = Color(0xff1d71e1),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
//        val systemUiController = rememberSystemUiController()
//        systemUiController.isSystemBarsVisible = false
//        Surface(
//            modifier = Modifier
//                .width(IntrinsicSize.Max)
//                .wrapContentHeight(),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = viewModel.selectedTestName.collectAsState().value,
//                    modifier = Modifier
//                        .padding(top = 20.dp),
//                    fontSize = 40.sp,
//                    fontWeight = FontWeight.ExtraBold,
//                    color = Color(0xff1d71e1),
//                    textAlign = TextAlign.Center
//                )
//                Spacer(
//                    modifier = Modifier
//                        .height(20.dp)
//                )
//                Text(
//                    text = StringProvider.getString(R.string.test_order_dialog_test_order),
//                    modifier = Modifier,
//                    fontSize = 25.sp,
//                    color = Color(0xff1d71e1),
//                    fontWeight = FontWeight.Bold
//                )
//                GlideImage(
//                    modifier = Modifier
//                        .width(700.dp)
//                        .height(350.dp)
//                        .padding(start = 20.dp, end = 20.dp),
//                    model = R.raw.img_start_presbyopia,
//                    contentScale = ContentScale.Fit,
//                    contentDescription = ""
//                )
//                Image(
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
//                    painter = painterResource(
//                        id = when(selectedTest) {
//                                TestType.Presbyopia -> R.drawable.text_popup_ko_presbyopia
//                                TestType.ShortDistanceVisualAcuity -> R.drawable.text_popup_ko_near
//                                TestType.LongDistanceVisualAcuity -> R.drawable.text_popup_ko_long
//                                TestType.ChildrenVisualAcuity -> R.drawable.text_popup_ko_child
//                                TestType.AmslerGrid -> R.drawable.text_popup_ko_amsler
//                                else -> R.drawable.text_popup_ko_m_chart
//                            }
//                        ),
//                    contentDescription = "",
//                    contentScale = ContentScale.FillWidth
//                )
//                Spacer(
//                    modifier = Modifier
//                        .background(Color(0xffcccccc))
//                        .height(1.dp)
//                        .fillMaxWidth()
//                )
//                Text(
//                    text = "확인",
//                    fontSize = 30.sp,
//                    color = Color(0xff1d71e1),
//                    fontWeight = FontWeight.Bold,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .padding(top = 12.dp, bottom = 12.dp)
//                        .fillMaxWidth()
//                        .clickable(
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() }
//                        ) {
//                            onDismissRequest()
//                        }
//                )
//            }
//        }
//    }
    }
}