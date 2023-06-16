package com.pixelro.nenoonkiosk.facedetection

import android.Manifest
import android.animation.TimeInterpolator
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.pixelro.nenoonkiosk.NenoonViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceDetectionWithPreview(
    viewModel: NenoonViewModel,
    visibleState: MutableTransitionState<Boolean>
) {

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
        )
    )

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }
    FaceDetectionScreenContentWithPreview(
        viewModel = viewModel,
        visibleState = visibleState
    )
}

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

@Composable
fun FaceDetectionScreenContent(
    viewModel: NenoonViewModel
) {
//    Log.e("androidView1", "androidView1")
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    Box() {
        LaunchedEffect(true) {
            val executor = ContextCompat.getMainExecutor(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1200, 1200))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setImageQueueDepth(5).build().apply {
                        setAnalyzer(
                            executor, MyFaceAnalyzer(
                                viewModel::updateFaceDetectionData,
                                viewModel::updateFaceContourData,
                                viewModel::updateInputImageSize,
                                viewModel::updateEyeOpenProbability,
//                                viewModel::updateBitmap
                            )
                        )
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, imageAnalysis
                )
            }, executor)
        }
    }
}

@Composable
fun FaceDetectionScreenContentWithPreview(
    viewModel: NenoonViewModel,
    visibleState: MutableTransitionState<Boolean>
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    if(visibleState.targetState) {
        Surface() {
//            Log.e("androidView2", "androidView2")
            AndroidView(
                modifier = Modifier
                    .height(740.dp),
                factory = { context ->
                    val previewView = PreviewView(context)
                    previewView.scaleType = PreviewView.ScaleType.FILL_END
                    val executor = ContextCompat.getMainExecutor(context)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(Size(1200, 1200))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .setImageQueueDepth(5).build().apply {
                                setAnalyzer(
                                    executor, MyFaceAnalyzer(
                                        viewModel::updateFaceDetectionData,
                                        viewModel::updateFaceContourData,
                                        viewModel::updateInputImageSize,
                                        viewModel::updateEyeOpenProbability,
//                                    viewModel::updateBitmap
                                    )
                                )
                            }
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalysis)
                    }, executor)
                    previewView
                }
            )
        }
    }
}