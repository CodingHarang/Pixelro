package com.pixelro.nenoonkiosk.ui.testcontent

import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    val heightDP = LocalConfiguration.current.screenHeightDp / 3
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
                    .padding(40.dp),
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
                        when(selectedTest) {
                            TestType.Presbyopia -> navController.navigate(GlobalConstants.ROUTE_PRESBYOPIA_TEST)
                            TestType.ShortDistanceVisualAcuity -> navController.navigate(GlobalConstants.ROUTE_SHORT_VISUAL_ACUITY_TEST)
                            TestType.LongDistanceVisualAcuity -> navController.navigate(GlobalConstants.ROUTE_LONG_VISUAL_ACUITY_TEST)
                            TestType.ChildrenVisualAcuity -> navController.navigate(GlobalConstants.ROUTE_CHILDREN_VISUAL_ACUITY_TEST)
                            TestType.AmslerGrid -> navController.navigate(GlobalConstants.ROUTE_AMSLER_GRID_TEST)
                            else -> navController.navigate(GlobalConstants.ROUTE_M_CHART_TEST)
                        }
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
        onDismissRequest = onDismissRequest
    ) {
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
                    fontWeight = FontWeight.Medium,
                    color = Color(0xff1d71e1),
                    textAlign = TextAlign.Center
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
                    fontWeight = FontWeight.Normal,
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