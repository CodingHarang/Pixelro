package com.pixelro.nenoonkiosk.facedetection

import android.Manifest
import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pixelro.nenoonkiosk.NenoonViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceDetection(
    viewModel: NenoonViewModel
) {

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
        )
    )

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }
    FaceDetectionScreenContent(
        viewModel = viewModel
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FaceDetectionScreenContent(
    viewModel: NenoonViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    Box() {
        val systemUiController = rememberSystemUiController()
        systemUiController.isSystemBarsVisible = false
        val executor = ContextCompat.getMainExecutor(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(640, 480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setImageQueueDepth(5).build().apply {
                    setAnalyzer(
                        executor, MyFaceAnalyzer(
                            viewModel::updateFaceDetectionData,
                            viewModel::updateFaceContourData,
                            viewModel::updateInputImageSize,
                            viewModel::updateEyeOpenProbability
                        )
                    )
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, imageAnalysis
            )

        }, executor)

        systemUiController.isSystemBarsVisible = false
    }
//    Scaffold {
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .onGloballyPositioned {
//                size = it.size
//            }) {
//            AndroidView(modifier = Modifier.matchParentSize(), factory = { ctx ->
//                val previewView = PreviewView(ctx)
//
//
//                previewView
//            })
//        }
//    }
}
