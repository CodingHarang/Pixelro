package com.pixelro.nenoonkiosk.ui.testscreen
import kotlin.math.*
import java.text.DecimalFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelro.nenoonkiosk.NenoonViewModel
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.facedetection.FaceDetection
import com.pixelro.nenoonkiosk.ui.testcontent.ShortDistanceVisualAcuityTestContent

@Composable
fun ShortDistanceVisualAcuityTestScreen(
    toResultScreen: () -> Unit,
    viewModel: NenoonViewModel
) {
    FaceDetection(
        viewModel = viewModel
    )
    viewModel.initializeSightTest()
//    Column() {
//        Text(
//            text = "${viewModel.leftEyeOpenProbability.collectAsState().value.toString().format(DecimalFormat("00.00"))}, ${viewModel.rightEyeOpenProbability.collectAsState().value.toString().format(DecimalFormat("00.00"))}",
//            fontSize = 40.sp
//        )
//    }
    ShortDistanceVisualAcuityTestContent(
        toResultScreen = toResultScreen,
        viewModel = viewModel
    )
}