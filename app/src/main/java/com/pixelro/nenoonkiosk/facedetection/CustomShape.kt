package com.pixelro.nenoonkiosk.facedetection

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.pixelro.nenoonkiosk.NenoonViewModel

@Composable
fun CustomShape(
    viewModel: NenoonViewModel = viewModel()
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val inputImgSizeX = viewModel.inputImageSizeX.collectAsState().value
    val inputImgSizeY = viewModel.inputImageSizeY.collectAsState().value

    val leftEyePosition = viewModel.leftEyePosition.collectAsState().value
    val rightEyePosition = viewModel.rightEyePosition.collectAsState().value
    val leftEyeContour = viewModel.leftEyeContour.collectAsState().value
    val rightEyeContour = viewModel.rightEyeContour.collectAsState().value
    val upperLipTopContour = viewModel.upperLipTopContour.collectAsState().value
    val upperLipBottomContour = viewModel.upperLipBottomContour.collectAsState().value
    val lowerLipTopContour = viewModel.lowerLipTopContour.collectAsState().value
    val lowerLipBottomContour = viewModel.lowerLipBottomContour.collectAsState().value
    val faceContour = viewModel.faceContour.collectAsState().value

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                size = it.size
            }
    ) {
        drawCircle(
            color = Color(0xFFFF0000),
            radius = 3f,
            center = Offset(size.width - (leftEyePosition.x * size.width), leftEyePosition.y * size.width)
        )
        drawCircle(
            color = Color(0xFFFF0000),
            radius = 3f,
            center = Offset(size.width - (rightEyePosition.x * size.width), rightEyePosition.y * size.width)
        )
        for(point in leftEyeContour) {
            drawCircle(
                color = Color(0xFFFF0000),
                radius = 3f,
                center = Offset(size.width - (point.x * size.width), point.y * size.width)
            )
        }
        for(point in rightEyeContour) {
            drawCircle(
                color = Color(0xFF0000FF),
                radius = 3f,
                center = Offset(size.width - (point.x * size.width), point.y * size.width)
            )
        }
        for(point in upperLipTopContour) {
            drawCircle(
                color = Color(0xFFFF0000),
                radius = 3f,
                center = Offset(size.width - (point.x * size.width), point.y * size.width)
            )
        }
        for(point in upperLipBottomContour) {
            drawCircle(
                color = Color(0xFFFF0000),
                radius = 3f,
                center = Offset(size.width - (point.x * size.width), point.y * size.width)
            )
        }
        for(point in lowerLipTopContour) {
            drawCircle(
                color = Color(0xFFFF0000),
                radius = 3f,
                center = Offset(size.width - (point.x * size.width), point.y * size.width)
            )
        }
        for(point in lowerLipBottomContour) {
            drawCircle(
                color = Color(0xFFFF0000),
                radius = 3f,
                center = Offset(size.width - (point.x * size.width), point.y * size.width)
            )
        }
        for(point in faceContour) {
            drawCircle(
                color = Color(0xFFFF0000),
                radius = 3f,
                center = Offset(size.width - (point.x * size.width), point.y * size.width)
            )
        }
        drawCircle(
            color = Color(0xFF00FF00),
            radius = 10f,
            center = Offset(0f, 0f)
        )
        drawCircle(
            color = Color(0xFF00FF00),
            radius = 10f,
            center = Offset(size.width.toFloat(), size.height.toFloat())
        )
    }
}