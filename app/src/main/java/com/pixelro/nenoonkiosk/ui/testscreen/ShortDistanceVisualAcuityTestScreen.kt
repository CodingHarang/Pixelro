package com.pixelro.nenoonkiosk.ui.testscreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.pixelro.nenoonkiosk.NenoonViewModel
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
//    Column() {
//        Text(
//            text = "${viewModel.leftEyeOpenProbability.collectAsState().value.toString().format(DecimalFormat("00.00"))}, ${viewModel.rightEyeOpenProbability.collectAsState().value.toString().format(DecimalFormat("00.00"))}",
//            fontSize = 40.sp
//        )
//    }
    DisposableEffect(true) {
        viewModel.initializeSightTest()
        onDispose() {

        }
    }
    ShortDistanceVisualAcuityTestContent(
        toResultScreen = toResultScreen,
        viewModel = viewModel
    )
}