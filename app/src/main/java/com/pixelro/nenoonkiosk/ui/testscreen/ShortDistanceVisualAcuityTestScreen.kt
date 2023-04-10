package com.pixelro.nenoonkiosk.ui.testscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.ui.testcontent.ShortDistanceVisualAcuityTestContent

@Composable
fun ShortDistanceVisualAcuityTestScreen(
    navController: NavHostController,
    viewModel: NenoonViewModel
) {
    FaceDetection(
        viewModel = viewModel
    )
    ShortDistanceVisualAcuityTestContent(
        viewModel = viewModel
    )
//    Column() {
//        Text("ShortDistanceVisualAcuityTestScreen")
//        Spacer(
//            modifier = Modifier.
//            height(20.dp)
//        )
//        Icon(
//            imageVector = ImageVector.vectorResource(id = R.drawable.round),
//            contentDescription = ""
//        )
//        Image(
//            modifier = Modifier
//                .height(5.dp)
//                .width(5.dp),
//            imageVector = ImageVector.vectorResource(
//                id = R.drawable.round
//            ),
//            contentScale = ContentScale.Fit,
//            contentDescription = ""
//        )
//    }
}