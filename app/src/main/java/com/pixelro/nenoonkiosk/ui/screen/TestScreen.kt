package com.pixelro.nenoonkiosk.ui.screen

import android.app.Activity
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType
import com.pixelro.nenoonkiosk.facedetection.FaceDetection

@Composable
fun EyeTestScreen(
    viewModel: NenoonViewModel,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    DisposableEffect(true) {
//        systemUiController.setNavigationBarColor(
//            color = Color(0x00000000),
//            darkIcons = false
//        )
    systemUiController.systemBarsDarkContentEnabled = false
    onDispose {
        systemUiController.systemBarsDarkContentEnabled = true
//            systemUiController.setNavigationBarColor(
//                color = Color(0x00000000),
//                darkIcons = true
//            )
    }
}
    val selectedTestType = viewModel.selectedTestType.collectAsState().value
    val context = LocalContext.current
    FaceDetection(
        viewModel = viewModel
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xff000000)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 40.dp, top = (viewModel.statusBarPadding.collectAsState().value + 20).dp, end = 40.dp, bottom = 20.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Image(
                        modifier = Modifier
                            .width(32.dp)
                            .clickable {
                                (context as Activity).dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                                context.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                            },
                        painter = painterResource(id = R.drawable.close_button),
                        contentDescription = ""
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when(selectedTestType) {
                            TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_name1)
                            TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_name)
                            TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_name)
                            TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_name)
                            TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_name)
                            TestType.MChart -> StringProvider.getString(R.string.mchart_name)
                            else -> ""
                        },
                        color = Color(0xffffffff),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            content()
        }
//        Image(
//            modifier = Modifier
//                .width(70.dp)
//                .height(70.dp)
//                .padding(start = 20.dp, top = 20.dp)
//                .clickable {
//                    (context as Activity).dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
//                    context.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
//                },
//            painter = painterResource(id = R.drawable.close_button),
//            contentDescription = ""
//        )
    }

}