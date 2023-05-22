package com.pixelro.nenoonkiosk.ui.screen

import android.app.Activity
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Text(
                modifier = Modifier
                    .padding(top = 80.dp, bottom = 40.dp),
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
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold
            )
            content()
        }
        Image(
            modifier = Modifier
                .width(70.dp)
                .height(70.dp)
                .padding(start = 20.dp, top = 20.dp)
                .clickable {
                    (context as Activity).dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
                    context.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK))
                },
            painter = painterResource(id = R.drawable.close_button),
            contentDescription = ""
        )
    }

}