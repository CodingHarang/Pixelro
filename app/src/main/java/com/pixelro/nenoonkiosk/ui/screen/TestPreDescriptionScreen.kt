package com.pixelro.nenoonkiosk.ui.screen

import android.app.Activity
import android.util.Log
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.pixelro.nenoonkiosk.data.TestType

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
            .paint(
                painterResource(id = R.drawable.bg_eyetest_info),
                contentScale = ContentScale.FillWidth
            )
    ) {
        TestPreDescriptionBackground()
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
fun TestPreDescriptionBackground() {
    val context = LocalContext.current
    val heightDP = LocalConfiguration.current.screenHeightDp / 3
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Image(
                modifier = Modifier
                    .height(heightDP.dp),
                contentScale = ContentScale.Fit,
                painter = painterResource(id = R.drawable.img_test),
                contentDescription = ""
            )
        }
        Image(
            modifier = Modifier
                .width(70.dp)
                .height(70.dp)
                .padding(start = 20.dp, top = 20.dp)
                .clickable {
                    (context as Activity).dispatchKeyEvent(
                        KeyEvent(
                            KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_BACK
                        )
                    )
                    context.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                },
            painter = painterResource(id = R.drawable.close_button),
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 120.dp, bottom = 40.dp),
                text = viewModel.selectedTestName.collectAsState().value,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xffffffff),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(start = 60.dp, end = 60.dp),
                text = viewModel.selectedTestDescription.collectAsState().value,
                fontSize = 30.sp,
                color = Color(0xffffffff)
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .height(100.dp)
                    .padding(bottom = 16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        changeDialogVisibility()
                    },
                painter = painterResource(id = R.drawable.but_how_text),
                contentDescription = ""
            )
            Image(
                modifier = Modifier
                    .height(200.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navController.navigate(GlobalConstants.ROUTE_TEST_CONTENT)
                    },
                painter = painterResource(id = R.drawable.btn_start_ko),
                contentDescription = ""
            )
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

//        val systemUiController = rememberSystemUiController()
//        systemUiController.isSystemBarsVisible = false
        Surface(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = viewModel.selectedTestName.collectAsState().value,
                    modifier = Modifier
                        .padding(top = 20.dp),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xff1d71e1),
                    textAlign = TextAlign.Center
                )
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                )
                Text(
                    text = "검사 방법",
                    modifier = Modifier,
                    fontSize = 25.sp,
                    color = Color(0xff1d71e1),
                )
                GlideImage(
                    modifier = Modifier
                        .width(700.dp)
                        .height(350.dp)
                        .padding(start = 20.dp, end = 20.dp),
                    model = R.raw.img_start_presbyopia,
                    contentScale = ContentScale.Fit,
                    contentDescription = ""
                )
                Image(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    painter = painterResource(
                        id = when(selectedTest) {
                                TestType.Presbyopia -> R.drawable.text_popup_ko_presbyopia
                                TestType.ShortDistanceVisualAcuity -> R.drawable.text_popup_ko_near
                                TestType.LongDistanceVisualAcuity -> R.drawable.text_popup_ko_long
                                TestType.ChildrenVisualAcuity -> R.drawable.text_popup_ko_child
                                TestType.AmslerGrid -> R.drawable.text_popup_ko_amsler
                                else -> R.drawable.text_popup_ko_m_chart
                            }
                        ),
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth
                )
                Spacer(
                    modifier = Modifier
                        .background(Color(0xffcccccc))
                        .height(1.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = "확인",
                    fontSize = 30.sp,
                    color = Color(0xff1d71e1),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onDismissRequest()
                        }
                )
            }
        }
    }
}